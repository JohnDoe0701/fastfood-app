export function ResourceTabs({ resources, activeKey, onChange }) {
  return (
    <nav className="tabs" aria-label="API resources">
      {resources.map((resource) => (
        <button
          key={resource.key}
          className={activeKey === resource.key ? 'tab active' : 'tab'}
          onClick={() => onChange(resource.key)}
        >
          {resource.title}
        </button>
      ))}
    </nav>
  )
}
