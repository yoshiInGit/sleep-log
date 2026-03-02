import React, { useState } from "react";
import axios from "axios";
import "./App.css";

function App() {
  const [healthStatus, setHealthStatus] = useState("");

  const checkHealth = async () => {
    try {
      const response = await axios.get("/api/health");
      setHealthStatus(JSON.stringify(response.data, null, 2));
    } catch (error) {
      setHealthStatus("Error connecting to backend");
      console.error(error);
    }
  };

  return (
    <div className="app-container">
      <header className="app-header">
        <h1>SleepLog</h1>
        <p>Your journey to better sleep starts here. ！！</p>
        <button onClick={checkHealth} className="primary-button">
          Server Health Check！！
        </button>
        {healthStatus && <pre className="health-status">{healthStatus}</pre>}
      </header>
    </div>
  );
}

export default App;
