import React from 'react'

export function EstadoBadge({estado}){
  return <span className={`badge status-${estado}`}>{estado}</span>
}

export function Field({label, children}){
  return (
    <div style={{marginBottom:12}}>
      <div className="label">{label}</div>
      {children}
    </div>
  )
}

export function PublicationCard({p, onEdit, actions}){
  return (
    <div className="card" style={{marginBottom:12}}>
      <div style={{display:'flex',justifyContent:'space-between',alignItems:'center'}}>
        <div>
          <div style={{fontWeight:600}}>{p.titulo} <small>v{p.version}</small></div>
          <div style={{color:'#9fb1cc'}}>{p.tipo} · {p.autorPrincipal}</div>
        </div>
        <EstadoBadge estado={p.estado} />
      </div>
      <div style={{marginTop:8, color:'#c6d4ea'}}>{p.resumen}</div>
      <div className="filters">
        {p.palabrasClave?.map(k=> <span key={k} className="badge">{k}</span>)}
      </div>
      <div style={{display:'flex',gap:8,marginTop:8}}>
        {onEdit && <button className="btn" onClick={()=>onEdit(p)}>Editar</button>}
        {actions}
      </div>
    </div>
  )
}
