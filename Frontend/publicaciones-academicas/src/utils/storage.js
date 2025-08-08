// Persistencia sencilla en localStorage
const KEY = 'pubs_state_v1'

export function loadState() {
  try {
    const raw = localStorage.getItem(KEY)
    return raw ? JSON.parse(raw) : null
  } catch { return null }
}

export function saveState(state) {
  try { localStorage.setItem(KEY, JSON.stringify(state)) } catch {}
}

export function genId(prefix='id'){ return `${prefix}_${Math.random().toString(36).slice(2,9)}` }

export function formatFecha(ts){
  const d = new Date(ts)
  return d.toLocaleDateString()
}

export function matchText(v, q){
  if(!q) return true
  return (v||'').toString().toLowerCase().includes(q.toLowerCase())
}
