
import React from 'react'
import { useApp } from '../context/AppContext'
import { ROLES } from '../data/constants'
import { useNavigate } from 'react-router-dom'


export default function LoginPage(){
  const { state, login } = useApp()
  const [role, setRole] = React.useState(ROLES.LECTOR)
  const [nombre, setNombre] = React.useState('')
  const navigate = useNavigate()

  // Mapeo de roles a rutas
  const roleToRoute = {
    [ROLES.AUTOR]: '/autor',
    [ROLES.REVISOR]: '/revisor',
    [ROLES.EDITOR]: '/editor',
    [ROLES.ADMIN]: '/admin',
    [ROLES.LECTOR]: '/catalogo',
  }

  const entrar = (e)=>{
    e.preventDefault()
    login(role, nombre)
    // Navegar después de un pequeño delay para asegurar el cambio de estado
    setTimeout(() => {
      navigate(roleToRoute[role] || '/')
    }, 100)
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
          <div className="label" style={{marginTop:10}}>Password</div>
          <input className="input" value={nombre} onChange={e=>setNombre(e.target.value)} type="password" />
          <div style={{marginTop:10, display:'flex', gap:8}}>
            <button className="btn primary" type="submit">Entrar</button>
          </div>
        </form>
      </div>
    </div>
  )
}
