import React, { useMemo, useState } from 'react'
import { useApp } from '../context/AppContext'
import { CATEGORIAS, LICENCIAS, TIPOS } from '../data/constants'
import { Field } from '../components/Common'

const empty = {
  id: undefined,
  tipo: 'articulo',
  titulo: '',
  resumen: '',
  palabrasClave: [],
  estado: 'BORRADOR',
  version: 1,
  meta: { isbn: '', doi: '', categoria: 'Ciencias', licencia: 'All rights reserved' },
  autorPrincipal: '',
  coautores: [],
  articulo: { revistaObjetivo: '', seccion: '', referencias: [], figuras: [] },
  libro: { capitulos: [] },
}

export default function AutorPage(){
  const { state, upsertPub, cambiarEstado } = useApp()
  const mias = useMemo(()=> state.publicaciones.filter(p=>p.autorPrincipal===state.user?.nombre), [state])
  const [form, setForm] = useState(empty)
  const [kw, setKw] = useState('')
  const [coas, setCoas] = useState('')
  const [refs, setRefs] = useState('')
  const [figs, setFigs] = useState('')

  const set = (k, v)=> setForm(f=> ({...f, [k]:v}))
  const submit = (e)=>{
    e.preventDefault()
    const payload = { 
      ...form,
      autorPrincipal: state.user?.nombre,
      palabrasClave: (form.palabrasClave||[]).filter(Boolean),
      coautores: coas? coas.split(';').map(s=>s.trim()).filter(Boolean): [],
      articulo: form.tipo==='articulo'? {
        ...form.articulo,
        referencias: refs? refs.split(';').map(s=>s.trim()).filter(Boolean): [],
        figuras: figs? figs.split(',').map(s=>s.trim()).filter(Boolean): [],
      }: form.articulo,
    }
    upsertPub(payload)
    setForm(empty)
    setKw(''); setCoas(''); setRefs(''); setFigs('')
  }

  const enviarRevision = (p)=> cambiarEstado(p.id, 'EN_REVISION')

  return (
    <div className="container">
      <h2>Panel del Autor</h2>
      <div className="row">
        <div className="col">
          <div className="card">
            <h3>Nueva publicación</h3>
            <form onSubmit={submit}>
              <Field label="Tipo">
                <select className="input" value={form.tipo} onChange={e=>set('tipo', e.target.value)}>
                  {TIPOS.map(t=> <option key={t} value={t}>{t}</option>)}
                </select>
              </Field>
              <Field label="Título">
                <input className="input" value={form.titulo} onChange={e=>set('titulo', e.target.value)} required />
              </Field>
              <Field label="Resumen">
                <textarea className="input" rows="3" value={form.resumen} onChange={e=>set('resumen', e.target.value)} />
              </Field>
              <Field label="Palabras clave (separadas por coma)">
                <input className="input" value={kw} onChange={e=>setKw(e.target.value)} onBlur={()=> set('palabrasClave', kw.split(',').map(s=>s.trim()))} />
              </Field>
              <div className="row">
                <div className="col">
                  <Field label="DOI">
                    <input className="input" value={form.meta.doi} onChange={e=>set('meta', {...form.meta, doi:e.target.value})} />
                  </Field>
                </div>
                <div className="col">
                  <Field label="ISBN (libro)">
                    <input className="input" value={form.meta.isbn} onChange={e=>set('meta', {...form.meta, isbn:e.target.value})} />
                  </Field>
                </div>
              </div>
              <Field label="Coautores (separados por ;)">
                <input className="input" value={coas} onChange={e=>setCoas(e.target.value)} />
              </Field>
              <div className="row">
                <div className="col">
                  <Field label="Categoría">
                    <select className="input" value={form.meta.categoria} onChange={e=>set('meta', {...form.meta, categoria:e.target.value})}>
                      {CATEGORIAS.map(c=> <option key={c}>{c}</option>)}
                    </select>
                  </Field>
                </div>
              </div>

              {form.tipo==='articulo' && (
                <div className="card" style={{marginTop:10}}>
                  <h4>Detalles de artículo</h4>
                  <Field label="Revista objetivo"><input className="input" value={form.articulo.revistaObjetivo} onChange={e=>set('articulo', {...form.articulo, revistaObjetivo:e.target.value})} /></Field>
                  <Field label="Sección"><input className="input" value={form.articulo.seccion} onChange={e=>set('articulo', {...form.articulo, seccion:e.target.value})} /></Field>
                  <Field label="Referencias (separadas por ;)">
                    <input className="input" value={refs} onChange={e=>setRefs(e.target.value)} />
                  </Field>
                  <Field label="Figuras (separadas por ,)">
                    <input className="input" value={figs} onChange={e=>setFigs(e.target.value)} />
                  </Field>
                </div>
              )}
              {form.tipo==='libro' && (
                <div className="card" style={{marginTop:10}}>
                  <h4>Detalles de libro</h4>
                  <Field label="Capítulos (JSON sencillo)">
                    <textarea className="input" rows="3" value={JSON.stringify(form.libro.capitulos)} onChange={e=>{
                      try {
                        const val = JSON.parse(e.target.value || '[]')
                        set('libro', {...form.libro, capitulos: Array.isArray(val)? val: []})
                      } catch { /* ignorar */ }
                    }} />
                  </Field>
                </div>
              )}
              <div style={{display:'flex',gap:8,marginTop:8}}>
                <button className="btn primary" type="submit">Guardar</button>
              </div>
            </form>
          </div>
        </div>
        <div className="col">
          <div className="card">
            <h3>Mis publicaciones</h3>
            {mias.map(p=> (
              <div key={p.id} style={{borderBottom:'1px solid #223', padding:'8px 0'}}>
                <div style={{display:'flex',justifyContent:'space-between'}}>
                  <div>
                    <strong>{p.titulo}</strong> <small>v{p.version}</small>
                    <div className={`status-${p.estado}`}>{p.estado}</div>
                  </div>
                  <div style={{display:'flex', gap:6}}>
                    <button className="btn" onClick={()=>setForm(p)}>Editar</button>
                    {p.estado==='BORRADOR' && <button className="btn success" onClick={()=>enviarRevision(p)}>Enviar a revisión</button>}
                  </div>
                </div>
                <div style={{color:'#9fb1cc'}}>{p.resumen}</div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  )
}
