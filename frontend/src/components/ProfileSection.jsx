import { useState, useEffect } from 'react'

export function ProfileSection({
  profile = { id: null, fullName: '', email: '', phone: '', userType: '', enabled: true },
  onUpdate = () => {},
  onLoad = () => {},
  onDelete = () => {},
  busy = false,
}) {
  const [form, setForm] = useState(profile)

  useEffect(() => {
    setForm(profile)
  }, [profile])

  function change(field, value) {
    setForm((prev) => ({ ...prev, [field]: value }))
  }

  return (
    <section className="card">
      <div className="section-title-row">
        <h2>My Profile</h2>
        <span>{profile?.id ? `User ID: ${profile.id}` : 'Not logged in'}</span>
      </div>

      <div className="form-grid">
        <label>
          Full Name
          <input
            value={form?.fullName || ''}
            onChange={(e) => change('fullName', e.target.value)}
            disabled={!profile?.id}
          />
        </label>
        <label>
          Email
          <input
            value={form?.email || ''}
            onChange={(e) => change('email', e.target.value)}
            disabled={!profile?.id}
            type="email"
          />
        </label>
        <label>
          Phone
          <input
            value={form?.phone || ''}
            onChange={(e) => change('phone', e.target.value)}
            disabled={!profile?.id}
          />
        </label>
        <label>
          User Type
          <input value={profile?.userType || ''} disabled type="text" />
        </label>
      </div>

      {profile?.id && (
        <div style={{ marginTop: '12px', padding: '10px', backgroundColor: '#f5f5f5', borderRadius: '4px' }}>
          <p style={{ margin: '0 0 8px 0', fontSize: '12px', color: '#666' }}>
            <strong>Account Status:</strong> {profile?.enabled ? 'Active' : 'Disabled'}
          </p>
        </div>
      )}

      <div className="actions-row">
        <button disabled={busy || !profile?.id} onClick={() => onUpdate(form)}>
          Update Profile
        </button>
        <button disabled={busy || !profile?.id} onClick={onLoad}>
          Reload Profile
        </button>
        <button className="danger" disabled={busy || !profile?.id} onClick={onDelete}>
          Delete Account
        </button>
      </div>
    </section>
  )
}
