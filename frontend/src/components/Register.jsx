import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import styles from './Form.module.css';
import apiClient from '../api';

const Register = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [name, setName] = useState('');
  const [address, setAddress] = useState('');
  const [age, setAge] = useState(18);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    setSuccess(null);
    setIsLoading(true);

    try {
      const authResponse = await apiClient.post('/auth/register', { 
        username, 
        password, 
        role: "CLIENT" 
      });

      const newUserId = authResponse.data.id;
      if (!newUserId) {
        throw new Error("Server did not return an ID.");
      }

      await apiClient.post('/users', {
        id: newUserId,
        username: username,
        password: password,
        name: name,
        address: address,
        age: age
      });

      setSuccess("Account created! Logging in...");
      
      const loginResponse = await apiClient.post('/auth/login', {
        username,
        password
      });

      localStorage.setItem('authToken', loginResponse.data.token);
      
      setTimeout(() => {
        navigate('/'); 
      }, 1000);

    } catch (err) {
      const errorMessage = (err.response && err.response.data && err.response.data.message)
        ? err.response.data.message
        : "Error when registering. Check the data.";
      setError(errorMessage);
      setIsLoading(false);
    }
  };

  return (
    <div style={{ maxWidth: '400px', margin: '50px auto' }}>
      <form onSubmit={handleSubmit} className={styles.form}>
        <h2 style={{ color: 'white', textAlign: 'center' }}>Înregistrare Cont Nou</h2>
        
        <input type="text" placeholder="Username" value={username} onChange={(e) => setUsername(e.target.value)} required />
        <input type="password" placeholder="Password (min. 8 characters)" value={password} onChange={(e) => setPassword(e.target.value)} required />
        <hr style={{ border: '1px solid #444' }} />
        <input type="text" placeholder="Full Name" value={name} onChange={(e) => setName(e.target.value)} required />
        <input type="text" placeholder="Address" value={address} onChange={(e) => setAddress(e.target.value)} required />
        <input type="number" placeholder="Age (min. 18)" value={age} onChange={(e) => setAge(parseInt(e.target.value, 10))} required min="18" />
        
        <button type="submit" disabled={isLoading}>
          {isLoading ? 'Creating account...' : 'Create Account'}
        </button>
        
        {error && <p className={styles.formError}>{error}</p>}
        {success && <p style={{ color: 'green' }}>{success}</p>}
        
        <p style={{ textAlign: 'center', marginTop: '15px' , color: 'white' }}>
          Already have an account? <Link to="/login" style={{ color: '#61dafb' }}>Login</Link>
        </p>
      </form>
    </div>
  );
};

export default Register;