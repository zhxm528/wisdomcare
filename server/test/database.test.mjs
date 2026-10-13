import test from 'node:test';
import assert from 'node:assert/strict';
import { createDatabase } from '../src/database.mjs';
import { resolveEnvironment, loadDatabaseConfig, validateDatasource, readValueScalars } from '../src/config.mjs';
import { createApi } from '../src/http.mjs';
import { mkdtempSync, mkdirSync, writeFileSync, rmSync } from 'node:fs';
import { tmpdir } from 'node:os';
import { join } from 'node:path';

test('environment defaults, explicit selection and production guard', () => {
  assert.equal(resolveEnvironment([], {}), 'dev');
  assert.equal(resolveEnvironment([], { NODE_ENV: 'production' }), 'prod');
  assert.equal(resolveEnvironment(['--env=prod'], {}), 'prod');
  assert.throws(() => resolveEnvironment(['--env=dev'], { NODE_ENV: 'production' }));
  assert.throws(() => resolveEnvironment(['--env=typo'], {}));
});

const sample = () => ({ dbType: 'postgresql', hostIp: 'localhost', databaseName: 'test', username: 'test', password: 'secret', tlsEnabled: false });
const yaml = data => 'configKey: db.datasource.qmdj\nvalue:\n' + Object.entries(data).map(([key, value]) => `  ${key}: ${value}`).join('\n');
test('reads qmdj YAML value block, quoted credentials and ignores nested metadata', () => {
  const data = readValueScalars(yaml({ ...sample(), password: '"a:#b"' }) + '\n  tags:\n    - dev\nmeta:\n  hostIp: wrong');
  assert.equal(data.password, 'a:#b');
  assert.equal(data.hostIp, 'localhost');
  assert.equal(data.tlsEnabled, false);
  assert.throws(() => readValueScalars('hostIp: localhost'));
});
test('datasource validation and verified TLS', () => {
  assert.equal(validateDatasource(sample()).port, 5432);
  assert.deepEqual(validateDatasource({ ...sample(), tlsEnabled: true }).ssl, { rejectUnauthorized: true });
  assert.throws(() => validateDatasource({ ...sample(), password: '' }));
  assert.throws(() => validateDatasource({ ...sample(), port: 0 }));
});

test('selects separate datasource files; missing prod never falls back to dev', () => {
  const root = mkdtempSync(join(tmpdir(), 'wisdomcare-test-'));
  try {
    mkdirSync(join(root, 'dev'));
    writeFileSync(join(root, 'dev/db.datasource.qmdj.yaml'), yaml(sample()));
    assert.equal(loadDatabaseConfig('dev', root).host, 'localhost');
    assert.throws(() => loadDatabaseConfig('prod', root));
    mkdirSync(join(root, 'prod'));
    writeFileSync(join(root, 'prod/db.datasource.qmdj.yaml'), yaml({ ...sample(), hostIp: 'prod-host' }));
    assert.equal(loadDatabaseConfig('prod', root).host, 'prod-host');
  } finally { rmSync(root, { recursive: true, force: true }); }
});

for (const fails of [false, true]) {
  test(`transaction ${fails ? 'rollback' : 'commit'} releases client`, async () => {
    const calls = [];
    class FakePool {
      on() {}
      async connect() { return { query: async sql => calls.push(sql), release: () => calls.push('release') }; }
    }
    const database = createDatabase({}, FakePool);
    const error = new Error('business error');
    const result = database.transaction(async () => { if (fails) throw error; return 42; });
    if (fails) await assert.rejects(result, caught => caught === error);
    else assert.equal(await result, 42);
    assert.deepEqual(calls, ['BEGIN', fails ? 'ROLLBACK' : 'COMMIT', 'release']);
  });
}

test('HTTP checks DB read-only, reports environment and conceals failures', async () => {
  let fail = false;
  const api = createApi({ query: async sql => {
    assert.equal(sql, 'SELECT 1 AS connected');
    if (fail) throw new Error('password=secret');
  } }, 'dev');
  await new Promise(resolve => api.listen(0, '127.0.0.1', resolve));
  const url = `http://127.0.0.1:${api.address().port}`;
  try {
    const ok = await fetch(`${url}/health/database`);
    assert.equal(ok.status, 200);
    assert.deepEqual(await ok.json(), { status: 'ok', environment: 'dev' });
    fail = true;
    const unavailable = await fetch(`${url}/health/database`);
    assert.equal(unavailable.status, 503);
    assert.deepEqual(await unavailable.json(), { status: 'unavailable', environment: 'dev' });
    assert.equal((await fetch(`${url}/query`, { method: 'POST' })).status, 404);
  } finally { await new Promise(resolve => api.close(resolve)); }
});
