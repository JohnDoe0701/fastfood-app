import { useMemo, useState } from 'react'
import { buildCrudApi } from '../api/resourceApi'
import { request } from '../api/http'

export function useResourceWorkspace(resource) {
  const api = useMemo(() => buildCrudApi(resource.path), [resource.path])

  const [busy, setBusy] = useState(false)
  const [error, setError] = useState('')
  const [output, setOutput] = useState(null)

  const [idForGet, setIdForGet] = useState(resource.sampleGetById ?? '1')
  const [idForUpdate, setIdForUpdate] = useState(resource.sampleUpdateId ?? '1')
  const [idForDelete, setIdForDelete] = useState(resource.sampleDeleteId ?? '1')

  const [createJson, setCreateJson] = useState(toPrettyJson(resource.sampleCreate ?? {}))
  const [updateJson, setUpdateJson] = useState(toPrettyJson(resource.sampleUpdate ?? {}))

  async function run(action) {
    setBusy(true)
    setError('')
    try {
      const data = await action()
      setOutput(data)
    } catch (err) {
      setError(err.message)
    } finally {
      setBusy(false)
    }
  }

  function getAll() {
    return run(() => (resource.supportsCrud ? api.list() : request(resource.path)))
  }

  function getById() {
    return run(() => api.getById(idForGet))
  }

  function createItem() {
    const payload = parseJson(createJson)
    return run(() => api.create(payload))
  }

  function updateItem() {
    const payload = parseJson(updateJson)
    return run(() => api.update(idForUpdate, payload))
  }

  function deleteItem() {
    return run(() => api.remove(idForDelete))
  }

  return {
    busy,
    error,
    output,
    idForGet,
    idForUpdate,
    idForDelete,
    createJson,
    updateJson,
    setIdForGet,
    setIdForUpdate,
    setIdForDelete,
    setCreateJson,
    setUpdateJson,
    getAll,
    getById,
    createItem,
    updateItem,
    deleteItem,
  }
}

function parseJson(value) {
  try {
    return JSON.parse(value)
  } catch {
    throw new Error('Invalid JSON payload')
  }
}

function toPrettyJson(value) {
  return JSON.stringify(value, null, 2)
}
