import { createServer } from 'node:http';

export function createApi(database, environment) {
  return createServer(async (request, response) => {
    response.setHeader('Content-Type', 'application/json; charset=utf-8');
    response.setHeader('Cache-Control', 'no-store');
    if (request.method !== 'GET' || request.url !== '/health/database') {
      response.writeHead(404).end(JSON.stringify({ error: 'not_found' }));
      return;
    }
    try {
      await database.query('SELECT 1 AS connected');
      response.writeHead(200).end(JSON.stringify({ status: 'ok', environment }));
    } catch {
      // 不在 HTTP 响应中暴露数据库主机、用户、密码或原始驱动错误。
      response.writeHead(503).end(JSON.stringify({ status: 'unavailable', environment }));
    }
  });
}
