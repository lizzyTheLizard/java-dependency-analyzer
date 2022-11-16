// vite.config.js
import { defineConfig } from "file:///home/magraf/Documents/java-dependency-analyzer/frontend/node_modules/vite/dist/node/index.js";
import { viteSingleFile } from "file:///home/magraf/Documents/java-dependency-analyzer/frontend/node_modules/vite-plugin-singlefile/dist/esm/index.js";
import { resolve } from "path";
var __vite_injected_original_dirname = "/home/magraf/Documents/java-dependency-analyzer/frontend";
var vite_config_default = defineConfig({
  plugins: [viteSingleFile()],
  base: "",
  build: {
    chunkSizeWarningLimit: "2000 KiB",
    rollupOptions: {
      external: [
        "src/data.json"
      ]
    }
  },
  resolve: {
    alias: {
      "~bootstrap": resolve(__vite_injected_original_dirname, "node_modules/bootstrap")
    }
  }
});
export {
  vite_config_default as default
};
//# sourceMappingURL=data:application/json;base64,ewogICJ2ZXJzaW9uIjogMywKICAic291cmNlcyI6IFsidml0ZS5jb25maWcuanMiXSwKICAic291cmNlc0NvbnRlbnQiOiBbImNvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9kaXJuYW1lID0gXCIvaG9tZS9tYWdyYWYvRG9jdW1lbnRzL2phdmEtZGVwZW5kZW5jeS1hbmFseXplci9mcm9udGVuZFwiO2NvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9maWxlbmFtZSA9IFwiL2hvbWUvbWFncmFmL0RvY3VtZW50cy9qYXZhLWRlcGVuZGVuY3ktYW5hbHl6ZXIvZnJvbnRlbmQvdml0ZS5jb25maWcuanNcIjtjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfaW1wb3J0X21ldGFfdXJsID0gXCJmaWxlOi8vL2hvbWUvbWFncmFmL0RvY3VtZW50cy9qYXZhLWRlcGVuZGVuY3ktYW5hbHl6ZXIvZnJvbnRlbmQvdml0ZS5jb25maWcuanNcIjsvLyB2aXRlLmNvbmZpZy5qc1xuaW1wb3J0IHsgZGVmaW5lQ29uZmlnIH0gZnJvbSAndml0ZSdcbmltcG9ydCB7IHZpdGVTaW5nbGVGaWxlIH0gZnJvbSBcInZpdGUtcGx1Z2luLXNpbmdsZWZpbGVcIlxuaW1wb3J0IHsgcmVzb2x2ZSB9IGZyb20gJ3BhdGgnXG5cbmV4cG9ydCBkZWZhdWx0IGRlZmluZUNvbmZpZyh7XG4gIHBsdWdpbnM6IFt2aXRlU2luZ2xlRmlsZSgpXSxcbiAgYmFzZTogJycsXG4gIGJ1aWxkOiB7XG4gICAgY2h1bmtTaXplV2FybmluZ0xpbWl0OiAnMjAwMCBLaUInLFxuICAgIHJvbGx1cE9wdGlvbnM6IHtcbiAgICAgIGV4dGVybmFsOiBbXG4gICAgICAgICdzcmMvZGF0YS5qc29uJyxcbiAgICAgIF1cbiAgICB9XG4gIH0sXG4gIHJlc29sdmU6IHtcbiAgICBhbGlhczoge1xuICAgICAgJ35ib290c3RyYXAnOiByZXNvbHZlKF9fZGlybmFtZSwgJ25vZGVfbW9kdWxlcy9ib290c3RyYXAnKSxcbiAgICB9XG4gIH0sXG59KVxuXG5cblxuIl0sCiAgIm1hcHBpbmdzIjogIjtBQUNBLFNBQVMsb0JBQW9CO0FBQzdCLFNBQVMsc0JBQXNCO0FBQy9CLFNBQVMsZUFBZTtBQUh4QixJQUFNLG1DQUFtQztBQUt6QyxJQUFPLHNCQUFRLGFBQWE7QUFBQSxFQUMxQixTQUFTLENBQUMsZUFBZSxDQUFDO0FBQUEsRUFDMUIsTUFBTTtBQUFBLEVBQ04sT0FBTztBQUFBLElBQ0wsdUJBQXVCO0FBQUEsSUFDdkIsZUFBZTtBQUFBLE1BQ2IsVUFBVTtBQUFBLFFBQ1I7QUFBQSxNQUNGO0FBQUEsSUFDRjtBQUFBLEVBQ0Y7QUFBQSxFQUNBLFNBQVM7QUFBQSxJQUNQLE9BQU87QUFBQSxNQUNMLGNBQWMsUUFBUSxrQ0FBVyx3QkFBd0I7QUFBQSxJQUMzRDtBQUFBLEVBQ0Y7QUFDRixDQUFDOyIsCiAgIm5hbWVzIjogW10KfQo=