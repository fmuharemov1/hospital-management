import axios from 'axios';

const instance = axios.create({
  // ISPRAVKA: Promijenjen port na 8092 i uklonjen "/client-service"
  baseURL: 'http://localhost:8092/api',
  headers: {
    'Content-Type': 'application/json'
  },
  withCredentials: true // Može ostati, ne smeta za JWT
});

instance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

export default instance;