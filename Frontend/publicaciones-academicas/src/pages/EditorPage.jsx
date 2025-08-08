import React, { useMemo, useState } from 'react'
import { useApp } from '../context/AppContext'

export default function EditorPage(){
  const { state, asignarRevisores, cambiarEstado } = useApp()
  const pendientes = useMemo(()=> state.publicaciones.filter(p=> ['EN_REVISION','APROBADO','CAMBIOS_SOLICITADOS'].includes(p.estado)), [state])
  const [sel, setSel] = useState({})

  const revisoresObjs = state.users.filter(u=>u.rol==='revisor')

  // Helper: determine if reviewer is currently assigned (supports legacy names or new IDs)
  const isAssigned = (p, reviewer)=>{
    const arr = sel[p.id] || p.asignados?.revisores || []
    return arr.includes(reviewer.id) || arr.includes(reviewer.nombre)
  }

  return (
    <div className="container">
      <h2>Panel del Editor</h2>
      {pendientes.map(p=> (
        <div key={p.id} className="card" style={{marginBottom:12}}>
          <div style={{display:'flex',justifyContent:'space-between',alignItems:'center'}}>
            <div>
              <strong>{p.titulo}</strong> · <span className={`status-${p.estado}`}>{p.estado}</span>
              <div style={{color:'#9fb1cc'}}>Autor: {p.autorPrincipal}</div>
            </div>
            <div style={{display:'flex',gap:6}}>
              {p.estado==='APROBADO' && <button className="btn success" onClick={()=>cambiarEstado(p.id,'PUBLICADO')}>Publicar</button>}
              {p.estado==='CAMBIOS_SOLICITADOS' && <button className="btn" onClick={()=>cambiarEstado(p.id,'EN_REVISION')}>Reenviar a revisión</button>}
            </div>
          </div>
          <div style={{marginTop:8}}>
            <div className="label">Asignar revisores</div>
            <div className="filters">
              {revisoresObjs.map(r=> (
                <label key={r.id} className="badge">
                  <input
                    type="checkbox"
                    checked={isAssigned(p, r)}
                    onChange={(e)=>{
                      const base = sel[p.id] || p.asignados?.revisores || []
                      const curr = new Set(base.map(x=>x))
                      // We store IDs moving forward
                      if(e.target.checked){
                        // remove legacy name if present
                        curr.delete(r.nombre)
                        curr.add(r.id)
                      } else {
                        curr.delete(r.id)
                        curr.delete(r.nombre)
                      }
                      setSel(s=> ({...s, [p.id]: Array.from(curr)}))
                    }}
                  /> {r.nombre}
                </label>
              ))}
            </div>
            <div style={{display:'flex', gap:6, marginTop:6}}>
              <button
                className="btn primary"
                onClick={()=>{
                  const base = sel[p.id] || p.asignados?.revisores || []
                  // Normalize to IDs when possible
                  const idSet = new Set()
                  base.forEach(x=>{
                    const found = revisoresObjs.find(r=> r.id===x || r.nombre===x)
                    if(found) idSet.add(found.id)
                    else idSet.add(x)
                  })
                  asignarRevisores(p.id, Array.from(idSet))
                }}
              >
                Guardar asignación
              </button>
            </div>
          </div>
        </div>
      ))}
    </div>
  )
}
