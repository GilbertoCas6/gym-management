# 🏋️‍♂️ Gym Management – Sistema de Gestión de Gimnasio

Proyecto desarrollado en **Spring Boot** con conexión a **MySQL**, que permite la gestión de usuarios y socios de un gimnasio.

---

## ⚙️ Tecnologías utilizadas
- Java 23  
- Spring Boot 3.5.6  
- Spring Data JPA  
- MySQL 8.0  
- Maven  
- IntelliJ IDEA  

---

## 📁 Estructura del Proyecto
El proyecto sigue una arquitectura **por capas**, organizada de la siguiente forma:

src/
├── main/
│ ├── java/com/itla/gym/gym_management/
│ │ ├── controlador/ → Controladores REST
│ │ ├── modelo/ → Entidades (JPA)
│ │ ├── repositorio/ → Interfaces Repository
│ │ ├── servicio/ → Lógica de negocio
│ │ └── GymManagementApplication.java
│ └── resources/
│ ├── application.properties
│ └── logback-spring.xml


---

## 🧠 Módulos implementados (Avance funcional)
### ✅ Módulo de Usuarios
- Permite registrar, listar y eliminar usuarios del sistema.  
- Campos principales: `id`, `nombre`, `email`, `rol`, `activo`.

### ✅ Módulo de Socios
- Permite registrar y administrar los socios activos del gimnasio.  
- Campos principales: `idUsuario`, `nombre`, `email`, `fechaIngreso`, `estado`, `objetivo`.

---

## 💾 Conexión con Base de Datos (MySQL)
Configura tu conexión en el archivo:

`src/main/resources/application.properties`
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/gimnasio
spring.datasource.username=root
spring.datasource.password=tu_contraseña
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true```

---
▶️ Ejecución del Proyecto

Clonar el repositorio:

git clone https://github.com/GilbertoCas6/gym-management.git


Abrir el proyecto en IntelliJ IDEA.

Ejecutar la clase GymManagementApplication.java.

El proyecto se iniciará en:
👉 http://localhost:8080

| Método | Endpoint         | Descripción              |
| :----: | :--------------- | :----------------------- |
|   GET  | `/usuarios`      | Lista todos los usuarios |
|  POST  | `/usuarios`      | Agrega un nuevo usuario  |
| DELETE | `/usuarios/{id}` | Elimina un usuario       |
|   GET  | `/socios`        | Lista todos los socios   |
|  POST  | `/socios`        | Agrega un nuevo socio    |
| DELETE | `/socios/{id}`   | Elimina un socio         |

👨‍💻 Autores

Gilberto Castillo C. – 
Gaudi Valera  
Eduardo Rodriguez 
Josairy Rosario  

Equipo de trabajo (4 integrantes) – Documentación técnica y pruebas

📘 Estado actual del proyecto

✔ Repositorio público con commits reales
✔ Conexión con base de datos operativa
✔ Módulos “Usuarios” y “Socios” funcionando correctamente
🚧 Próximos pasos: módulo de rutinas y control de pagos
