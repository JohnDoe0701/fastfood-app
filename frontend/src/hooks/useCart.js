import { useMemo } from 'react'
import { useLocalStorageState } from './useLocalStorageState'

export function useCart() {
  const [items, setItems] = useLocalStorageState('akfood-cart', [])

  const total = useMemo(
    () => items.reduce((sum, item) => sum + item.price * item.quantity, 0),
    [items],
  )

  function add(menuItem) {
    setItems((prev) => {
      const existing = prev.find((item) => item.id === menuItem.id)
      if (existing) {
        return prev.map((item) =>
          item.id === menuItem.id ? { ...item, quantity: item.quantity + 1 } : item,
        )
      }
      return [...prev, { id: menuItem.id, name: menuItem.name, price: Number(menuItem.price), quantity: 1 }]
    })
  }

  function increase(id) {
    setItems((prev) => prev.map((item) => (item.id === id ? { ...item, quantity: item.quantity + 1 } : item)))
  }

  function decrease(id) {
    setItems((prev) =>
      prev
        .map((item) => (item.id === id ? { ...item, quantity: item.quantity - 1 } : item))
        .filter((item) => item.quantity > 0),
    )
  }

  function clear() {
    setItems([])
  }

  return { items, total, add, increase, decrease, clear }
}
