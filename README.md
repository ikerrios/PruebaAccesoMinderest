# PruebaAccesoMinderest

Aplicación de consola en **Java** para gestionar **Clientes**, **Productos** y **Equivalencias** entre productos usando **MySQL** y acceso a datos con **DAO (JDBC)**.

---

## Qué hace este proyecto

Desde un menú por consola permite:

- **Alta de producto** para un cliente (por código)
- **Establecer equivalencia** entre 2 productos (obligatorio: productos de **clientes distintos**)
- **Buscar / ver equivalencias** de un producto

Además, antes de establecer equivalencia se listan en consola **CLIENTS** y **PRODUCTS** con encabezados para poder elegir sin entrar a MySQL.

---

## Tecnologías

- Java **21**
- Maven
- MySQL (o MariaDB)
- JDBC (DAO)

---

## Estructura del proyecto

```text
PruebaAccesoMinderest/
├─ README.md
└─ productos/
   ├─ pom.xml
   ├─ mvnw
   ├─ mvnw.cmd
   ├─ HELP.md
   ├─ .gitignore
   ├─ sql/
   │  ├─ schema.sql
   │  └─ data.sql
   └─ src/
      └─ main/
         ├─ java/
         │  └─ com/
         │     └─ gestion/
         │        └─ productos/
         │           ├─ app/
         │           │  └─ Main.java
         │           ├─ dao/
         │           │  ├─ ClientDAO.java
         │           │  ├─ ProductDAO.java
         │           │  └─ EquivalenceDAO.java
         │           ├─ model/
         │           │  ├─ Client.java
         │           │  └─ Product.java
         │           ├─ services/
         │           │  └─ Service.java
         │           └─ util/
         │              └─ DB.java
         └─ resources/
            └─ database.properties
```
## Base de datos

### Tablas

El esquema está en `productos/sql/schema.sql` y crea:

- `CLIENTS`
- `PRODUCTS`
- `EQUIVALENCES`

**Detalles importantes:**

- En `PRODUCTS` existe `UNIQUE (CLIENT_ID, NAME)` para evitar productos repetidos dentro del mismo cliente.
- En `EQUIVALENCES` existe `UNIQUE (PRODUCT_ID_A, PRODUCT_ID_B)` para evitar duplicados.

### Datos de ejemplo

`productos/sql/data.sql` inserta:

- **10 clientes**
- **40 productos**
- **2 equivalencias** ya creadas (para poder probar el listado desde el minuto 1)

---

## Configuración de conexión

La conexión JDBC está centralizada en:

- `productos/src/main/java/com/gestion/productos/util/DB.java`

Valores por defecto:

- Host: `localhost`
- Puerto: `3306`
- BD: `MINDEREST`
- Usuario: `user_minderest`
- Password: `@minderest1412`

Si tu MySQL usa otra contraseña o usuario, cambia esas constantes en `DB.java`.

---

## Cómo preparar la BD (rápido)

1. Abre MySQL Workbench (o consola)
2. Ejecuta primero:
   - `productos/sql/schema.sql`
3. Ejecuta después:
   - `productos/sql/data.sql`

Con esto ya tienes la base de datos preparada y con datos.

---

## Ejecución

### Recomendado: desde el IDE

1. Importa el proyecto en IntelliJ / Eclipse / VS Code
2. Asegúrate de que MySQL está encendido y la BD `MINDEREST` está creada y poblada
3. Ejecuta:

```text
productos/src/main/java/com/gestion/productos/app/Main.java
```
## Menú de consola

Opciones actuales del menú:

- **1. Alta producto**
- **2. Establecer equivalencia**
- **3. Ver equivalencias de un producto**
- **0. Salir**

En la opción **2**, antes de pedir datos, se imprimen:

- Listado de **CLIENTS** (con encabezados)
- Listado de **PRODUCTS** (con encabezados)

---

## Notas de diseño

- **DAO**: contienen el SQL y el mapeo a objetos (`Client`, `Product`). No imprimen por consola.
- **Service**: valida datos y aplica reglas (por ejemplo: clientes distintos y evitar equivalencias duplicadas).
- **Main**: menú, inputs por consola y salida formateada.

---

## Nota extra

Es un proyecto Java con Maven y JDBC. Está inicializado con Spring Initializr por estructura, pero toda la lógica está implementada sin frameworks para demostrar mi conocimiento de JDBC, DAOs y separación de capas.

---
## Autor

- Iker Ríos