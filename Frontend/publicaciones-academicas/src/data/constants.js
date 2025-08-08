export const ROLES = {
  LECTOR: 'lector',
  AUTOR: 'autor',
  REVISOR: 'revisor',
  EDITOR: 'editor',
  ADMIN: 'admin',
}

export const ESTADOS = [
  'BORRADOR',
  'EN_REVISION',
  'CAMBIOS_SOLICITADOS',
  'APROBADO',
  'PUBLICADO',
]

export const TIPOS = ['articulo', 'libro']

export const CATEGORIAS = ['Ciencias', 'Ingeniería', 'Salud', 'Humanidades']

export const LICENCIAS = ['All rights reserved', 'CC-BY', 'CC-BY-SA', 'CC0']

export const META_DEFAULT = {
  isbn: '',
  doi: '',
  categoria: 'Ciencias',
  licencia: 'All rights reserved',
}

export const USERS_SEED = [
  { id: 'u1', nombre: 'autor', rol: 'autor' },
  { id: 'u2', nombre: 'revisor', rol: 'revisor' },
  { id: 'u3', nombre: 'editor', rol: 'editor' },
  { id: 'u4', nombre: 'lector', rol: 'lector' },
  { id: 'u5', nombre: 'admin', rol: 'admin' },
]

export const PUBLICACIONES_SEED = [
  {
    id: 'p1',
    tipo: 'articulo',
    titulo: 'Análisis de Redes Complejas',
    resumen: 'Estudio de centralidad y modularidad en redes grandes.',
    palabrasClave: ['redes', 'grafos', 'modularidad'],
    estado: 'BORRADOR',
    version: 1,
    meta: { isbn: '', doi: '10.1000/xyz123', categoria: 'Ingeniería', licencia: 'CC-BY' },
    autorPrincipal: 'Ana Autora',
    coautores: ['Luis López'],
    articulo: {
      revistaObjetivo: 'Journal of Networks',
      seccion: 'Research Article',
      referencias: ['Newman 2010', 'Barabási 2002'],
      figuras: ['fig1.png']
    },
    historial: [{ fecha: Date.now(), accion: 'Creado BORRADOR v1' }],
    asignados: { revisores: ['Rafa Revisor'] }
  },
  {
    id: 'p2',
    tipo: 'libro',
    titulo: 'Introducción a la IA Responsable',
    resumen: 'Principios, prácticas y estudios de caso.',
    palabrasClave: ['IA', 'ética', 'responsabilidad'],
    estado: 'PUBLICADO',
    version: 3,
    meta: { isbn: '978-3-16-148410-0', doi: '', categoria: 'Ciencias', licencia: 'CC-BY-SA' },
    autorPrincipal: 'Ana Autora',
    coautores: ['María Pérez', 'Juan Díaz'],
    libro: {
      capitulos: [
        { numero: 1, titulo: 'Fundamentos', resumen: 'Conceptos básicos.'},
        { numero: 2, titulo: 'Sesgos', resumen: 'Tipos y mitigación.'}
      ]
    },
    historial: [
      { fecha: Date.now()-86400000*20, accion: 'Aprobado v2' },
      { fecha: Date.now()-86400000*10, accion: 'Publicado v3' }
    ],
    asignados: { revisores: ['Rafa Revisor'] }
  },
  {
    id: 'p3',
    tipo: 'articulo',
    titulo: 'Aplicaciones de IoT en la Salud Pública',
    resumen: 'Explora cómo los dispositivos conectados mejoran la atención médica.',
    palabrasClave: ['IoT', 'salud', 'sensores'],
    estado: 'EN_REVISION',
    version: 2,
    meta: { isbn: '', doi: '10.1000/iot456', categoria: 'Salud', licencia: 'CC0' },
    autorPrincipal: 'Carlos Científico',
    coautores: ['Sofía Saludable'],
    articulo: {
      revistaObjetivo: 'HealthTech Journal',
      seccion: 'Case Study',
      referencias: ['WHO 2021', 'Chen et al. 2019'],
      figuras: ['fig2.png', 'fig3.png']
    },
    historial: [
      { fecha: Date.now()-86400000*5, accion: 'Subido EN_REVISION v2' }
    ],
    asignados: { revisores: ['Sofía Saludable'] }
  },
  {
    id: 'p4',
    tipo: 'libro',
    titulo: 'Humanidades Digitales: Una Perspectiva Crítica',
    resumen: 'Discute el impacto de la tecnología en las ciencias humanas.',
    palabrasClave: ['digital', 'humanidades', 'cultura'],
    estado: 'CAMBIOS_SOLICITADOS',
    version: 1,
    meta: { isbn: '978-1-23-456789-0', doi: '', categoria: 'Humanidades', licencia: 'CC-BY' },
    autorPrincipal: 'Valeria Visionaria',
    coautores: ['Tomás Técnico'],
    libro: {
      capitulos: [
        { numero: 1, titulo: 'Tecnología y Cultura', resumen: 'Relación entre medios digitales y sociedad.' },
        { numero: 2, titulo: 'Archivos Digitales', resumen: 'Preservación y acceso.' }
      ]
    },
    historial: [
      { fecha: Date.now()-86400000*3, accion: 'Revisión solicitada v1' }
    ],
    asignados: { revisores: ['Rafa Revisor'] }
  },
  {
    id: 'p4',
    tipo: 'libro',
    titulo: 'Humanidades Digitales: Una Perspectiva Crítica',
    resumen: 'Discute el impacto de la tecnología en las ciencias humanas.',
    palabrasClave: ['digital', 'humanidades', 'cultura'],
    estado: 'CAMBIOS_SOLICITADOS',
    version: 1,
    meta: { isbn: '978-1-23-456789-0', doi: '', categoria: 'Humanidades', licencia: 'CC-BY' },
    autorPrincipal: 'Valeria Visionaria',
    coautores: ['Tomás Técnico'],
    libro: {
      capitulos: [
        { numero: 1, titulo: 'Tecnología y Cultura', resumen: 'Relación entre medios digitales y sociedad.' },
        { numero: 2, titulo: 'Archivos Digitales', resumen: 'Preservación y acceso.' }
      ]
    },
    historial: [
      { fecha: Date.now()-86400000*3, accion: 'Revisión solicitada v1' }
    ],
    asignados: { revisores: ['Rafa Revisor'] }
  }
]
