import React from 'react'
import { Link } from 'react-router-dom'
import { useApp } from '../context/AppContext'

export function Header(){
  const { state, logout } = useApp()
  const u = state.user
  return (
    <header className="header">
      <Link to="/"><strong>Publicaciones</strong></Link>
      <nav className="nav">
        <Link to="/catalogo">Catálogo</Link>
        {u?.rol==='autor' && <Link to="/autor">Autor</Link>}
        {u?.rol==='revisor' && <Link to="/revisor">Revisor</Link>}
        {u?.rol==='editor' && <Link to="/editor">Editor</Link>}
        {u?.rol==='admin' && <Link to="/admin">Admin</Link>}
      </nav>
      <div>
        {u ? (
          <>
            <span className="badge" style={{marginRight:8}}>{u.nombre} · {u.rol}</span>
            <button className="btn" onClick={logout}>Salir</button>
          </>
        ) : (
          <Link className="btn" to="/login">Entrar</Link>
        )}
      </div>
    </header>
  )
}

export function Toasts(){
  const { state } = useApp()
  return (
    <div className="toast" style={{display: state.notifs.length? 'block':'none'}}>
      {state.notifs.map(n=> (
        <div key={n.id} style={{marginBottom:8}}>
          <span className="badge" style={{marginRight:6}}>{n.type}</span>
          {n.msg}
        </div>
      ))}
    </div>
  )
}
