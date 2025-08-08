import React, { useMemo, useState } from 'react'
import { useApp } from '../context/AppContext'
import { matchText } from '../utils/storage'

export default function CatalogoPage(){
  const { state } = useApp()
  const [q, setQ] = useState('')
  const [tipo, setTipo] = useState('')
  const [autor, setAutor] = useState('')

  const lista = useMemo(()=> state.publicaciones.filter(p=> p.estado==='PUBLICADO'), [state])
  const filtrada = useMemo(()=> lista.filter(p=> (
    matchText(p.titulo, q) || p.palabrasClave?.some(k=> matchText(k,q))
  ) && (!tipo || p.tipo===tipo) && (!autor || p.autorPrincipal===autor)
  ), [q,tipo,autor,lista])

  const autores = Array.from(new Set(lista.map(p=>p.autorPrincipal)))

  return (
    <div className="container">
      <h2>Catálogo público</h2>
      <div className="filters">
        <input className="input" placeholder="Buscar por título o palabra clave" value={q} onChange={e=>setQ(e.target.value)} style={{maxWidth:320}} />
        <select className="input" value={tipo} onChange={e=>setTipo(e.target.value)} style={{maxWidth:200}}>
          <option value="">Todos los tipos</option>
          <option value="articulo">Artículo</option>
          <option value="libro">Libro</option>
        </select>
        <select className="input" value={autor} onChange={e=>setAutor(e.target.value)} style={{maxWidth:220}}>
          <option value="">Todos los autores</option>
          {autores.map(a=> <option key={a}>{a}</option>)}
        </select>
      </div>
      <div className="row">
        {filtrada.map(p=> (
          <div key={p.id} className="col">
            <div className="card">
              <div style={{fontWeight:600}}>{p.titulo} <small>v{p.version}</small></div>
              <div style={{color:'#9fb1cc'}}>{p.tipo} · {p.autorPrincipal}</div>
              <div style={{marginTop:8}}>{p.resumen}</div>
              <div className="filters">
                {p.palabrasClave?.map(k=> <span key={k} className="badge">{k}</span>)}
              </div>
              {p.tipo==='articulo' && <div className="badge">Revista: {p.articulo?.revistaObjetivo||'-'}</div>}
              {p.tipo==='libro' && <div className="badge">Capítulos: {p.libro?.capitulos?.length||0}</div>}
            </div>
          </div>
        ))}
      </div>
    </div>
  )
}
