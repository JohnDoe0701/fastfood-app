export function SiteHeader({ health, userName, onLogout }) {
  return (
    <header className="site-header">
      <div>
        <p className="eyebrow">AKFOOD ONLINE</p>
        <h1>Late-night cravings, delivered fast.</h1>
        <p className="subtitle">
          Browse menu, build your cart, place orders, and manage your profile from one smooth interface.
        </p>
      </div>
      <div style={{ display: 'flex', gap: '15px', alignItems: 'center' }}>
        <div className="status-pill">API Health: {health ?? '...'}</div>
        {userName && (
          <div style={{ display: 'flex', gap: '10px', alignItems: 'center' }}>
            <span style={{ color: '#fff', fontSize: '14px' }}>User: {userName}</span>
            <button
              onClick={onLogout}
              style={{
                padding: '6px 12px',
                backgroundColor: '#d32f2f',
                color: '#fff',
                border: 'none',
                borderRadius: '4px',
                cursor: 'pointer',
                fontSize: '12px',
              }}
            >
              Logout
            </button>
          </div>
        )}
      </div>
    </header>
  )
}
