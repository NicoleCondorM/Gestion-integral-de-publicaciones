import React, { useMemo, useState } from 'react'
import { useApp } from '../context/AppContext'
import { Field } from '../components/Common'

export default function RevisorPage(){
  const { state, addComentarioRevision, cambiarEstado } = useApp()
  const asignadas = useMemo(()=> state.publicaciones.filter(p=> {
    const arr = p.asignados?.revisores || []
    const me = state.user
    if(!me) return false
    // Support IDs and legacy names
    return arr.includes(me.id) || arr.includes(me.nombre)
  }), [state])
  const [coment, setComent] = useState({})

  const send = (p, decision)=>{
    const c = coment[p.id] || { aspectos: '', decision }
    addComentarioRevision(p.id, { autor: state.user?.nombre, aspectos: c.aspectos, decision })
    if(decision==='aceptar') cambiarEstado(p.id, 'APROBADO')
    if(decision==='rechazar' || decision==='cambios') cambiarEstado(p.id, 'CAMBIOS_SOLICITADOS')
  }

  return (
    <div className="container">
      <h2>Panel del Revisor</h2>
      {asignadas.map(p=> (
        <div key={p.id} className="card" style={{marginBottom:12}}>
          <div style={{display:'flex',justifyContent:'space-between'}}>
            <div>
              <strong>{p.titulo}</strong> <span className={`status-${p.estado}`}>{p.estado}</span>
              <div style={{color:'#9fb1cc'}}>Autor: {p.autorPrincipal}</div>
            </div>
          </div>
          <div className="row" style={{marginTop:8}}>
            <div className="col">
              <div className="label">Resumen</div>
              <div>{p.resumen}</div>
            </div>
            <div className="col">
              <Field label="Comentarios (aspectos)">
                <textarea className="input" rows="3" value={(coment[p.id]?.aspectos)||''} onChange={e=> setComent(c=> ({...c, [p.id]:{...c[p.id], aspectos:e.target.value}}))} />
              </Field>
              <div style={{display:'flex', gap:6}}>
                <button className="btn success" onClick={()=>send(p,'aceptar')}>Recomendar Aceptar</button>
                <button className="btn warning" onClick={()=>send(p,'cambios')}>Solicitar Cambios</button>
                <button className="btn danger" onClick={()=>send(p,'rechazar')}>Recomendar Rechazo</button>
              </div>
            </div>
          </div>
          <div style={{marginTop:8}}>
            <div className="label">Historial de comentarios</div>
            {(p.comentarios||[]).map(c=> (
              <div key={c.id} style={{borderTop:'1px solid #223', paddingTop:6}}>
                <span className="badge">{c.autor}</span> · {new Date(c.fecha).toLocaleString()} · {c.decision}
                <div style={{color:'#c6d4ea'}}>{c.aspectos}</div>
              </div>
            ))}
          </div>
        </div>
      ))}
    </div>
  )
}
