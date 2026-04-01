export function CartSection({
  items = [],
  total = 0,
  onIncrease = () => {},
  onDecrease = () => {},
  onCheckout = () => {},
  checkoutBusy = false,
}) {
  return (
    <section className="card">
      <div className="section-title-row">
        <h2>Cart</h2>
        <span>{Array.isArray(items) ? items.length : 0} items</span>
      </div>

      {!Array.isArray(items) || items.length === 0 ? (
        <p className="empty">Your cart is empty.</p>
      ) : null}

      <ul className="cart-list">
        {Array.isArray(items) &&
          items.map((item) => (
            <li key={item.id}>
              <div>
                <h3>{item.name}</h3>
                <p>INR {Number(item.price).toFixed(2)} each</p>
              </div>
              <div className="qty-control">
                <button onClick={() => onDecrease(item.id)}>-</button>
                <span>{item.quantity}</span>
                <button onClick={() => onIncrease(item.id)}>+</button>
              </div>
            </li>
          ))}
      </ul>

      <div className="checkout-row">
        <strong>Total: INR {Number(total).toFixed(2)}</strong>
        <button
          disabled={!Array.isArray(items) || items.length === 0 || checkoutBusy}
          onClick={onCheckout}
        >
          {checkoutBusy ? 'Placing...' : 'Place Order'}
        </button>
      </div>
    </section>
  )
}
