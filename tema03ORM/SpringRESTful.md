# Introducción a Spring

Con el siguiente ejercicio vamos a implementar como servicio REST un CRUD con Spring contra una base de datos MySQL. Se trata de una prueba de concepto (PoC) para demostrar cómo crear con anotaciones @Autowiring un servicio REST con Spring e Hibernate.

También veremos cómo solucionar el problema que el JSON generado sea recursivo (entidades que referencian a entidades).

## Paso 1: Crear la Base de Datos

Para completar el servicio, necesitamos tener instalado MySQL o MariaDB como servicios en el sistema operativo o bien usamos un contenedor docker. En esta guía sólo detallaremos cómo hacer un contenedor por ser más complicado.

### Creación con Docker del contenedor

Usamos la configuración que [nos explican en la Web oficial](https://hub.docker.com/_/mysql).

Vamos a exponer el servicio MySQL (puerto 3306 del contenedor) en el puerto 33306 de la máquina física. Nos ayudaremos de Adminer (puerto 8080 del contenedor) para disponer de una sencilla interfaz Web para poder interactuar con la base de datos fácilmente, [puerto 8181 de la máquina física](http://localhost:8181). Además vamos a *pre-*cargar la base de datos desde el fichero bbdd.sql. Para ello creamos el fichero stack.yml:

```yaml
# Use root/example as user/password credentials
version: '3.1'

services:

  db:
    image: mysql
    command: --default-authentication-plugin=mysql_native_password
    restart: "no"
    volumes:
      - ./bbdd.sql:/docker-entrypoint-initdb.d/bbdd.sql
    environment:
      MYSQL_ROOT_PASSWORD: example
    ports:
      - 33306:3306

  adminer:
    image: adminer
    restart: "no"
    ports:
      - 8181:8080

```
Creamos la BBDD "zapateria" desde línea de comandos, desde el IDE o desde PHPmyadmin.
Lanzamos el contenedor:

```bash
 $ sudo docker-compose -f stack.yml up
```


## Paso 2: Conectar desde el IDE a la BBDD (crear la conexión)

Con [IntelliJ IDEA](), pestaña "Servicios", botón derecho en "Databases" y creamos una 
nueva conexión. 

En cadena de conexión ponemos: **jdbc:mysql://localhost:33306/gestion_reservas** para 
forzar el driver MySQL.

Recuerda que el diagrama ER que estamos usando en clase es éste:

![Diagrama Entidad Relación de la Gestión de Reservas](docs/diagramaER.jpg)

### Paso 3: Creamos el proyecto

#### Usando este repo

Clonamos este repositorio y configuramos la conexión modificando
"src/main/resources/application.properties" con nuestro usuario y contraseña:

```java
spring.datasource.url = jdbc:mariadb://localhost:33306/gestion_reservas
spring.datasource.username = root
spring.datasource.password = example
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.ddl-auto = update
spring.session.store-type=jdbc
spring.session.jdbc.initialize-schema=always
```

#### Creando el proyecto desde cero: 

Visitar la página [Spring Initializr](start.spring.io) y creamos un proyecto.

![Generando un proyecto Spring](docs/start-pring-io.png "Generar el proyecto en Spring Initializr")

### Paso 4: Generar las entidades nuevas desde la BBDD

Aunque nosotros vamos a usar Lombok para ver cómo crear rápidamente las clases entidad, también es posible generarlas desde la base de datos. 

Para generar automáticamente desde la base de datos los POJO, abrimos la ventana *"Database"* del IDE, creamos la conexión tal y como vimos en el **Paso 2**, luego buscamos la tabla para la que queramos generar su POJO (Plain Old Java Object) y le indicamos en el paquete que queremos que se genere.

![Generar POJOs desde la base de datos](docs/generacionPOJOs.png "Generando clases entidad desde el IDE")

Recuerda que tenemos que estar conectados a la base de datos para poder generar las clases entidad.


#### Lombok

Una alternativa a generar las entidades desde la BBDD es usar [Lombok]().

### Paso 5: Modificar las clases entidad para evitar referencias recursivas en JSON

Modificando la aplicación Spring: para conseguir que se genere JSON donde unas entidades aniden a otras, tenemos que añadir **@EnableJpaRepositories(considerNestedRepositories = true)** a nuestra aplicación.

```java
package com.iesvdc.acceso.reservas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(considerNestedRepositories = true)
public class ReservasApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReservasApplication.class, args);
    }

}
```

Para evitar que las entidades se aniden infinitamente, hay que indicarlo en las clases entidad (POJOs)  mediante las anotaciones **JsonManagedReference** y **JsonBackReference**.

![Entidad padre en el JSON](docs/jsonManagedReference.png "Generando clases entidad desde el IDE")

![Entidad hija en el JSON](docs/jsonBackReference.png "Generando clases entidad desde el IDE")

### Paso 6: Crear las interfaces de los distintos repositorios

Para cada uno de nuestras clases entidad, tenemos que generar las interfaces para los repositorios. Veamos un ejemplo clásico de un cliente que puede tener varias direcciones y cómo podríamos definir en el controlador todas las rutas que le afecten.

**Fichero RepositorioClientes.java**
```java
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioClientes extends JpaRepository<Cliente, Long> {
    
}
```

Gracias al **"autowiring"** de Spring, automáticamente estarán disponibles 
las consultas predefidas en Cliente (findById, findAll, findByNombre, findByApellidos....):

**Fichero Cliente.java**


```java
@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cliente.findAll", query = "SELECT c FROM Cliente c")
    , @NamedQuery(name = "Cliente.findById", query = "SELECT c FROM Cliente c WHERE c.id = :id")
    , @NamedQuery(name = "Cliente.findByNombre", query = "SELECT c FROM Cliente c WHERE c.nombre = :nombre")
    , @NamedQuery(name = "Cliente.findByApellidos", query = "SELECT c FROM Cliente c WHERE c.apellidos = :apellidos")
    , @NamedQuery(name = "Cliente.findByDni", query = "SELECT c FROM Cliente c WHERE c.dni = :dni")})
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(nullable = false, length = 40)
    private String nombre;
    @Basic(optional = false)
    @Column(nullable = false, length = 100)
    private String apellidos;
    @Basic(optional = false)
    @Column(nullable = false)
    private int dni;
    
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCliente")
    @JsonManagedReference
    private List<ClienteDireccion> clienteDireccionList;
```

### Paso 7: Crear el controlador

Vamos a implementar las siguientes rutas y verbos HTTP:


|	Descripción 							| Verbo HTTP 	| ruta						| 
|---										|--- 			|--- 						|
| Listar todos los clientes					| GET			| /rest/cliente				|
| Ver el detalle de un cliente				| GET			| /rest/cliente/{id}		|
| Crear un nuevo cliente					| POST			| /rest/cliente				|
| Borrar un  cliente						| DELETE		| /rest/cliente/{id}		|
| Listar todos los códigos postales 		| GET			| /rest/codpos  			|
| Añadir un nuevo código postal				| POST			| /rest/codpos				|
| Listar todas las direcciones de todos los clientes | GET	| /rest/cliente/direccion	|
| Listar todas las direcciones un cliente	| GET			| /rest/cliente/{idCli}/direccion		|
| Añadir una dirección a un cliente			| POST			| /rest/cliente/{idCli}/direccion		|
| Actualizar las direcciones de un cliente 	| PUT 			| /rest/cliente/{idCli}/direccion		|
| Añadir un código postal					| POST			| /rest/codpos				|
| Listar todos los códigos postales 		| GET			| /rest/codpos				|
| Buscar la localidad de un código postal 	| GET			| /rest/codpos/{id}			|
| Actualizar la localidad de un código		| PUT			| /rest/codpos/{id}			|
| Borrar un código postal					| DELETE		| /rest/codpos/{id}			|
```java

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/rest")
public class Controlador {
    
    @Autowired
    RepositorioClientes repoClient;
    
    @Autowired
    RepositorioCodPos repoCodPos;
    
    @Autowired
    RepositorioDireccion repoDirec;

   // Get All Clientes
    @GetMapping("/clientes")
    public List<Cliente> getAllAlumnos() {
        return repoClient.findAll();
    }
    
    // Get All CodPos
    @GetMapping("/codpos")
    public List<CodPos> getAllCodPos() {
        return repoCodPos.findAll();
    }
    
    // Get All ClienteDireccion
    @GetMapping("/direcciones")
    public List<ClienteDireccion> getAllDirecciones() {
        return repoDirec.findAll();
    }    
}
```

### Paso 8: Probar el servicio

Desde un navegador:

http://localhost:8080/rest/clientes


Desde línea de comandos, usando los tests (cambiamos al directorio del proyecto
y escribimos):

$ mvn -Dtest=GestionZapateriaApplicationTests test


### Paso 9: Crear el cliente Web que conecte al servicio

HTML5+JS+jQuery






## Problema de JSON anidado en Spring (relaciones)


## Sesiones, autenticación y mantenimiento del objeto en el tiempo

Un backend REST, por definición, no mantiene estados, es **stateless**, es decir, cada petición que hacemos al servidor no depende de la anterior. Por tanto el encargado de mantener el estado de la aplicación es el frontend.

Para identificarnos en cada petición, podemos optar por múltiples posibilidades:

1. Básica: El cliente se identifica enviando la cabecera "Authorization: Basic anVhbmd1OnNlY3JldG8xMjMK*, donde la cadena larga ilegible que ves es en realidad el par *juangu:secreto123* en Base64.
2. Digest: Un sistema algo más complejo que el anterior que usa algoritmos de cifrado MD5 para identificarse ante el backend.
3. Por token: Hoy día un ejemplo de éste muy extendido es JWT (JSON Web Token). El cliente se identifica una primera vez ante el servidor, quien genera un token que será válido para identificarse en futuras peticiones. 

![Autenticación por JSON Web Token](./docs/jwt.png)

Para más información sobre JWT [puedes descargar este libro](https://www.fomasgroup.com/Portals/0/MgmNewsDocuments/jwt-handbook.pdf).
