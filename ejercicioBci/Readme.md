# Read Me 
Aplicación Springboot para registro de usuarios.

Las tecnologías usadas fueron:
* Springboot
* JWT
* JPA
* H2

Algunas librerías extras fueron:

* Lombok
* Log4j

# Getting Started

En este proyecto se implementaron 3 API disponibles para su uso las cuales se listan a continuación con 
algunas de sus características

Todos los endpoints listados trabajan con ResponseEntity.

* localhost:8080/usuarios/registrar
  * No requiere autenticación JWT para funcionar.
  * Recibe un objeto UsuarioDTO en formato JSON
  * Responde un objeto RegistroResponse en formato JSON.
  

* localhost:8080/usuarios/modificar
  * Se debe tener un token valido para poder hacer modificaciones
  * Recibe un UsuarioDTO en formato JSON
  * Responde un objeto Usuario en formato JSON.
  
* localhost:8080/usuarios/buscar
  * Se debe tener un token valido para poder buscar usuarios
  * Recibe un UsuarioDTO en formato JSON
  * Responde un objeto Usuario en formato JSON.

# Cómo Probarlo?

Para poder hacer las pruebas correspondientes de este proyecto es necesario levantar la aplicación de forma 
local con springboot. La base de datos para este proyecto es en memoria utilizando H2 por lo que el contenido
después de detener el artefacto desaparecerá, existe una línea comentada en nuestro application.properties 
que se puede activar si se desea mantener la información en un archivo.

Para consumir estos servicios será necesario utilizar Postman.

en caso de no tenerlo adjunto el link: https://www.postman.com/downloads/

El token que se manejará para la seguridad debe ser ingresado en la pestaña Authorization y es de tipo "Bearer Token"


* Prueba de registrar:

La ruta para hacer las pruebas locales es: 
```agsl
localhost:8080/usuarios/registrar
```

Para registrar un usuario se debe hacer uso de la siguiente estructura:
```agsl

{
    "name": "Juan Rodrdiguez",
    "email": "correo3@dominio.cl",
    "password": "h2222222",
    "phones": [
        {
            "number": "12324567",
            "citycode": "1",
            "contrycode": "57"
        },
                {
            "number": "332223",
            "citycode": "12",
            "contrycode": "547"
        },
                {
            "number": "331212",
            "citycode": "12",
            "contrycode": "527"
        }
    ]
}
```
Se debe tener en cuenta que la contraseña debe ser de mínimo 5 caracteres y tener una letra y un número.

La respuesta de este servicio tendra una estructura como esta:

```agsl
{
    "id": "0d9c373a-b612-42bd-bb63-374b5602e6d8",
    "created": "2023-09-25T01:13:32.899784",
    "modified": "2023-09-25T01:13:32.899784",
    "last_login": "2023-09-25T01:13:32.899784",
    "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjb3JyZW8zQGRvbWluaW8uY2wiLCJyb2xlIjoidXNlciIsImV4cCI6MTY5NTYxODgxMiwiaWF0IjoxNjk1NjE1MjEyfQ.d0mUTQ0m1YBn6D7s_k5CPcMhbAJkRw39afp0ZsodC_4",
    "active": false
}
```
se debe conservar este token para la utilizacion de los siguientes endpoints.

* Prueba de Modificar

La ruta es: 
```
localhost:8080/usuarios/modificar
```
Se debe configurar el token generado en el endpoint anterior para realizar esta prueba.
El usuario a modificar debe ser el mismo que se almacena en el token.
Para realizar la prueba en el endpoint de modificar se puede usar la siguiente estructura:
```agsl
{

    "name": "NuevoNombre",
    "email": "correo2@dominio.cl",
    "password" : "nuevaPassword"
}
```
Para esta version de prueba solamente podremos modificar estos dos campos ya que el Email es diferenciador de usuario.

* Prueba de Buscar

La ruta es : 
```
localhost:8080/usuarios/buscar
```
Se debe configurar el token generado en el endpoint anterior para realizar esta prueba.

El request tiene la siguiente estructura:
```agsl
{
    "email": "correo3@dominio.cl"
}
```
Como señalamos anteriormente el correo es diferenciador de usuarios por lo cual es el campo que utilizaremos en la búsqueda
de un usuario en específico.

La respuesta de este servicio será el registro completo del usuario de esta manera:
```
{
    "id": "0ac455b6-69f1-45aa-947c-1685f6d36a6f",
    "name": "Juan Rodrdiguez",
    "email": "correo3@dominio.cl",
    "password": "hunter2",
    "phones": [
        {
            "id": 7,
            "number": "12324567",
            "cityCode": "1",
            "contryCode": "57"
        },
        {
            "id": 8,
            "number": "332223",
            "cityCode": "12",
            "contryCode": "547"
        },
        {
            "id": 9,
            "number": "331212",
            "cityCode": "12",
            "contryCode": "527"
        }
    ],
    "created": "2023-09-25T01:05:58.396735",
    "lastLogin": "2023-09-25T01:05:58.396735",
    "modified": "2023-09-25T01:05:58.396735",
    "role": "user",
    "active": false
}
```
con esto hacemos la prueba de la creacion de usuarios, modificación y muestra del usuario.



# Configuraciones adicionales
Se puede configurar la expresión regular para la contraseña en el aplication.properties, específicamente
en la variable 
```
regex.password
```
















