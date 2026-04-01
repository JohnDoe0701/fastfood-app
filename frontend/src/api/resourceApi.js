import { request } from './http'

export function buildCrudApi(basePath) {
  return {
    list: () => request(basePath),
    getById: (id) => request(`${basePath}/${id}`),
    create: (payload) =>
      request(basePath, {
        method: 'POST',
        body: JSON.stringify(payload),
      }),
    update: (id, payload) =>
      request(`${basePath}/${id}`, {
        method: 'PUT',
        body: JSON.stringify(payload),
      }),
    remove: (id) =>
      request(`${basePath}/${id}`, {
        method: 'DELETE',
      }),
  }
}
