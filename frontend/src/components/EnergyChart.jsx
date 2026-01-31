import React, { useState, useEffect, useCallback } from 'react';
import DatePicker from 'react-datepicker';
import "react-datepicker/dist/react-datepicker.css";
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';
import apiClient from '../api';

const EnergyChart = ({ deviceId }) => {
  const [selectedDate, setSelectedDate] = useState(new Date());
  const [chartData, setChartData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [errorMsg, setErrorMsg] = useState(null); 

  const fetchConsumptionData = useCallback(async () => {
    if (!deviceId) return; 
    
    setLoading(true);
    setErrorMsg(null);
    
    try {
      const startOfDay = new Date(selectedDate);
      startOfDay.setHours(0, 0, 0, 0);
      const timestamp = startOfDay.getTime();
      
      // FIX: Use proper template literal syntax
      const response = await apiClient.get(`/monitoring/consumption/${deviceId}`, {
        params: { date: timestamp }
      });

      const processedData = response.data.map(item => {
        const dateObj = new Date(item.hour);
        return {
            hour: dateObj.getHours() + ":00",
            kwh: item.consumption
        };
      });
      
      processedData.sort((a, b) => parseInt(a.hour) - parseInt(b.hour));
      
      setChartData(processedData);
    } catch (error) {
      console.error("Eroare la încărcarea datelor graficului:", error);
      
      if (error.response) {
          if (error.response.status === 401) {
             setErrorMsg("Sesiune expirată. Te rog autentifică-te din nou.");
          } else if (error.response.status === 403) {
             setErrorMsg("Nu ai permisiunea să vezi aceste date.");
          } else {
             setErrorMsg(`Eroare server: ${error.response.status}`);
          }
      } else if (error.request) {
          setErrorMsg("Eroare de rețea. Verifică conexiunea cu backend-ul.");
      } else {
          setErrorMsg("Eroare internă aplicație.");
      }
    } finally {
      setLoading(false);
    }
  }, [deviceId, selectedDate]); 

  useEffect(() => {
    if (deviceId) {
      fetchConsumptionData();
    }
  }, [fetchConsumptionData, deviceId]); 

  return (
    <div style={{ color: 'black' }}>
      <div style={{ marginBottom: '20px', textAlign: 'center' }}>
        <label style={{ marginRight: '10px', fontWeight: 'bold', color: 'white' }}>
          Selectează Ziua:
        </label>
        <DatePicker 
          selected={selectedDate} 
          onChange={(date) => setSelectedDate(date)} 
          dateFormat="dd/MM/yyyy"
          className="date-picker-custom"
        />
      </div>

      {errorMsg && (
          <p style={{color: 'red', textAlign: 'center', fontWeight: 'bold'}}>
            {errorMsg}
          </p>
      )}

      {loading ? (
        <p style={{color: 'white', textAlign: 'center'}}>Se încarcă datele...</p>
      ) : !errorMsg && chartData.length === 0 ? (
        <p style={{color: 'white', textAlign: 'center'}}>
          Nu există date pentru această zi.
        </p>
      ) : !errorMsg && (
        <div style={{ height: '300px', width: '100%' }}>
          <ResponsiveContainer width="100%" height="100%">
            <BarChart data={chartData}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="hour" stroke="#ffffff" />
              <YAxis 
                stroke="#ffffff" 
                label={{ 
                  value: 'kWh', 
                  angle: -90, 
                  position: 'insideLeft', 
                  fill: 'white' 
                }} 
              />
              <Tooltip 
                contentStyle={{ 
                  backgroundColor: '#333', 
                  border: 'none', 
                  color: '#fff' 
                }} 
              />
              <Bar dataKey="kwh" fill="#8884d8" name="Energie (kWh)" />
            </BarChart>
          </ResponsiveContainer>
        </div>
      )}
    </div>
  );
};

export default EnergyChart;