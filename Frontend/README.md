# Publicaciones Académicas (Maqueta React)

Proyecto React (Vite) que simula una plataforma de gestión de publicaciones académicas con roles: Autor, Revisor, Editor, Administrador y Lector. Sin backend ni API: usa datos estáticos y localStorage.

## Características
- Login/registro simulado por rol (sin auth real)
- Panel Autor: crear/editar publicaciones, enviar a revisión
- Panel Revisor: ver asignadas, comentar, recomendar decisiones
- Panel Editor: asignar revisores, publicar
- Panel Lector: catálogo público con filtros
- Notificaciones visuales (toasts)
- Ciclo de vida: BORRADOR → EN_REVISION → CAMBIOS_SOLICITADOS → APROBADO → PUBLICADO

## Estructura
- src/context/AppContext.jsx: estado global, acciones y persistencia
- src/pages/*: vistas por rol
- src/components/*: UI común (Header, badges)
- src/data/constants.js: semilla de datos y constantes
- src/utils/storage.js: utilidades y localStorage

## Ejecutar

```powershell
# Instalar dependencias
npm install

# Desarrollo
npm run dev

# Build (listo para Vercel u otro hosting)
npm run build
npm run preview
```

En Vercel, selecciona este directorio y usa el framework Vite con comando de build `npm run build` y directorio de salida `dist/`.

## Notas
- Para entrar como demo usa la página /login y elige un rol; puedes ingresar nombres como "Ana Autora", "Rafa Revisor", "Ema Editora", "Admin", "Luz Lectora".
- Los datos se guardan en localStorage para persistencia básica local.
