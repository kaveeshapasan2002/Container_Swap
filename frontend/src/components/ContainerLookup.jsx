import { useState } from 'react';
import { getContainer } from '../services/api';

export default function ContainerLookup() {
  const [searchId, setSearchId] = useState('');
  const [container, setContainer] = useState(null);
  const [notFound, setNotFound] = useState(false);
  const [swapEvent, setSwapEvent] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleSearch = async (e) => {
    e.preventDefault();
    if (!searchId.trim()) return;

    setContainer(null);
    setNotFound(false);
    setSwapEvent(null);
    setLoading(true);

    try {
      const result = await getContainer(searchId.trim());
      if (result) {
        setContainer(result);
        setSwapEvent('Container retrieved — check Dashboard for swap activity!');
      } else {
        setNotFound(true);
      }
    } catch (err) {
      setNotFound(true);
    } finally {
      setLoading(false);
    }
  };

  const getTypeColor = (type) => {
    const colors = { DRY: '#3b82f6', REEFER: '#06b6d4', TANK: '#f59e0b', FLAT: '#8b5cf6' };
    return colors[type] || '#6b7280';
  };

  const getStatusColor = (status) => {
    const colors = { YARD: '#10b981', GATE_IN: '#3b82f6', GATE_OUT: '#f59e0b', ON_VESSEL: '#8b5cf6' };
    return colors[status] || '#6b7280';
  };

  return (
    <div className="card">
      <h2>Container Lookup</h2>
      <p className="hint-text">Search triggers swap-in from disk if container was evicted</p>

      <form onSubmit={handleSearch} className="search-form">
        <input
          type="text"
          value={searchId}
          onChange={(e) => setSearchId(e.target.value)}
          placeholder="Enter Container ID (e.g. ZPMC-2024-00001)"
          className="search-input"
        />
        <button className="btn btn-primary" type="submit" disabled={loading}>
          {loading ? 'Searching...' : 'Search'}
        </button>
      </form>

      {swapEvent && <div className="alert alert-info">{swapEvent}</div>}

      {notFound && (
        <div className="alert alert-error">
          Container "{searchId}" not found in RAM or swap.
        </div>
      )}

      {container && (
        <div className="container-detail">
          <div className="detail-header">
            <h3>{container.containerId}</h3>
            <div className="detail-badges">
              <span className="badge" style={{ background: getTypeColor(container.type) }}>
                {container.type}
              </span>
              <span className="badge" style={{ background: getStatusColor(container.status) }}>
                {container.status}
              </span>
            </div>
          </div>
          <div className="detail-grid">
            <div className="detail-item">
              <span className="detail-label">Yard Location</span>
              <span className="detail-value">{container.yardLocation || '—'}</span>
            </div>
            <div className="detail-item">
              <span className="detail-label">Vessel</span>
              <span className="detail-value">{container.vesselName || '—'}</span>
            </div>
            <div className="detail-item">
              <span className="detail-label">Weight</span>
              <span className="detail-value">{container.weightTons} tons</span>
            </div>
            <div className="detail-item">
              <span className="detail-label">Last Accessed</span>
              <span className="detail-value">
                {container.lastAccessed ? new Date(container.lastAccessed).toLocaleString() : '—'}
              </span>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
