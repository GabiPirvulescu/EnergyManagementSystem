import React, { useState } from 'react';
import apiClient from '../api'; 
import styles from './Form.module.css';

const UserForm = ({ onSuccess, onClose }) => {
  
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [name, setName] = useState('');
  const [address, setAddress] = useState('');
  const [age, setAge] = useState(18);
  const [role, setRole] = useState('CLIENT');
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    setIsLoading(true);

    try {
      const authResponse = await apiClient.post('/auth/register', { 
        username, 
        password, 
        role 
      });

      const newUserId = authResponse.data.id; 
      if (!newUserId) {
          throw new Error("Auth service did not return an ID.");
      }

      await apiClient.post('/users', { 
        id: newUserId,
        username: username, 
        password: password, 
        name: name, 
        address: address, 
        age: age 
      });

      onSuccess();
      onClose(); 
    
    } catch (err) {
      const errorMessage = (err.response && err.response.data && err.response.data.message)
        ? err.response.data.message
        : "Error creating user.";
      setError(errorMessage);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className={styles.form}>
      <div style={{ marginBottom: '10px' }}>
        <input 
          type="text" 
          placeholder="Username (for login)" 
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          required 
          style={{ width: '90%', padding: '8px' }}
        />
      </div>
      <div style={{ marginBottom: '10px' }}>
        <input 
          type="password" 
          placeholder="Password (min. 8 characters)" 
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
          style={{ width: '90%', padding: '8px' }}
        />
      </div>
      <div style={{ marginBottom: '10px' }}>
        <input 
          type="text" 
          placeholder="Full Name" 
          value={name}
          onChange={(e) => setName(e.target.value)}
          required
          style={{ width: '90%', padding: '8px' }}
        />
      </div>
      <div style={{ marginBottom: '10px' }}>
        <input 
          type="text" 
          placeholder="Address" 
          value={address}
          onChange={(e) => setAddress(e.target.value)}
          required
          style={{ width: '90%', padding: '8px' }}
        />
      </div>
       <div style={{ marginBottom: '10px' }}>
        <input 
          type="number" 
          placeholder="Age" 
          value={age}
          onChange={(e) => setAge(parseInt(e.target.value, 10))}
          required
          min="18"
          style={{ width: '90%', padding: '8px' }}
        />
      </div>
      <div style={{ marginBottom: '10px' }}>
        <label style={{ color: 'white', marginRight: '10px' }}>Role:</label>
        <select value={role} onChange={(e) => setRole(e.target.value)} style={{ width: '60%', padding: '8px' }}>
          <option value="CLIENT">Client</option>
          <option value="ADMINISTRATOR">Administrator</option>
        </select>
      </div>
      
      <button type="submit" disabled={isLoading}>
        {isLoading ? 'Saving...' : 'Save User'}
      </button>
      
      {error && <p className={styles.formError}>{error}</p>}
    </form>
  );
};

export default UserForm;