import React, { useState } from 'react';
import apiClient from '../api';
import styles from './Form.module.css'; 

const AssignDeviceForm = ({ onSuccess, onClose, device, users }) => {
  const [selectedUserId, setSelectedUserId] = useState(device.userId || '');
  const [error, setError] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    
    if (!selectedUserId) {
      setError("Please select an user.");
      return;
    }

    try {
      await apiClient.put(`/devices/${device.id}/assign-user`, { userId: selectedUserId });
      onSuccess();
      onClose();
    } catch (err) {
      const errorMessage = (err.response && err.response.data && err.response.data.message)
        ? err.response.data.message
        : "Error assigning device.";
      setError(errorMessage);
    }
  };

  return (
    <form onSubmit={handleSubmit} className={styles.form}>
      <p style={{ color: 'white' }}>Assign device: <strong>{device.name}</strong></p>
      
      <label style={{ color: 'white' }} htmlFor="user-select">Select a user:</label>
      <select 
        id="user-select"
        value={selectedUserId}
        onChange={(e) => setSelectedUserId(e.target.value)}
      >
        <option value="">-- No user --</option>
        {users.map(user => (
          <option key={user.id} value={user.id}>
            {user.username} (ID: ...{user.id.slice(-6)})
          </option>
        ))}
      </select>

      <button type="submit">Save Assignment</button>
      {error && <p className={styles.formError}>{error}</p>}
    </form>
  );
};

export default AssignDeviceForm;