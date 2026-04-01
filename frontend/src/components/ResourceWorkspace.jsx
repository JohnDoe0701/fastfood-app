import { JsonPanel } from './JsonPanel'
import { ResultViewer } from './ResultViewer'
import { useResourceWorkspace } from '../hooks/useResourceWorkspace'

export function ResourceWorkspace({ resource }) {
  const ws = useResourceWorkspace(resource)

  return (
    <section className="workspace">
      <div className="panel-grid">
        <div className="panel">
          <h2>{resource.title}</h2>
          <p className="muted">Base path: {resource.path}</p>
          <div className="actions-row">
            <button onClick={ws.getAll} disabled={ws.busy}>
              GET All
            </button>
            {resource.supportsCrud ? null : <span className="tag">Health Endpoint</span>}
          </div>

          {resource.supportsCrud ? (
            <>
              <div className="inline-fields">
                <label>
                  ID for GET
                  <input value={ws.idForGet} onChange={(e) => ws.setIdForGet(e.target.value)} />
                </label>
                <button onClick={ws.getById} disabled={ws.busy}>
                  GET By ID
                </button>
              </div>

              <JsonPanel
                label="POST Payload"
                value={ws.createJson}
                onChange={ws.setCreateJson}
                disabled={ws.busy}
              />
              <button onClick={ws.createItem} disabled={ws.busy}>
                POST Create
              </button>

              <div className="inline-fields">
                <label>
                  ID for PUT
                  <input value={ws.idForUpdate} onChange={(e) => ws.setIdForUpdate(e.target.value)} />
                </label>
              </div>
              <JsonPanel
                label="PUT Payload"
                value={ws.updateJson}
                onChange={ws.setUpdateJson}
                disabled={ws.busy}
              />
              <button onClick={ws.updateItem} disabled={ws.busy}>
                PUT Update
              </button>

              <div className="inline-fields">
                <label>
                  ID for DELETE
                  <input value={ws.idForDelete} onChange={(e) => ws.setIdForDelete(e.target.value)} />
                </label>
                <button className="danger" onClick={ws.deleteItem} disabled={ws.busy}>
                  DELETE
                </button>
              </div>
            </>
          ) : null}
        </div>

        <ResultViewer output={ws.output} error={ws.error} />
      </div>
    </section>
  )
}
