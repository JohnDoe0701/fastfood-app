import React, { useEffect, useState } from 'react'
import { fastfoodApi } from './api/fastfoodApi'
import { getAuthToken, setAuthToken, clearAuth, getAuthUser, setAuthUser } from './api/http'
import { SiteHeader } from './components/SiteHeader'
import { AuthSection } from './components/AuthSection'
import { MenuSection } from './components/MenuSection'
import { CartSection } from './components/CartSection'
import { ProfileSection } from './components/ProfileSection'
import { OrdersSection } from './components/OrdersSection'
import { useCart } from './hooks/useCart'
import { useLocalStorageState } from './hooks/useLocalStorageState'
import './App.css'

// Error boundary component for catching component render errors
class ErrorBoundary extends React.Component {
  constructor(props) {
    super(props)
    this.state = { hasError: false, error: null }
  }

  static getDerivedStateFromError(error) {
    console.error('Error caught by boundary:', error)
    return { hasError: true, error }
  }

  componentDidCatch(error, errorInfo) {
    console.error('Component stack:', errorInfo.componentStack)
  }

  render() {
    if (this.state.hasError) {
      return (
        <div className="container">
          <div style={{ padding: '40px', textAlign: 'center' }}>
            <h2 style={{ color: '#ff6b6b' }}>Something went wrong</h2>
            <p style={{ color: '#96a4bd', marginTop: '10px' }}>
              {this.state.error?.message || 'An unexpected error occurred'}
            </p>
            <p style={{ color: '#666', fontSize: '12px', marginTop: '20px' }}>
              Check the browser console for more details
            </p>
            <button
              onClick={() => {
                this.setState({ hasError: false, error: null })
                window.location.reload()
              }}
              style={{
                marginTop: '20px',
                padding: '10px 20px',
                backgroundColor: '#36d2a7',
                color: '#fff',
                border: 'none',
                borderRadius: '8px',
                cursor: 'pointer',
              }}
            >
              Reload Page
            </button>
          </div>
        </div>
      )
    }

    return this.props.children
  }
}

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false)
  const [health, setHealth] = useState('...')
  const [categories, setCategories] = useState([])
  const [menuItems, setMenuItems] = useState([])
  const [orders, setOrders] = useState([])
  const [orderItems, setOrderItems] = useState([])
  const [message, setMessage] = useState('')
  const [busy, setBusy] = useState(false)
  const [profile, setProfile] = useState({
    id: null,
    fullName: '',
    email: '',
    phone: '',
    userType: '',
    enabled: true,
  })

  const cart = useCart()

  // Initialize auth state from localStorage
  useEffect(() => {
    try {
      const token = getAuthToken()
      const user = getAuthUser()
      
      console.log('[App] Initializing auth state:', { hasToken: !!token, hasUser: !!user })
      
      if (token && user) {
        setProfile(user)
        setIsAuthenticated(true)
        console.log('[App] User authenticated:', user.email)
      } else {
        setIsAuthenticated(false)
        console.log('[App] User not authenticated')
      }
    } catch (error) {
      console.error('[App] Error initializing auth:', error)
      clearAuth()
      setIsAuthenticated(false)
    }
  }, [])

  // Load initial data when authenticated
  useEffect(() => {
    if (isAuthenticated) {
      console.log('[App] Loading initial data (authenticated)')
      loadInitial()
    }
  }, [isAuthenticated])

  async function loadInitial() {
    try {
      console.log('[App] Fetching health, categories, and menu items')
      const [healthData, categoriesData, menuData] = await Promise.all([
        fastfoodApi.getHealth().catch((error) => {
          console.warn('[App] Health endpoint failed:', error.message)
          return { status: 'UNKNOWN' }
        }),
        fastfoodApi.getCategories().catch((error) => {
          console.error('[App] Categories fetch failed:', error.message)
          throw error
        }),
        fastfoodApi.getMenuItems().catch((error) => {
          console.error('[App] Menu items fetch failed:', error.message)
          throw error
        }),
      ])
      
      setHealth(healthData?.status ?? 'UP')
      setCategories(categoriesData ?? [])
      setMenuItems(menuData ?? [])
      console.log('[App] Initial data loaded successfully')
      
      // Load user's orders
      try {
        console.log('[App] Fetching user orders')
        const ordersData = await fastfoodApi.getOrders()
        setOrders(ordersData ?? [])
        console.log('[App] Orders loaded:', ordersData?.length ?? 0)
      } catch (error) {
        console.warn('[App] Orders fetch failed (non-critical):', error.message)
      }
    } catch (error) {
      console.error('[App] Error loading initial data:', error)
      setMessage(`Error loading data: ${error.message}`)
    }
  }

  async function runAction(fn) {
    setBusy(true)
    try {
      await fn()
    } catch (error) {
      console.error('[App] Action error:', error)
      setMessage(error.message || 'An error occurred')
    } finally {
      setBusy(false)
    }
  }

  async function handleRegister(payload) {
    console.log('[App] Attempting registration:', payload.email)
    await runAction(async () => {
      try {
        const response = await fastfoodApi.register(payload)
        console.log('[App] Registration successful:', response)
        
        if (response.access_token) {
          setAuthToken(response.access_token)
          setAuthUser(response.user)
          setProfile(response.user)
          setIsAuthenticated(true)
          setMessage(`Welcome ${response.user.fullName}! You are now registered and logged in.`)
          console.log('[App] Auth state updated, redirecting to main app')
        } else {
          throw new Error('No access token in registration response')
        }
      } catch (error) {
        console.error('[App] Registration failed:', error)
        throw error
      }
    })
  }

  async function handleLogin(payload) {
    console.log('[App] Attempting login:', payload.email)
    await runAction(async () => {
      try {
        const response = await fastfoodApi.login(payload)
        console.log('[App] Login successful:', response)
        
        if (response.access_token) {
          setAuthToken(response.access_token)
          setAuthUser(response.user)
          setProfile(response.user)
          setIsAuthenticated(true)
          setMessage(`Welcome back, ${response.user.fullName}!`)
          console.log('[App] Auth state updated, redirecting to main app')
        } else {
          throw new Error('No access token in login response')
        }
      } catch (error) {
        console.error('[App] Login failed:', error)
        throw error
      }
    })
  }

  function handleLogout() {
    console.log('[App] Logging out')
    clearAuth()
    setProfile({
      id: null,
      fullName: '',
      email: '',
      phone: '',
      userType: '',
      enabled: true,
    })
    setIsAuthenticated(false)
    setMessage('You have been logged out.')
    console.log('[App] Logout complete')
  }

  async function updateProfile(payload) {
    if (!profile.id) {
      setMessage('No user logged in')
      console.warn('[App] Update profile attempted without user login')
      return
    }

    await runAction(async () => {
      console.log('[App] Updating profile:', profile.id)
      const user = await fastfoodApi.updateUser(profile.id, payload)
      setProfile(user)
      setAuthUser(user)
      setMessage('Profile updated successfully.')
      console.log('[App] Profile updated')
    })
  }

  async function reloadProfile() {
    if (!profile.id) {
      setMessage('No user logged in')
      return
    }

    await runAction(async () => {
      console.log('[App] Reloading profile:', profile.id)
      const user = await fastfoodApi.getUser(profile.id)
      setProfile(user)
      setAuthUser(user)
      setMessage('Profile reloaded from server.')
      console.log('[App] Profile reloaded')
    })
  }

  async function deleteProfile() {
    if (!profile.id) {
      setMessage('No user logged in')
      return
    }

    if (!confirm('Are you sure you want to delete your profile?')) {
      console.log('[App] Profile deletion cancelled')
      return
    }

    await runAction(async () => {
      console.log('[App] Deleting profile:', profile.id)
      await fastfoodApi.deleteUser(profile.id)
      clearAuth()
      setProfile({
        id: null,
        fullName: '',
        email: '',
        phone: '',
        userType: '',
        enabled: true,
      })
      setIsAuthenticated(false)
      setMessage('Your profile has been deleted.')
      console.log('[App] Profile deleted')
    })
  }

  async function placeOrder() {
    if (!profile.id) {
      setMessage('Please login to place an order.')
      console.warn('[App] Order placement attempted without user login')
      return
    }

    if (cart.items.length === 0) {
      setMessage('Cart is empty.')
      return
    }

    await runAction(async () => {
      console.log('[App] Creating order for user:', profile.id, 'Items:', cart.items.length)
      const order = await fastfoodApi.createOrder({
        userId: profile.id,
        status: 'CREATED',
        totalAmount: Number(cart.total.toFixed(2)),
      })

      console.log('[App] Order created:', order.id)

      for (const item of cart.items) {
        const lineTotal = Number((item.price * item.quantity).toFixed(2))
        console.log('[App] Adding order item:', item.name, 'quantity:', item.quantity)
        await fastfoodApi.createOrderItem({
          orderId: order.id,
          menuItemId: item.id,
          quantity: item.quantity,
          unitPrice: item.price,
          lineTotal: lineTotal,
        })
      }

      setMessage(`Order #${order.id} placed successfully!`)
      cart.clear()
      console.log('[App] Order placement complete')
      
      // Reload orders
      try {
        const ordersData = await fastfoodApi.getOrders()
        setOrders(ordersData ?? [])
      } catch (error) {
        console.warn('[App] Failed to reload orders:', error.message)
      }
    })
  }

  const handleStatusUpdate = async (order, newStatus) => {
    await runAction(async () => {
      console.log('[App] Updating order', order.id, 'status to:', newStatus)
      await fastfoodApi.updateOrder(order.id, {
        userId: order.user?.id || order.userId,
        status: newStatus,
        totalAmount: order.totalAmount,
        placedAt: order.placedAt,
      })
      setMessage(`Order #${order.id} status changed to ${newStatus}`)
      
      // Reload orders
      try {
        const ordersData = await fastfoodApi.getOrders()
        setOrders(ordersData ?? [])
      } catch (error) {
        console.warn('[App] Failed to reload orders:', error.message)
      }
    })
  }

  const handleDeleteOrder = async (orderId) => {
    await runAction(async () => {
      console.log('[App] Deleting order:', orderId)
      await fastfoodApi.deleteOrder(orderId)
      setMessage(`Order #${orderId} deleted successfully`)
      
      // Reload orders
      try {
        const ordersData = await fastfoodApi.getOrders()
        setOrders(ordersData ?? [])
      } catch (error) {
        console.warn('[App] Failed to reload orders:', error.message)
      }
    })
  }

  // Render
  console.log('[App] Rendering, isAuthenticated:', isAuthenticated)

  if (!isAuthenticated) {
    return (
      <div className="container">
        <div style={{ marginBottom: '30px' }}>
          <h1 style={{ marginBottom: '10px' }}>AK Food Online</h1>
          <p style={{ color: '#96a4bd' }}>Sign in or create an account to get started</p>
        </div>
        {message && <div className="message">{message}</div>}
        <AuthSection
          onRegisterSuccess={handleRegister}
          onLoginSuccess={handleLogin}
          busy={busy}
        />
        <div style={{ padding: '40px 20px', textAlign: 'center', color: '#666' }}>
          <p>NEW USER? Create an account with email and password</p>
          <p>RETURNING? Sign in with your credentials</p>
        </div>
      </div>
    )
  }

  return (
    <div className="container">
      <SiteHeader health={health} userName={profile.fullName} onLogout={handleLogout} />
      {message && <div className="message">{message}</div>}

      <div className="main-grid">
        <div className="left-column">
          <MenuSection
            categories={categories}
            menuItems={menuItems}
            onAddToCart={cart.add}
          />
          <CartSection
            items={cart.items}
            total={cart.total}
            onIncrease={cart.increase}
            onDecrease={cart.decrease}
            onCheckout={placeOrder}
            checkoutBusy={busy}
          />
        </div>

        <div className="right-column">
          <ProfileSection
            profile={profile}
            onUpdate={updateProfile}
            onLoad={reloadProfile}
            onDelete={deleteProfile}
            busy={busy}
          />
          <OrdersSection
            orders={orders}
            orderItems={orderItems}
            userType={profile.userType}
            onRefresh={loadInitial}
            onStatusUpdate={handleStatusUpdate}
            onDelete={handleDeleteOrder}
          />
        </div>
      </div>
    </div>
  )
}

export default function AppWithErrorBoundary() {
  return (
    <ErrorBoundary>
      <App />
    </ErrorBoundary>
  )
}
