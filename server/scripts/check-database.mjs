import { resolveEnvironment, loadDatabaseConfig } from '../src/config.mjs';
import { createDatabase } from '../src/database.mjs';

const environment = resolveEnvironment();
const database = createDatabase(loadDatabaseConfig(environment));
try {
  await database.query('SELECT 1 AS connected');
  console.log(`${environment}: database connection OK (read-only check)`);
} catch {
  console.error(`${environment}: database connection failed; check server network and credentials`);
  process.exitCode = 1;
} finally { await database.close(); }
