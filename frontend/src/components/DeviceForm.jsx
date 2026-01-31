import React, { useState, useEffect } from 'react';
import apiClient from '../api';

const DeviceForm = ({ onSuccess, onClose, existingDevice }) => {
  const isEditMode = Boolean(existingDevice);

  const [name, setName] = useState('');
  const [maxHourlyConsumption, setMaxHourlyConsumption] = useState(0);
  const [description, setDescription] = useState('');
  const [error, setError] = useState(null);

  useEffect(() => {
    if (isEditMode && existingDevice) {
      setName(existingDevice.name)
      setMaxHourlyConsumption(existingDevice.maxHourlyConsumption); 
      setDescription(existingDevice.description || '');
    }
  }, [isEditMode, existingDevice]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    
    const deviceData = { name, maxHourlyConsumption, description };

    try {
      if (isEditMode) {
        await apiClient.put(`/devices/${existingDevice.id}`, deviceData);
      } else {
        await apiClient.post('/devices', deviceData);
      }
      onSuccess(); 
      onClose();  
    } catch (err) {
      const errorMessage = (err.response && err.response.data && err.response.data.message)
        ? err.response.data.message
        : "Error saving device.";
      setError(errorMessage);
    }
  };

  return (
    <form onSubmit={handleSubmit} style={{ color: '#333' }}>
      <div style={{ marginBottom: '10px' }}>
        <input 
          type="text" 
          placeholder="Device Name" 
          value={name}
          onChange={(e) => setName(e.target.value)}
          required 
          style={{ width: '90%', padding: '8px' }}
        />
      </div>
      <div style={{ marginBottom: '10px' }}>
        <input 
          type="number" 
          step="0.01"
          placeholder="Max Consumption (kWh)" 
          value={maxHourlyConsumption}
          onChange={(e) => setMaxHourlyConsumption(parseFloat(e.target.value))}
          required
          style={{ width: '90%', padding: '8px' }}
        />
      </div>
      <div style={{ marginBottom: '10px' }}>
        <textarea 
          placeholder="Description (optional)" 
          value={description}
          onChange={(e) => setDescription(e.target.value)}
          style={{ width: '90%', padding: '8px', minHeight: '80px', fontFamily: 'sans-serif' }}
        />
      </div>
      <button type="submit">{isEditMode ? 'Update Device' : 'Create Device'}</button>
      {error && <p style={{ color: 'red' }}>{error}</p>}
    </form>
  );
};

export default DeviceForm;