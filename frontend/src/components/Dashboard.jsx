import { useState, useEffect } from 'react';
import { getStatus } from '../services/api';

export default function Dashboard() {
  const [status, setStatus] = useState(null);
  const [loading, setLoading] = useState(true);

  const fetchStatus = async () => {
    try {
      setLoading(true);
      const data = await getStatus();
      setStatus(data);
    } catch (err) {
      console.error('Failed to fetch status:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchStatus();
    const interval = setInterval(fetchStatus, 3000); // auto-refresh every 3s
    return () => clearInterval(interval);
  }, []);

  if (loading && !status) return <div className="card">Loading...</div>;
  if (!status) return <div className="card">Failed to load status</div>;

  const ramPercent = Math.round((status.ramUsed / status.ramCapacity) * 100);

  return (
    <div className="card">
      <h2>System Status</h2>

      {/* RAM Usage Bar */}
      <div className="status-section">
        <div className="status-header">
          <span className="label">RAM Cache</span>
          <span className="value">{status.ramUsed} / {status.ramCapacity}</span>
        </div>
        <div className="progress-bar">
          <div
            className={`progress-fill ${ramPercent >= 100 ? 'full' : ramPercent >= 80 ? 'warning' : ''}`}
            style={{ width: `${ramPercent}%` }}
          />
        </div>
      </div>

      {/* Swap Usage */}
      <div className="status-section">
        <div className="status-header">
          <span className="label">Disk Swap</span>
          <span className="value swap-value">{status.swapUsed} containers</span>
        </div>
      </div>

      {/* Containers in RAM */}
      <div className="status-section">
        <h3>Containers in RAM</h3>
        {status.containersInRam.length === 0 ? (
          <p className="empty-text">No containers in RAM</p>
        ) : (
          <div className="container-chips">
            {status.containersInRam.map((id) => (
              <span key={id} className="chip ram-chip">{id}</span>
            ))}
          </div>
        )}
      </div>

      <button className="btn btn-secondary" onClick={fetchStatus}>
        Refresh
      </button>
    </div>
  );
}
