module.exports = {
  apps: [{
    name: 'wisdomcare-api',
    cwd: __dirname,
    script: 'src/server.mjs',
    args: '--env=prod',
    instances: 1,
    env: { NODE_ENV: 'production', HOST: '127.0.0.1', PORT: '3100' },
  }],
};
