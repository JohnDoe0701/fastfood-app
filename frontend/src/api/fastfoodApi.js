import { request } from './http'

export const fastfoodApi = {
  // Health
  getHealth: () => {
    console.log('[fastfoodApi] getHealth')
    return request('/api/health')
  },

  // Authentication
  register: (payload) => {
    console.log('[fastfoodApi] register:', payload.email)
    return request('/api/auth/register', {
      method: 'POST',
      body: JSON.stringify(payload),
    })
  },
  login: (payload) => {
    console.log('[fastfoodApi] login:', payload.email)
    return request('/api/auth/login', {
      method: 'POST',
      body: JSON.stringify(payload),
    })
  },

  // Categories and Menu
  getCategories: () => {
    console.log('[fastfoodApi] getCategories')
    return request('/api/categories')
  },
  getMenuItems: () => {
    console.log('[fastfoodApi] getMenuItems')
    return request('/api/menu-items')
  },
  getMenuItem: (id) => {
    console.log('[fastfoodApi] getMenuItem:', id)
    return request(`/api/menu-items/${id}`)
  },

  // Users
  getAllUsers: () => {
    console.log('[fastfoodApi] getAllUsers')
    return request('/api/users')
  },
  getUser: (id) => {
    console.log('[fastfoodApi] getUser:', id)
    return request(`/api/users/${id}`)
  },
  updateUser: (id, payload) => {
    console.log('[fastfoodApi] updateUser:', id)
    return request(`/api/users/${id}`, {
      method: 'PUT',
      body: JSON.stringify(payload),
    })
  },
  deleteUser: (id) => {
    console.log('[fastfoodApi] deleteUser:', id)
    return request(`/api/users/${id}`, {
      method: 'DELETE',
    })
  },
  enableUser: (id) => {
    console.log('[fastfoodApi] enableUser:', id)
    return request(`/api/users/${id}/enable`, {
      method: 'PUT',
    })
  },
  disableUser: (id) => {
    console.log('[fastfoodApi] disableUser:', id)
    return request(`/api/users/${id}/disable`, {
      method: 'PUT',
    })
  },

  // Orders
  createOrder: (payload) => {
    console.log('[fastfoodApi] createOrder')
    return request('/api/orders', {
      method: 'POST',
      body: JSON.stringify(payload),
    })
  },
  getOrders: () => {
    console.log('[fastfoodApi] getOrders')
    return request('/api/orders')
  },
  getOrder: (id) => {
    console.log('[fastfoodApi] getOrder:', id)
    return request(`/api/orders/${id}`)
  },
  updateOrder: (id, payload) => {
    console.log('[fastfoodApi] updateOrder:', id)
    return request(`/api/orders/${id}`, {
      method: 'PUT',
      body: JSON.stringify(payload),
    })
  },
  deleteOrder: (id) => {
    console.log('[fastfoodApi] deleteOrder:', id)
    return request(`/api/orders/${id}`, {
      method: 'DELETE',
    })
  },

  // Order Items
  createOrderItem: (payload) => {
    console.log('[fastfoodApi] createOrderItem')
    return request('/api/order-items', {
      method: 'POST',
      body: JSON.stringify(payload),
    })
  },
  getOrderItems: () => {
    console.log('[fastfoodApi] getOrderItems')
    return request('/api/order-items')
  },
  deleteOrderItem: (id) => {
    console.log('[fastfoodApi] deleteOrderItem:', id)
    return request(`/api/order-items/${id}`, {
      method: 'DELETE',
    })
  },
}
