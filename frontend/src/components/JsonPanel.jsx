export function JsonPanel({ label, value, onChange, disabled = false }) {
  return (
    <label className="field-block">
      <span>{label}</span>
      <textarea
        className="json-input"
        value={value}
        onChange={(event) => onChange(event.target.value)}
        disabled={disabled}
        rows={8}
      />
    </label>
  )
}
