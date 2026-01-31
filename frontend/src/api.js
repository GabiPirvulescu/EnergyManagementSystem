import axios from 'axios';
import { logout } from './auth'; 

const API_BASE_URL = 'http://localhost'; 

const apiClient = axios.create({
  baseURL: API_BASE_URL,
});

apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('authToken');
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

apiClient.interceptors.response.use(
  (response) => response,
  (error) => {

    if (!error.response) {
        console.error("NO RESPONSE FROM BACKEND → CORS/NETWORK ERROR");
        return Promise.reject(error);
    }

    const { status } = error.response;

    if (status === 401) {
        alert("Session expired. Log in again.");
        logout();
    } else if (status === 403) {
        alert("You lack permission to access this resource.");
    }

    return Promise.reject(error);
  }
);


export default apiClient;