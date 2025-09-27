import api from './api';

export const orderService = {
  async getAllOrders() {
    const response = await api.get('/api/orders');
    return response.data;
  },

  async getOrderById(id) {
    const response = await api.get(`/api/orders/${id}`);
    return response.data;
  },

  async createOrder(orderData) {
    const response = await api.post('/api/orders', orderData);
    return response.data;
  },

  async updateOrder(id, orderData) {
    const response = await api.put(`/api/orders/${id}`, orderData);
    return response.data;
  }
};
