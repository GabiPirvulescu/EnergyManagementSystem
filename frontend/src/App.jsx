import React, { useEffect } from 'react'; 
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { getUserDataFromToken, logout } from './auth';

import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import WebSocketService from './services/WebSocketService';

import Login from './components/Login.jsx'; 
import AdminDashboard from './components/AdminDashboard.jsx';
import Register from './components/Register.jsx';
import ClientDashboard from './components/ClientDashboard.jsx';

import './App.css'; 

const PrivateRoute = ({ children, roleRequired }) => {
  const userData = getUserDataFromToken();
  if (!userData) {
    return <Navigate to="/login" replace />;
  }
  if (roleRequired && userData.role !== roleRequired) {
    return <Navigate to="/unauthorized" replace />;
  }
  return children;
};

const MainDashboard = () => {
  const userData = getUserDataFromToken();
  if (userData?.role === 'ADMINISTRATOR') {
    return <AdminDashboard />;
  }
  if (userData?.role === 'CLIENT') {
    return <ClientDashboard />;
  }
  return <Navigate to="/login" replace />;
};

function App() {
  const userData = getUserDataFromToken();

  useEffect(() => {
    if (userData && userData.userId) { 
      console.log("🚀 Initializing WebSocket:");
      console.log("   User ID:", userData.userId);
      console.log("   Role:", userData.role);

      // Callback for alerts (device consumption)
      const handleAlert = (notification) => {
        console.log("🔔 Alert Received:", notification);
        toast.error(`⚠️ ${notification.message}`, {
            position: "top-right",
            autoClose: 5000,
            hideProgressBar: false,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            theme: "colored",
        });
      };

      // Callback for chat messages (handled by Chat component)
      const handleChatMessage = (chatMsg) => {
        console.log("💬 Chat Message Received in App:", chatMsg);
        // This will be picked up by the Chat component
        // We'll use a custom event to notify all Chat components
        window.dispatchEvent(new CustomEvent('chatMessage', { detail: chatMsg }));
      };

      WebSocketService.connect(
        userData.userId, 
        userData.role, 
        handleAlert,
        handleChatMessage
      );
    }

    return () => {
      WebSocketService.disconnect();
    };
  }, [userData?.userId, userData?.role]); // Add dependencies

  return (
    <div className="App">
      <ToastContainer /> 

      <header className="App-header">
        <h1>Energy Management System</h1>
        {userData && (
          <button 
            onClick={logout} 
            style={{ position: 'absolute', top: '20px', right: '20px', padding: '10px', cursor: 'pointer' }}
          >
            Logout ({userData.username})
          </button>
        )}
      </header>
      
      <BrowserRouter>
        <Routes>
          <Route 
            path="/" 
            element={
              <PrivateRoute>
                <MainDashboard />
              </PrivateRoute>
            } 
          />
          <Route 
            path="/login" 
            element={!userData ? <Login /> : <Navigate to="/" replace />} 
          />
          <Route 
            path="/register" 
            element={!userData ? <Register /> : <Navigate to="/" replace />} 
          />
          
          <Route 
            path="/unauthorized" 
            element={
              <div style={{ padding: '50px', textAlign: 'center' }}>
                <h2>403 - Unauthorized Access</h2>
                <p>You do not have permission to access this page.</p>
                <a href="/">Home</a>
              </div>
            } 
          />
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;