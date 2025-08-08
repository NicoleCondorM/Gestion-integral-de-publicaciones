import React from 'react'
import { useApp } from '../context/AppContext'
import { ROLES } from '../data/constants'

export default function LoginPage(){
  const { state, login } = useApp()
  const [role, setRole] = React.useState(ROLES.LECTOR)
  const [nombre, setNombre] = React.useState('')

  const entrar = (e)=>{
    e.preventDefault()
    login(role, nombre)
  }

  return (
    <div className="container">
      <h2>Ingreso</h2>
      <div className="card" style={{maxWidth:520}}>
        <form onSubmit={entrar}>
          <div className="label">Rol</div>
          <select className="input" value={role} onChange={e=>setRole(e.target.value)}>
            {Object.values(ROLES).map(r=> <option key={r} value={r}>{r}</option>)}
          </select>
          <div className="label" style={{marginTop:10}}>Nombre (opcional, coincide con un usuario para datos existentes)</div>
          <input className="input" value={nombre} onChange={e=>setNombre(e.target.value)} placeholder="Ej: Ana Autora" />
          <div style={{marginTop:10, display:'flex', gap:8}}>
            <button className="btn primary" type="submit">Entrar</button>
          </div>
          <div style={{color:'#9fb1cc', marginTop:8}}>
            Usuarios demo: Ana Autora (autor), Rafa Revisor (revisor), Ema Editora (editor), Admin (admin), Luz Lectora (lector)
          </div>
        </form>
      </div>
    </div>
  )
}
