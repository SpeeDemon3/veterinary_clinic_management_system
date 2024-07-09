# veterinary_clinic_management_system
- This project is a system for managing a veterinary clinic of users that allows the management of pets, roles, profiles, pets and notifications. It is built using Spring Boot and Hibernate for data persistence.
- Este proyecto es un sistema para la gestión de una clínica veterinaria de usuarios que permite la gestión de mascotas, roles, perfiles, mascotas y notificaciones. Está construido utilizando Spring Boot y Hibernate para la persistencia de datos.

## Characteristics / Características

- Register and login users. / Registro e inicio de sesión de usuarios.
- User role management. / Gestión de roles de usuario.
- User profile management. / Gestión de perfiles de usuario.
- Management of pets associated with users. / Gestión de mascotas asociadas a usuarios.
- Notifications for users. / Notificaciones para usuarios.

## Used technology / Tecnologías Utilizadas

- Java
- Spring Boot
- Spring Security
- Hibernate
- ModelMapper
- Lombok
- Jakarta Persistence API (JPA)
<<<<<<< HEAD
- Base de datos relacional (MySQL)
=======
- Relational database (e.g. MySQL, PostgreSQL) / Base de datos relacional (por ejemplo, MySQL, PostgreSQL)
- Non-relational database (e.g. MongoDb) / Base de datos no relacional (por ejemplo, MongoDb)
- Kafka
>>>>>>> origin/v1.0.1

## Previous requirements / Requisitos Previos

- Java 11 or higher / Java 11 o superior
- Maven
- Relational database (configured in `application.properties`) / Base de datos relacional (configurada en `application.properties`)

## Installation / Instalación

1. Clone the repository / Clona el repositorio:

   `https://github.com/SpeeDemon3/veterinary_clinic_management_system.git` 


2. Navigate to the project directory / Navega al directorio del proyecto:

*   cd veterinary_clinic_management_system

3. Build the project using Maven / Compila el proyecto utilizando Maven:

* mvn clean install

4. Configure the database in the src/main/resources/application.properties file / Configura la base de datos en el archivo: 
 
    `src/main/resources/application.properties`


5. Run the application / Ejecuta la aplicación:

* mvn spring-boot:run

## Endpoints Principales

- User register / Registro de Usuario: `Post /api/user/signup`
  - Request body / Cuerpo de la solicitud: `SignUpRequest`

- Login / Inicio de Sesión: `POST /api/user/login`
  - Cuerpo de la solicitud: `LoginRequest`

- Crear Mascota: `POST /api/pet/add/{ownerId}`
  - Request body / Cuerpo de la solicitud: `PetRequest` 
  
## Example of Request to Register User / Ejemplo de Solicitud para Registrar Usuario
POST /api/user/signup
`{
"name": "John Cube",
"email": "johncube@example.com",
"password": "p@ssword123"
}`

## Sample Application to Register a Pet / Ejemplo de Solicitud para Registrar una Mascota

POST /api/pet/add
`{
"identificationCode": "123ABC",
"name": "Tango",
"description": "Friendly dog",
"vaccinationData": "Up to date",
"img": "image_url",
"birthdate": "2020-01-01",
"medication": "None"
}`

## Contribution / Contribución
1. Fork the project. / Haz un fork del proyecto.
2. Create a new branch (git checkout -b feature/new-feature). / Crea una nueva rama (git checkout -b feature/nueva-caracteristica).
3. Make your changes and commit (git commit -am 'Add new feature'). / Realiza tus cambios y haz commit (git commit -am 'Agrega nueva característica').
4. Push to the branch (git push origin feature/new-feature). / Haz push a la rama (git push origin feature/nueva-caracteristica).
5. Open a Pull Request. / Abre un Pull Request.

## Licencia
This project is licensed under the GPL-3.0 license. See the LICENSE file for more details. / Este proyecto está licenciado bajo la Licencia GPL-3.0 license. Ver el archivo LICENSE para más detalles.
