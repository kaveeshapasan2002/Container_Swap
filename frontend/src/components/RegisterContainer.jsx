import { useState } from 'react';
import { registerContainer } from '../services/api';

const TYPES = ['DRY', 'REEFER', 'TANK', 'FLAT'];
const STATUSES = ['YARD', 'GATE_IN', 'GATE_OUT', 'ON_VESSEL'];

export default function RegisterContainer() {
  const [form, setForm] = useState({
    containerId: '',
    type: 'DRY',
    status: 'GATE_IN',
    yardLocation: '',
    vesselName: '',
    weightTons: '',
  });
  const [message, setMessage] = useState(null);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage(null);
    setError(null);

    if (!form.containerId.trim()) {
      setError('Container ID is required');
      return;
    }

    try {
      setLoading(true);
      const payload = {
        ...form,
        weightTons: parseFloat(form.weightTons) || 0,
      };
      const result = await registerContainer(payload);
      setMessage(result);
      // Reset form
      setForm({
        containerId: '',
        type: 'DRY',
        status: 'GATE_IN',
        yardLocation: '',
        vesselName: '',
        weightTons: '',
      });
    } catch (err) {
      setError('Failed to register container');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="card">
      <h2>Register Container</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-grid">
          <div className="form-group">
            <label>Container ID *</label>
            <input
              type="text"
              name="containerId"
              value={form.containerId}
              onChange={handleChange}
              placeholder="ZPMC-2024-00100"
            />
          </div>

          <div className="form-group">
            <label>Type</label>
            <select name="type" value={form.type} onChange={handleChange}>
              {TYPES.map((t) => (
                <option key={t} value={t}>{t}</option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label>Status</label>
            <select name="status" value={form.status} onChange={handleChange}>
              {STATUSES.map((s) => (
                <option key={s} value={s}>{s}</option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label>Yard Location</label>
            <input
              type="text"
              name="yardLocation"
              value={form.yardLocation}
              onChange={handleChange}
              placeholder="Block-A, Row-3, Tier-2"
            />
          </div>

          <div className="form-group">
            <label>Vessel Name</label>
            <input
              type="text"
              name="vesselName"
              value={form.vesselName}
              onChange={handleChange}
              placeholder="MV Pacific Star"
            />
          </div>

          <div className="form-group">
            <label>Weight (tons)</label>
            <input
              type="number"
              name="weightTons"
              value={form.weightTons}
              onChange={handleChange}
              placeholder="25.5"
              step="0.1"
            />
          </div>
        </div>

        <button className="btn btn-primary" type="submit" disabled={loading}>
          {loading ? 'Registering...' : 'Register Container'}
        </button>
      </form>

      {message && <div className="alert alert-success">{message}</div>}
      {error && <div className="alert alert-error">{error}</div>}
    </div>
  );
}
