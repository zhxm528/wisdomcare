import { readFileSync } from 'node:fs';
import { fileURLToPath } from 'node:url';
import { resolve } from 'node:path';

const configRoot = fileURLToPath(new URL('../infra/config-center/', import.meta.url));

export function resolveEnvironment(args = process.argv.slice(2), env = process.env) {
  const explicit = args.find(arg => arg.startsWith('--env='))?.slice(6);
  const value = explicit ?? env.WISDOMCARE_ENV ?? (env.NODE_ENV === 'production' ? 'prod' : 'dev');
  if (!['dev', 'prod'].includes(value)) throw new Error('Environment must be dev or prod');
  if (env.NODE_ENV === 'production' && value !== 'prod') {
    throw new Error('Production process cannot use the dev database');
  }
  return value;
}

export function validateDatasource(value) {
  if (value.dbType !== 'postgresql') throw new Error('Datasource must use PostgreSQL');
  for (const key of ['hostIp', 'databaseName', 'username', 'password']) {
    if (typeof value[key] !== 'string' || !value[key].trim()) throw new Error(`Missing datasource field: ${key}`);
  }
  for (const [key, fallback] of Object.entries({ port: 5432, poolMax: 20, poolIdleMs: 30000, poolAcquireTimeoutMs: 5000 })) {
    value[key] ??= fallback;
    if (!Number.isInteger(value[key]) || value[key] <= 0 || (key === 'port' && value[key] > 65535)) {
      throw new Error(`Invalid datasource field: ${key}`);
    }
  }
  if (typeof value.tlsEnabled !== 'boolean') throw new Error('tlsEnabled must be boolean');
  return {
    host: value.hostIp, port: value.port, database: value.databaseName,
    user: value.username, password: value.password, max: value.poolMax,
    idleTimeoutMillis: value.poolIdleMs, connectionTimeoutMillis: value.poolAcquireTimeoutMs,
    query_timeout: 10000, statement_timeout: 10000,
    ssl: value.tlsEnabled ? { rejectUnauthorized: true } : false,
    application_name: 'wisdomcare',
  };
}

export function loadDatabaseConfig(environment, directory = configRoot) {
  if (!['dev', 'prod'].includes(environment)) throw new Error('Environment must be dev or prod');
  // 缺少配置或连接失败时不回退到另一个环境。
  const file = resolve(directory, environment, 'db.datasource.qmdj.yaml');
  let value;
  try { value = readValueScalars(readFileSync(file, 'utf8')); }
  catch { throw new Error(`Cannot load ${environment} datasource config: ${file}`); }
  return validateDatasource(value);
}

/** 与 qmdj/lib/config.ts 一样，只读取 value 下的顶层标量。 */
export function readValueScalars(yaml) {
  const lines = yaml.split(/\r?\n/);
  const start = lines.findIndex(line => /^value:\s*$/.test(line));
  if (start < 0) throw new Error('Datasource config is missing value block');
  const values = {};
  for (const line of lines.slice(start + 1)) {
    if (/^\S/.test(line)) break;
    const match = line.match(/^\s{2}([A-Za-z0-9_]+):\s*(.*?)\s*$/);
    if (!match || !match[2]) continue;
    let scalar = match[2].trim();
    if ((scalar.startsWith('"') && scalar.endsWith('"')) ||
        (scalar.startsWith("'") && scalar.endsWith("'"))) scalar = scalar.slice(1, -1);
    if (['port', 'poolMax', 'poolIdleMs', 'poolAcquireTimeoutMs'].includes(match[1])) scalar = Number(scalar);
    if (match[1] === 'tlsEnabled') {
      if (!['true', 'false'].includes(scalar)) throw new Error('Invalid tlsEnabled');
      scalar = scalar === 'true';
    }
    values[match[1]] = scalar;
  }
  return values;
}
