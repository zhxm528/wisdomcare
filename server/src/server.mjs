import { resolveEnvironment, loadDatabaseConfig } from './config.mjs';
import { createDatabase } from './database.mjs';
import { createApi } from './http.mjs';

const environment = resolveEnvironment();
const port = Number(process.env.PORT ?? 3100);
if (!Number.isInteger(port) || port < 1 || port > 65535) throw new Error('Invalid PORT');
const database = createDatabase(loadDatabaseConfig(environment));
const server = createApi(database, environment);
server.requestTimeout = 15000;
server.headersTimeout = 10000;
server.listen(port, process.env.HOST ?? '127.0.0.1', () => {
  console.log(`WisdomCare API listening on port ${port}, environment=${environment}`);
});
for (const signal of ['SIGINT', 'SIGTERM']) {
  process.once(signal, () => {
    const timeout = setTimeout(() => process.exit(1), 15000).unref();
    server.close(async () => {
      await database.close();
      clearTimeout(timeout);
    });
  });
}
