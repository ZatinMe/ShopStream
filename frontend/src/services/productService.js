import api from './api';

export const productService = {
  async getAllProducts() {
    const response = await api.get('/api/products');
    return response.data;
  },

  async getProductById(id) {
    const response = await api.get(`/api/products/${id}`);
    return response.data;
  },

  async createProduct(productData) {
    const response = await api.post('/api/products', productData);
    return response.data;
  },

  async updateProduct(id, productData) {
    const response = await api.put(`/api/products/${id}`, productData);
    return response.data;
  },

  async deleteProduct(id) {
    const response = await api.delete(`/api/products/${id}`);
    return response.data;
  },

  async searchProducts(params) {
    const response = await api.get('/api/products/search', { params });
    return response.data;
  }
};
