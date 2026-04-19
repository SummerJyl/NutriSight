// jest.setup.ts
Object.defineProperty(global, 'import', {
  value: {
    meta: {
      env: {
        VITE_USDA_API_KEY: 'test-api-key', // mock your API key here
      },
    },
  },
});
