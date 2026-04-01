const ORDER_STATUSES = ['CREATED', 'CONFIRMED', 'PREPARING', 'OUT_FOR_DELIVERY', 'DELIVERED', 'CANCELLED']

function getId(value) {
  if (typeof value === 'object' && value !== null) return value?.id
  return value
}

export function OrdersSection({
  orders = [],
  orderItems = [],
  userType = '',
  onRefresh = () => {},
  onStatusUpdate = () => {},
  onDelete = () => {},
}) {
  const canManageOrders = userType === 'ADMIN' || userType === 'RESTAURANT_MANAGER'
  return (
    <section className="card">
      <div className="section-title-row">
        <h2>Orders</h2>
        <button onClick={onRefresh}>Refresh</button>
      </div>

      {!Array.isArray(orders) || orders.length === 0 ? (
        <p className="empty">No orders yet.</p>
      ) : null}

      <div className="orders-list">
        {Array.isArray(orders) &&
          orders.map((order) => {
            const relatedItems = Array.isArray(orderItems)
              ? orderItems.filter((item) => getId(item.order) === order.id)
              : []
            return (
              <article key={order.id} className="order-card">
                <div className="order-head">
                  <div>
                    <h3>Order #{order.id}</h3>
                    <p>Placed: {order.placedAt || 'N/A'}</p>
                  </div>
                  <strong>INR {Number(order.totalAmount || 0).toFixed(2)}</strong>
                </div>

                <p className="order-meta">Items: {relatedItems.length}</p>

                <div className="actions-row">
                  {canManageOrders && ORDER_STATUSES.map((status) => (
                    <button
                      key={status}
                      className={order.status === status ? 'chip active' : 'chip'}
                      onClick={() => onStatusUpdate(order, status)}
                    >
                      {status}
                    </button>
                  ))}
                  {canManageOrders && (
                    <button className="danger" onClick={() => onDelete(order.id)}>
                      Delete
                    </button>
                  )}
                  {!canManageOrders && (
                    <p style={{ color: '#666', fontSize: '0.9em', margin: 0 }}>
                      Status: <strong>{order.status}</strong>
                    </p>
                  )}
                </div>
              </article>
            )
          })}
      </div>
    </section>
  )
}
