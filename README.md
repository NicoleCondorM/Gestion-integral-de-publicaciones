🏛️ Gestion integral de publicaciones 📚
---
![Logo de Publicaciones](https://blogger.googleusercontent.com/img/a/AVvXsEhV9C-Q_vvaxDASnwhol73P6HqCdq5jmd3jbSwg7rkSMVjXLJGWdQplPF9CJV4c_4eY9pdl9SizHmIVB-hrH4dpVP6lO4rjVp1cZWMqZ94wuE354I4I1z7imDbije_a-YcdePBO_ilvmhC4cGn6nVaJ4ydsLHM9cL52jMJRkNROkPu5EtGUhbfbG39cQ8U)

Proyecto final - Arrquitectura Software

👤 Autores
- Andrade Eduardo
- Condor Nicole
- Cuji Martha


## 🚀 Tecnologías utilizadas

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![React](https://img.shields.io/badge/React-61DAFB?style=for-the-badge&logo=react&logoColor=white)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-FF6600?style=for-the-badge&logo=rabbitmq&logoColor=white)
![CockroachDB](https://img.shields.io/badge/CockroachDB-6933FF?style=for-the-badge&logo=cockroachlabs&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Eureka](https://img.shields.io/badge/Eureka-6DB33F?style=for-the-badge&logo=spring&logoColor=white)

---

## 📚 Tabla de contenidos

- [Descripción](#descripción)
- [Instalación](#instalación)
- [Funcionamiento](#Funcionamiento)

---

- ## 🧾 Descripción

Este proyecto implementa microservicios destinados para una institucion con la necesidad de gestionar el ciclo de vida de publicaciones academicas y editoriales elaboradas por autores registrados. El sistema tiene la capacidad de ejecutar operaciones **CRUD** de cada uno de los servicios que los usuarios tiene acceso, es decir tienen acceso a crear, editar publicaciones, flujo de revision colaborativa, control de cambios y publicacion final a un catalogo.
Nuestro proyecto cuenta con descubrimiento dinamico de servicios (Eureka), exposicion unificada mediante API Gateway, comunicacion sıncrona (REST/Feign) y asıncrona (RabbitMQ), base de datos distribuida con CockroachDB para garantizar alta disponibilidad y tolerancia a fallos, y un microservicio de notificaciones para alertas de eventos clave (inicio de sesion, nuevas publicaciones disponibles, registros de usuarios, estados de revision).

- ## ⚙️ Instalación

### Clonar el proyecto
- Ingresamos a GitHub Desktop y le damos clik en clonar repositorio
- Seleccionamos la opcion de URL
- Pegamos este URL : https://github.com/NicoleCondorM/Gestion-integral-de-publicaciones.git
- Seleccionamos donde queremos guardar
- Desde un IDE abrimos la carpeta
![VS Code](https://img.shields.io/badge/VS%20Code-007ACC?style=for-the-badge&logo=visual-studio-code&logoColor=white)
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ%20IDEA-000000?style=for-the-badge&logo=intellij-idea&logoColor=white)

### Ejecutar docker
docker-compose up -d

### Arrancar microservicios
- **Eureka Server**

cd ms-eureka-server

mvn spring-boot:run
- **Auth Server**

cd ms-auth-server

mvn spring-boot:run
- **API Publicaciones**
  
cd api-publicaciones

mvn spring-boot:run
- **API Gateway**
  
cd ms-api-gateway

mvn spring-boot:run
- **Frontend**
  
cd frontend-microservices-client

npm start








