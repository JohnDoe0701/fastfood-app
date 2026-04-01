import { useState } from 'react'

export function AuthSection({ onRegisterSuccess, onLoginSuccess, busy }) {
  const [isLogin, setIsLogin] = useState(true)
  const [error, setError] = useState('')
  const [form, setForm] = useState({
    email: '',
    password: '',
    confirmPassword: '',
    fullName: '',
    phone: '',
  })

  function change(field, value) {
    setForm((prev) => ({ ...prev, [field]: value }))
    setError('')
  }

  function handleRegisterClick() {
    setError('')
    
    if (!form.fullName.trim()) {
      setError('Full name is required')
      return
    }
    if (!form.email.trim()) {
      setError('Email is required')
      return
    }
    if (!form.email.includes('@')) {
      setError('Please enter a valid email address')
      return
    }
    if (!form.phone.trim()) {
      setError('Phone number is required')
      return
    }
    if (!form.password) {
      setError('Password is required')
      return
    }
    if (form.password.length < 6) {
      setError('Password must be at least 6 characters')
      return
    }
    if (!form.confirmPassword) {
      setError('Please confirm your password')
      return
    }
    if (form.password !== form.confirmPassword) {
      setError('Passwords do not match')
      return
    }

    console.log('[AuthSection] Submitting registration form')
    onRegisterSuccess({
      fullName: form.fullName,
      email: form.email,
      phone: form.phone,
      password: form.password,
      confirmPassword: form.confirmPassword,
      userType: 'CUSTOMER',
    })
    setForm({ email: '', password: '', confirmPassword: '', fullName: '', phone: '' })
  }

  function handleLoginClick() {
    setError('')
    
    if (!form.email.trim()) {
      setError('Email is required')
      return
    }
    if (!form.email.includes('@')) {
      setError('Please enter a valid email address')
      return
    }
    if (!form.password) {
      setError('Password is required')
      return
    }

    console.log('[AuthSection] Submitting login form')
    onLoginSuccess({
      email: form.email,
      password: form.password,
    })
    setForm({ email: '', password: '', confirmPassword: '', fullName: '', phone: '' })
  }

  return (
    <section className="card" style={{ maxWidth: '500px', margin: '0 auto' }}>
      <div className="section-title-row">
        <h2>{isLogin ? 'Login' : 'Create Account'}</h2>
        <a
          href="#"
          onClick={(e) => {
            e.preventDefault()
            setIsLogin(!isLogin)
            setError('')
            setForm({ email: '', password: '', confirmPassword: '', fullName: '', phone: '' })
          }}
          className="link"
          style={{ color: '#36d2a7', cursor: 'pointer', fontSize: '14px' }}
        >
          {isLogin ? 'Need an account? Register' : 'Already have an account? Login'}
        </a>
      </div>

      {error && (
        <div
          style={{
            padding: '12px',
            borderRadius: '8px',
            backgroundColor: '#ffebee',
            border: '1px solid #ef5350',
            color: '#c62828',
            marginBottom: '16px',
            fontSize: '14px',
          }}
        >
          {error}
        </div>
      )}

      {!isLogin && (
        <>
          <div className="form-grid">
            <label>
              Full Name
              <input
                disabled={busy}
                value={form.fullName}
                onChange={(e) => change('fullName', e.target.value)}
                placeholder="John Doe"
                style={{
                  padding: '8px 12px',
                  border: '1px solid var(--line)',
                  borderRadius: '8px',
                  backgroundColor: 'var(--bg-soft)',
                  color: 'var(--text)',
                  fontSize: '14px',
                }}
              />
            </label>
            <label>
              Phone
              <input
                disabled={busy}
                value={form.phone}
                onChange={(e) => change('phone', e.target.value)}
                placeholder="1234567890"
                style={{
                  padding: '8px 12px',
                  border: '1px solid var(--line)',
                  borderRadius: '8px',
                  backgroundColor: 'var(--bg-soft)',
                  color: 'var(--text)',
                  fontSize: '14px',
                }}
              />
            </label>
          </div>
        </>
      )}

      <div className="form-grid">
        <label>
          Email
          <input
            disabled={busy}
            value={form.email}
            onChange={(e) => change('email', e.target.value)}
            placeholder="user@example.com"
            type="email"
            style={{
              padding: '8px 12px',
              border: '1px solid var(--line)',
              borderRadius: '8px',
              backgroundColor: 'var(--bg-soft)',
              color: 'var(--text)',
              fontSize: '14px',
            }}
          />
        </label>
        <label>
          Password
          <input
            disabled={busy}
            value={form.password}
            onChange={(e) => change('password', e.target.value)}
            placeholder="••••••••"
            type="password"
            style={{
              padding: '8px 12px',
              border: '1px solid var(--line)',
              borderRadius: '8px',
              backgroundColor: 'var(--bg-soft)',
              color: 'var(--text)',
              fontSize: '14px',
            }}
          />
        </label>
      </div>

      {!isLogin && (
        <div className="form-grid">
          <label>
            Confirm Password
            <input
              disabled={busy}
              value={form.confirmPassword}
              onChange={(e) => change('confirmPassword', e.target.value)}
              placeholder="••••••••"
              type="password"
              style={{
                padding: '8px 12px',
                border: '1px solid var(--line)',
                borderRadius: '8px',
                backgroundColor: 'var(--bg-soft)',
                color: 'var(--text)',
                fontSize: '14px',
              }}
            />
          </label>
        </div>
      )}

      <div className="actions-row">
        {isLogin ? (
          <button
            disabled={busy}
            onClick={handleLoginClick}
            style={{
              padding: '10px 20px',
              backgroundColor: '#36d2a7',
              color: '#fff',
              border: 'none',
              borderRadius: '8px',
              cursor: busy ? 'not-allowed' : 'pointer',
              opacity: busy ? 0.6 : 1,
              fontSize: '14px',
              fontWeight: '600',
            }}
          >
            {busy ? 'Signing in...' : 'Sign In'}
          </button>
        ) : (
          <button
            disabled={busy}
            onClick={handleRegisterClick}
            style={{
              padding: '10px 20px',
              backgroundColor: '#36d2a7',
              color: '#fff',
              border: 'none',
              borderRadius: '8px',
              cursor: busy ? 'not-allowed' : 'pointer',
              opacity: busy ? 0.6 : 1,
              fontSize: '14px',
              fontWeight: '600',
            }}
          >
            {busy ? 'Creating Account...' : 'Create Account'}
          </button>
        )}
      </div>
    </section>
  )
}
