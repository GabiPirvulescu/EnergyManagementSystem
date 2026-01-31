import React, { useState, useEffect } from 'react';
import apiClient from '../api';
import Modal from './Modal';         
import EnergyChart from './EnergyChart'; 
import { getUserDataFromToken } from '../auth'; // Ensure you have this
import Chat from './Chat';

const ClientDashboard = () => {
  const userData = getUserDataFromToken();

  const [myDevices, setMyDevices] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const [isChartOpen, setIsChartOpen] = useState(false);
  const [selectedDevice, setSelectedDevice] = useState(null);

  const fetchMyDevices = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await apiClient.get('/devices');
      setMyDevices(response.data);
    } catch (err) {
      setError("Could not fetch devices. Is the backend running?");
      console.error(err);
    }
    setLoading(false);
  };

  useEffect(() => {
    fetchMyDevices();
  }, []); 

  const handleViewChart = (device) => {
    setSelectedDevice(device);
    setIsChartOpen(true);
  };

  const closeChart = () => {
    setIsChartOpen(false);
    setSelectedDevice(null);
  };

  if (loading) return <p style={{ textAlign: 'center' }}>Loading devices...</p>;
  if (error) return <p style={{ color: 'red', textAlign: 'center' }}>{error}</p>;

  return (
    <div style={{ width: '80%', margin: '0 auto' }}>
      <h2>My Devices</h2>
      
      {/* Energy Chart Modal */}
      <Modal isOpen={isChartOpen} onClose={closeChart} title={`Consumption: ${selectedDevice?.name}`}>
        {selectedDevice && <EnergyChart deviceId={selectedDevice.id} />}
      </Modal>

      {myDevices.length === 0 ? (
        <p style={{ textAlign: 'center' }}>You have no devices assigned at the moment.</p>
      ) : (
        <table className="dashboard-table">
          <thead>
            <tr>
              <th>Name</th>
              <th>Max Consumption</th>
              <th>Description</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {myDevices.map(device => (
              <tr key={device.id}>
                <td>{device.name}</td>
                <td>{device.maxHourlyConsumption} kWh</td>
                <td style={{ maxWidth: '300px', overflow: 'hidden', textOverflow: 'ellipsis' }}>
                  {device.description || '---'}
                </td>
                <td>
                  <button onClick={() => handleViewChart(device)}>
                    View Chart
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
      <Chat currentUser={userData} chatPartner={null} />
    </div>
  );
};

export default ClientDashboard;