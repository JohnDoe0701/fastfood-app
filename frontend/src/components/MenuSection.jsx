import { useMemo, useState } from 'react'

function getId(value) {
  if (typeof value === 'object' && value !== null) return value?.id
  return value
}

export function MenuSection({ categories = [], menuItems = [], onAddToCart = () => {} }) {
  const [activeCategory, setActiveCategory] = useState('all')

  const filteredItems = useMemo(() => {
    if (!Array.isArray(menuItems) || menuItems.length === 0) return []
    if (activeCategory === 'all') return menuItems
    return menuItems.filter((item) => String(getId(item.category)) === activeCategory)
  }, [activeCategory, menuItems])

  return (
    <section className="card">
      <div className="section-title-row">
        <h2>Menu</h2>
        <div className="chips">
          <button
            className={activeCategory === 'all' ? 'chip active' : 'chip'}
            onClick={() => setActiveCategory('all')}
          >
            All
          </button>
          {Array.isArray(categories) && categories.map((category) => (
            <button
              key={category.id}
              className={activeCategory === String(category.id) ? 'chip active' : 'chip'}
              onClick={() => setActiveCategory(String(category.id))}
            >
              {category.name}
            </button>
          ))}
        </div>
      </div>

      <div className="menu-grid">
        {filteredItems.length === 0 ? (
          <p className="empty" style={{ gridColumn: '1 / -1' }}>No menu items available.</p>
        ) : (
          filteredItems.map((item) => (
            <article key={item.id} className="menu-item">
              <h3>{item.name}</h3>
              <p>{item.description || 'Freshly prepared item.'}</p>
              <div className="menu-item-footer">
                <strong>INR {Number(item.price).toFixed(2)}</strong>
                <button disabled={!item.available} onClick={() => onAddToCart(item)}>
                  {item.available ? 'Add' : 'Unavailable'}
                </button>
              </div>
            </article>
          ))
        )}
      </div>
    </section>
  )
}
