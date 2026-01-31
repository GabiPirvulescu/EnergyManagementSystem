import React, { useState, useEffect } from 'react';
import apiClient from '../api';
import { getUserDataFromToken } from '../auth'; 
import Modal from './Modal';
import UserForm from './UserForm';
import DeviceForm from './DeviceForm';
import AssignDeviceForm from './AssignDeviceForm'; 
import Chat from './Chat'; 

const AdminDashboard = () => {
  const [users, setUsers] = useState([]);
  const [devices, setDevices] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  
  const [selectedUser, setSelectedUser] = useState(null);

  const [modalType, setModalType] = useState(null); 
  const [selectedItem, setSelectedItem] = useState(null);

  const currentUser = getUserDataFromToken(); 

  const getUsernameById = (userId) => {
    if (!userId) return '---';
    const user = users.find(u => u.id === userId);
    return user ? user.username : 'Unknown User';
  };

  const fetchData = async () => {
    setLoading(true);
    setError(null);
    try {
      const [usersResponse, devicesResponse] = await Promise.all([
        apiClient.get('/users'),
        apiClient.get('/devices')
      ]);
      setUsers(usersResponse.data);
      setDevices(devicesResponse.data);
    } catch (error) {
      setError("No available data. Make sure you are logged in as admin.");
      console.error(error);
    }
    setLoading(false);
  };

  useEffect(() => {
    fetchData();
  }, []);

  const closeModal = () => {
    setModalType(null);
    setSelectedItem(null);
  };

  const handleSuccess = () => {
    closeModal();
    fetchData();
  };

  const handleDeleteUser = async (userId) => {
    if (window.confirm("Are you sure you want to delete this user?")) {
      try {
        await apiClient.delete(`/users/${userId}`);
        fetchData();
      } catch (error) {
        setError("Error when deleting user.");
        console.error(error);
      }
    }
  };

  const handleEditDevice = (device) => {
    setSelectedItem(device);
    setModalType('editDevice');
  };

  const handleDeleteDevice = async (deviceId) => {
    if (window.confirm("Are you sure you want to delete this device?")) {
      try {
        await apiClient.delete(`/devices/${deviceId}`);
        fetchData();
      } catch (error) {
        setError("Error when deleting device.");
        console.error(error);
      }
    }
  };

  const handleAssignDevice = (device) => {
    setSelectedItem(device); 
    setModalType('assignDevice'); 
  };

  if (loading) return <p style={{ textAlign: 'center' }}>Loading data...</p>;
  if (error) return <p style={{ color: 'red', textAlign: 'center' }}>{error}</p>;

  return (
    <div style={{ display: 'flex', justifyContent: 'space-around', width: '90%', margin: '0 auto', gap: '20px' }}>
      
      {/* Modals */}
      <Modal isOpen={modalType === 'createUser'} onClose={closeModal} title="Add New User">
        <UserForm onSuccess={handleSuccess} onClose={closeModal} />
      </Modal>
      
      <Modal isOpen={modalType === 'createDevice'} onClose={closeModal} title="Add New Device">
        <DeviceForm onSuccess={handleSuccess} onClose={closeModal} />
      </Modal>

      <Modal isOpen={modalType === 'editDevice'} onClose={closeModal} title="Edit Device">
        <DeviceForm onSuccess={handleSuccess} onClose={closeModal} existingDevice={selectedItem} />
      </Modal>

      <Modal isOpen={modalType === 'assignDevice'} onClose={closeModal} title="Assign Device">
        <AssignDeviceForm 
          onSuccess={handleSuccess} 
          onClose={closeModal} 
          device={selectedItem}
          users={users} 
        />
      </Modal>

      {}
      <div style={{ flex: 1 }}>
        <h2>User Management</h2>
        <button onClick={() => setModalType('createUser')}>Add New User</button>
        <table className="dashboard-table">
          <thead>
            <tr>
              <th>Username</th>
              <th>Role</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {users.map(user => (
              <tr key={user.id}>
                <td>{user.username}</td>
                <td>{user.role}</td>
                <td>
                  <button onClick={() => handleDeleteUser(user.id)} className="danger">
                    Delete
                  </button>
                  {}
                  <button 
                    onClick={() => setSelectedUser(user)} 
                    style={{ marginLeft: '10px', backgroundColor: '#007bff', color: 'white', border: 'none', padding: '5px 10px', cursor: 'pointer' }}
                  >
                    💬 Chat
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {}
      <div style={{ flex: 1 }}>
        <h2>Device Management</h2>
        <button onClick={() => setModalType('createDevice')}>Add New Device</button>
        <table className="dashboard-table">
          <thead>
            <tr>
              <th>Name</th>
              <th>Assigned To</th>
              <th>Max Consumption</th>
              <th>Description</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {devices.map(device => (
              <tr key={device.id}>
                <td>{device.name}</td>
                <td>{getUsernameById(device.userId)}</td>
                <td>{device.maxHourlyConsumption} kWh</td>
                <td>{device.description || '---'}</td>
                <td>
                  <button onClick={() => handleAssignDevice(device)}>Assign</button>
                  <button onClick={() => handleEditDevice(device)} style={{ margin: '0 5px' }}>Edit</button>
                  <button onClick={() => handleDeleteDevice(device.id)} className="danger">
                    Delete
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {}
      {selectedUser && (
          <Chat currentUser={currentUser} chatPartner={selectedUser} />
      )}

    </div>
  );
};

export default AdminDashboard;