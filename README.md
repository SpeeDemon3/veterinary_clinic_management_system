# veterinary_clinic_management_system
# User Notification System

Este proyecto es un sistema para la gestión de una clínica veterinaria de usuarios que permite la gestión de usuarios, roles, perfiles, mascotas y notificaciones. Está construido utilizando Spring Boot y Hibernate para la persistencia de datos.

## Características

- Registro e inicio de sesión de usuarios.
- Gestión de roles de usuario.
- Gestión de perfiles de usuario.
- Gestión de mascotas asociadas a usuarios.
- Notificaciones para usuarios.

## Tecnologías Utilizadas

- Java
- Spring Boot
- Spring Security
- Hibernate
- ModelMapper
- Lombok
- Jakarta Persistence API (JPA)
- Base de datos relacional (por ejemplo, MySQL, PostgreSQL)

## Requisitos Previos

- Java 11 o superior
- Maven
- Base de datos relacional (configurada en `application.properties`)

## Instalación

1. Clona el repositorio:

   `https://github.com/SpeeDemon3/veterinary_clinic_management_system.git` 

2. Navega al directorio del proyecto:

*   cd veterinary_clinic_management_system

3. Compila el proyecto utilizando Maven:

* mvn clean install

4. Configura la base de datos en el archivo src/main/resources/application.properties

5. Ejecuta la aplicación:

* mvn spring-boot:run

## Endpoints Principales

- Registro de Usuario: `Post /signup`
  - Cuerpo de la solicitud: `SignUpRequest`

- Inicio de Sesión: `POST /login`
  - Cuerpo de la solicitud: `LoginRequest`

- Crear Mascota: POST `/add/{ownerId}`
  - Cuerpo de la solicitud: `PetRequest` 
  
## Ejemplo de Solicitud para Registrar Usuario
POST /signup
{
"name": "John Cube",
"email": "johncube@example.com",
"password": "p@ssword123"
}

POST /users/1/pets
{
"identificationCode": "123ABC",
"name": "Tango",
"description": "Friendly dog",
"vaccinationData": "Up to date",
"img": "image_url",
"birthdate": "2020-01-01",
"medication": "None"
}

## Contribución
1. Haz un fork del proyecto.
2. Crea una nueva rama (git checkout -b feature/nueva-caracteristica).
3. Realiza tus cambios y haz commit (git commit -am 'Agrega nueva característica').
4. Haz push a la rama (git push origin feature/nueva-caracteristica).
5. Abre un Pull Request.

## Licencia
Este proyecto está licenciado bajo la Licencia GPL-3.0 license. Ver el archivo LICENSE para más detalles.
