import { jwtDecode } from 'jwt-decode';

export const getUserDataFromToken = () => {
  const token = localStorage.getItem('authToken');
  if (!token) {
    return null;
  }
  try {
    const decodedToken = jwtDecode(token);
    if (decodedToken.exp * 1000 < Date.now()) {
      localStorage.removeItem('authToken');
      return null;
    }
    return {
      username: decodedToken.sub,
      role: decodedToken.role,
      userId: decodedToken.userId,
    };
  } catch (error) {
    console.error("Token invalid:", error);
    localStorage.removeItem('authToken');
    return null;
  }
};

export const logout = () => {
  localStorage.removeItem('authToken');
  window.location.href = '/login';
};