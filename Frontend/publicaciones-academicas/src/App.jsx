import React from 'react'
import { Routes, Route, Navigate, Link } from 'react-router-dom'
import { AppProvider, useApp } from './context/AppContext'
import { Header, Toasts } from './components/Header'
import LoginPage from './pages/LoginPage'
import AutorPage from './pages/AutorPage'
import RevisorPage from './pages/RevisorPage'
import EditorPage from './pages/EditorPage'
import AdminPage from './pages/AdminPage'
import CatalogoPage from './pages/CatalogoPage'

function Guard({role, children}){
  const { state } = useApp()
  if(!role) return children
  return state.user?.rol===role? children : <Navigate to="/login" replace />
}

function Home(){
  return (
    <div className="container">
      <h1>Plataforma de Publicaciones Académicas</h1>
      <div className="row">
        <div className="col">
          <div className="card">
            <h3>Roles y módulos</h3>
            <ul>
              <li>Autor: crear/editar, enviar a revisión</li>
              <li>Revisor: comentarios y recomendaciones</li>
              <li>Editor: asignación y publicación final</li>
              <li>Administrador: métricas y usuarios</li>
              <li>Lector: catálogo público con filtros</li>
            </ul>
            <Link className="btn" to="/catalogo">Abrir catálogo</Link>
          </div>
        </div>
      </div>
    </div>
  )
}

export default function App(){
  return (
    <AppProvider>
      <Header />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/catalogo" element={<CatalogoPage />} />
        <Route path="/autor" element={<Guard role="autor"><AutorPage /></Guard>} />
        <Route path="/revisor" element={<Guard role="revisor"><RevisorPage /></Guard>} />
        <Route path="/editor" element={<Guard role="editor"><EditorPage /></Guard>} />
        <Route path="/admin" element={<Guard role="admin"><AdminPage /></Guard>} />
        <Route path="*" element={<Navigate to="/" />} />
      </Routes>
      <Toasts />
    </AppProvider>
  )
}
