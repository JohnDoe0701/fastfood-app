export function TopBar() {
  return (
    <header className="topbar">
      <div>
        <h1>AK Food API Console</h1>
        <p>Dark-mode CRUD workspace for all Fastfood backend endpoints.</p>
      </div>
      <div className="topbar-links">
        <a href="/swagger-ui.html" target="_blank" rel="noreferrer">
          Swagger UI
        </a>
        <a href="/v3/api-docs" target="_blank" rel="noreferrer">
          OpenAPI JSON
        </a>
      </div>
    </header>
  )
}
