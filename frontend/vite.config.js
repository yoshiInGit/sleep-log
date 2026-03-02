import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/api': {
        target: process.env.VITE_API_TARGET || 'http://backend:8080',
        changeOrigin: true,
        secure: false,
      }
    },
    host: true,
    port: 3000,
  }
})
