// vite.config.js
import { defineConfig } from 'vite';
import { viteSingleFile } from 'vite-plugin-singlefile';
import { resolve } from 'path';

export default defineConfig({
    plugins: [viteSingleFile()],
    base: '',
    build: {
        chunkSizeWarningLimit: '2000 KiB',
        rollupOptions: {
            external: [
                'src/data.json',
            ]
        }
    },
    resolve: {
        alias: {
            '~bootstrap': resolve(__dirname, 'node_modules/bootstrap'),
        }
    },
});



