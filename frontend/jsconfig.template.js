module.exports = {
    baseUrl: '.',
    include: ['src/**/*', 'tests/**/*'],
    compilerOptions: {
        baseUrl: '.',
        target: 'esnext',
        module: 'es2015',
        // ...
        // `paths` will be automatically generated using aliases.config.js
        // ...
    },
}