import api from './api';

export const authService = {
  async login(credentials) {
    const response = await api.post('/api/auth/signin', credentials);
    return response.data;
  },

  async register(userData) {
    const response = await api.post('/api/auth/signup', userData);
    return response.data;
  },

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  },

  getCurrentUser() {
    const user = localStorage.getItem('user');
    return user ? JSON.parse(user) : null;
  },

  isAuthenticated() {
    return !!localStorage.getItem('token');
  }
};
