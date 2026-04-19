module.exports = {
  preset: 'ts-jest/presets/js-with-babel',
  testEnvironment: 'jsdom',
  globals: {
    'ts-jest': {
      tsconfig: 'tsconfig.json',
      babelConfig: './babel.config.js',
    },
  },
  setupFilesAfterEnv: ['<rootDir>/jest.setup.ts'],
};
