import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig(({ command }) => {
  if (command === 'serve') {
    return {
      plugins: [react()],
      server: {
        port: 5500,
        strictPort: true, // fail if port 5500 is in use
      },
      base: '/',
    };
  } else {
    return {
      plugins: [react()],
      base: '/Data-Explore/',
    };
  }
});
