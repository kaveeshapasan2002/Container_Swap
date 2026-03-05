import { useState } from 'react';
import Dashboard from './components/Dashboard';
import RegisterContainer from './components/RegisterContainer';
import ContainerLookup from './components/ContainerLookup';

export default function App() {
  const [activeTab, setActiveTab] = useState('dashboard');

  return (
    <div className="app">
      {/* Header */}
      <header className="header">
        <div className="header-content">
          <div className="logo">
            <span className="logo-icon">⚓</span>
            <div>
              <h1>ZPMC Container Swap System</h1>
              <p className="subtitle">RAM Cache + Disk Swap | Clean Architecture Demo</p>
            </div>
          </div>
        </div>
      </header>

      {/* Navigation Tabs */}
      <nav className="nav">
        <button
          className={`nav-tab ${activeTab === 'dashboard' ? 'active' : ''}`}
          onClick={() => setActiveTab('dashboard')}
        >
          📊 Dashboard
        </button>
        <button
          className={`nav-tab ${activeTab === 'register' ? 'active' : ''}`}
          onClick={() => setActiveTab('register')}
        >
          📦 Register
        </button>
        <button
          className={`nav-tab ${activeTab === 'lookup' ? 'active' : ''}`}
          onClick={() => setActiveTab('lookup')}
        >
          🔍 Lookup
        </button>
      </nav>

      {/* Content */}
      <main className="main">
        {activeTab === 'dashboard' && <Dashboard />}
        {activeTab === 'register' && <RegisterContainer />}
        {activeTab === 'lookup' && <ContainerLookup />}
      </main>

      {/* Footer */}
      <footer className="footer">
        <p>ZPMC Lanka International — Container Cache System with Swap Demo</p>
      </footer>
    </div>
  );
}
