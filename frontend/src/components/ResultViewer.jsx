export function ResultViewer({ output, error }) {
  return (
    <section className="result-viewer">
      <h3>Response</h3>
      {error ? <p className="error-text">{error}</p> : null}
      <pre>{output ? JSON.stringify(output, null, 2) : 'Run an action to see response here.'}</pre>
    </section>
  )
}
