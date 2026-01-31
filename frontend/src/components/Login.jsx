import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import styles from './Form.module.css';
import apiClient from '../api';

const API_BASE_URL = 'http://localhost'; 

const Login = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState(null);

  const handleLogin = async (e) => {
    e.preventDefault();
    setError(null);

    try {
      const response = await apiClient.post('/auth/login', {
        username,
        password
      });

      localStorage.setItem('authToken', response.data.token);
      window.location.href = '/'; 

    } catch (err) {
      const errorMessage = (err.response && err.response.data && err.response.data.message)
        ? err.response.data.message
        : "Error when logging in. Check if the backend is running or if credentials exist.";
      setError(errorMessage);
    }
  };

  return (

   <div style={{ maxWidth: '400px', margin: '50px auto' }}>
      <form onSubmit={handleLogin} className={styles.form}>
        <h2 style={{ color: 'white', textAlign: 'center' }}>Login</h2>
        <div>
          <input
            type="text"
            placeholder="Username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
        </div>
        <div>
          <input
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
        </div>
        <button type="submit">
          Login
        </button>
        {error && <p className={styles.formError}>{error}</p>}

        {}
        <p style={{ textAlign: 'center', marginTop: '15px' , color: 'white'}}>
          Don't have an account? <Link to="/register" style={{ color: '#61dafb' }}>Register</Link>
        </p>
      </form>
    </div>
  );
};

export default Login;