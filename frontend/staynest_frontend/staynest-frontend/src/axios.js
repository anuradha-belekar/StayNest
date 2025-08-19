import axios from 'axios';

const instance = axios.create({
  baseURL: 'http://localhost:9090',
});

instance.interceptors.response.use(
  (response) => response,
  (error) => {
    if (
      error.response?.status === 401 ||
      (error.response?.status === 500 && error.response?.data?.message?.includes('ExpiredJwtException'))
    ) {
      localStorage.removeItem('token');
      window.location.href = '/owner/login';
      alert('Session expired. Please log in again.');
    }
    return Promise.reject(error);
  }
);

export default instance;