import pg from 'pg';

/** 迁移自 qmdj/lib/db.ts：单例连接池、参数化查询和事务。 */
export function createDatabase(config, Pool = pg.Pool) {
  const pool = new Pool(config);
  pool.on('error', () => console.error('Database pool connection error'));
  return {
    query: async (text, params) => (await pool.query(text, params)).rows,
    async transaction(callback) {
      const client = await pool.connect();
      try {
        await client.query('BEGIN');
        const result = await callback(client);
        await client.query('COMMIT');
        return result;
      } catch (error) {
        try { await client.query('ROLLBACK'); }
        catch { console.error('Database transaction rollback failed'); }
        throw error;
      } finally { client.release(); }
    },
    close: () => pool.end(),
  };
}
