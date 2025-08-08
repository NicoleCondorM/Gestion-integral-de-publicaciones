import React from 'react'
import { useApp } from '../context/AppContext'

export default function AdminPage(){
  const { state } = useApp()
  const counts = state.publicaciones.reduce((acc,p)=>{acc[p.estado]=(acc[p.estado]||0)+1; return acc},{})
  return (
    <div className="container">
      <h2>Panel del Administrador</h2>
      <div className="kpis">
        {Object.entries(counts).map(([k,v])=> (
          <div key={k} className="kpi">
            <div className="label">{k}</div>
            <div style={{fontSize:24,fontWeight:700}}>{v}</div>
          </div>
        ))}
        <div className="kpi">
          <div className="label">Usuarios</div>
          <div style={{fontSize:24,fontWeight:700}}>{state.users.length}</div>
        </div>
      </div>
      <div className="card">
        <h3>Usuarios</h3>
        <table className="table">
          <thead><tr><th>Nombre</th><th>Rol</th></tr></thead>
          <tbody>
            {state.users.map(u=> <tr key={u.id}><td>{u.nombre}</td><td>{u.rol}</td></tr>)}
          </tbody>
        </table>
      </div>
    </div>
  )
}
