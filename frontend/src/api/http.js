const API_BASE_URL = 'http://localhost:8080'

const DEFAULT_HEADERS = {
  'Content-Type': 'application/json',
}

export function getAuthToken() {
  const token = localStorage.getItem('ak-auth-token')
  console.log('[http.getAuthToken] Token exists:', !!token)
  return token
}

export function setAuthToken(token) {
  if (token) {
    localStorage.setItem('ak-auth-token', token)
    console.log('[http.setAuthToken] Token stored')
  } else {
    localStorage.removeItem('ak-auth-token')
    console.log('[http.setAuthToken] Token cleared')
  }
}

export function clearAuth() {
  localStorage.removeItem('ak-auth-token')
  localStorage.removeItem('ak-auth-user')
  console.log('[http.clearAuth] Auth data cleared')
}

export function getAuthUser() {
  try {
    const user = localStorage.getItem('ak-auth-user')
    const parsed = user ? JSON.parse(user) : null
    console.log('[http.getAuthUser] User retrieved:', parsed?.email ?? 'none')
    return parsed
  } catch (error) {
    console.error('[http.getAuthUser] Error parsing user:', error)
    return null
  }
}

export function setAuthUser(user) {
  if (user) {
    localStorage.setItem('ak-auth-user', JSON.stringify(user))
    console.log('[http.setAuthUser] User stored:', user.email)
  }
}

export async function request(path, options = {}) {
  const url = path.startsWith('http') ? path : `${API_BASE_URL}${path}`
  const headers = { ...DEFAULT_HEADERS, ...(options.headers ?? {}) }
  
  const token = getAuthToken()
  if (token) {
    headers['Authorization'] = `Bearer ${token}`
  }

  const method = options.method || 'GET'
  console.log(`[http.request] ${method} ${url}`)

  try {
    const response = await fetch(url, {
      ...options,
      headers,
    })

    const text = await response.text()
    const payload = text ? safeParseJson(text) : null

    console.log(`[http.request] Response status: ${response.status}`)

    if (!response.ok) {
      const errorMessage =
        payload?.message ?? payload?.error ?? `Request failed with status ${response.status}`
      console.error(`[http.request] Error: ${errorMessage}`)
      throw new Error(errorMessage)
    }

    console.log(`[http.request] Success`)
    return payload
  } catch (error) {
    console.error(`[http.request] Network/parsing error:`, error)
    throw error
  }
}

function safeParseJson(text) {
  try {
    return JSON.parse(text)
  } catch (error) {
    console.warn('[http.safeParseJson] Failed to parse JSON response')
    return text
  }
}
