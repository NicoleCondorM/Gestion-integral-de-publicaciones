import React, {createContext, useContext, useEffect, useMemo, useState} from 'react'
import { ESTADOS, PUBLICACIONES_SEED, USERS_SEED } from '../data/constants'
import { genId, loadState, saveState } from '../utils/storage'

const AppCtx = createContext(null)
export const useApp = ()=> useContext(AppCtx)

const initial = loadState() || {
  user: null,
  users: USERS_SEED,
  publicaciones: PUBLICACIONES_SEED,
  notifs: []
}

export function AppProvider({children}){
  const [state, setState] = useState(initial)
  useEffect(()=> saveState(state), [state])

  const login = (rol, nombre) => {
    const found = state.users.find(u=>u.rol===rol && (!nombre || u.nombre===nombre))
    if(found){
      setState(s=> ({...s, user: found}))
    } else {
      const nuevo = { id: genId('u'), nombre: nombre||rol, rol }
      setState(s=> ({...s, user: nuevo, users: [...s.users, nuevo]}))
      pushNotif('Usuario registrado', 'info')
    }
  }
  const logout = ()=> setState(s=> ({...s, user:null}))

  const pushNotif = (msg, type='info') => {
    const id = genId('n')
    setState(s=> ({...s, notifs:[...s.notifs, {id, msg, type}]}))
    setTimeout(()=> setState(s=> ({...s, notifs: s.notifs.filter(n=>n.id!==id)})), 3500)
  }

  const upsertPub = (pub) => {
    setState(s=>{
      const exists = s.publicaciones.find(p=>p.id===pub.id)
      let pubs
      if(exists){
        const newVersion = (exists.version||1) + 1
        pubs = s.publicaciones.map(p=> p.id===pub.id? {
          ...exists,
          ...pub,
          version: newVersion,
          historial: [...(exists.historial||[]), {fecha:Date.now(), accion:`Actualizado v${newVersion}`}]
        }: p)
      } else {
        pubs = [{...pub, id: genId('p'), historial:[{fecha:Date.now(), accion:'Creado BORRADOR v1'}]}, ...s.publicaciones]
      }
      return {...s, publicaciones: pubs}
    })
    pushNotif('Publicación guardada', 'success')
  }

  const cambiarEstado = (id, nuevoEstado) => {
    if(!ESTADOS.includes(nuevoEstado)) return
    setState(s=>{
      const pubs = s.publicaciones.map(p=> p.id===id? {
        ...p,
        estado: nuevoEstado,
        version: nuevoEstado==='CAMBIOS_SOLICITADOS'? p.version: p.version + (nuevoEstado==='EN_REVISION'?0:1),
        historial: [...(p.historial||[]), {fecha:Date.now(), accion:`Estado -> ${nuevoEstado} v${(p.version||1)}`}]
      }: p)
      return {...s, publicaciones: pubs}
    })
    pushNotif(`Estado actualizado a ${nuevoEstado}`, 'info')
  }

  const asignarRevisores = (id, revisores=[])=>{
    setState(s=>{
      const normalize = (vals)=>{
        const namesById = new Map(s.users.map(u=>[u.id,u.nombre]))
        const idsByName = new Map(s.users.map(u=>[u.nombre,u.id]))
        const set = new Set()
        vals.forEach(v=>{
          if(namesById.has(v)) set.add(v)
          else if(idsByName.has(v)) set.add(idsByName.get(v))
        })
        return Array.from(set)
      }
      const pubs = s.publicaciones.map(p=> p.id===id? {
        ...p, asignados: { ...(p.asignados||{}), revisores: normalize(revisores) }
      }: p)
      return {...s, publicaciones: pubs}
    })
    pushNotif('Revisores asignados', 'success')
  }

  const addComentarioRevision = (id, comentario)=>{
    setState(s=>{
      const pubs = s.publicaciones.map(p=> p.id===id? {
        ...p,
        comentarios: [...(p.comentarios||[]), { id: genId('c'), fecha: Date.now(), ...comentario }]
      }: p)
      return {...s, publicaciones: pubs}
    })
    pushNotif('Comentario registrado', 'success')
  }

  const value = useMemo(()=>({ state, login, logout, upsertPub, cambiarEstado, asignarRevisores, addComentarioRevision }), [state])
  return <AppCtx.Provider value={value}>{children}</AppCtx.Provider>
}
