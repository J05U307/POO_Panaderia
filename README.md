# 🍞 Bakery Management System – Sistema de Gestión para Panadería (Java POO)

Sistema de escritorio desarrollado en **Java Puro (POO)** como proyecto final académico.  
Gestiona pedidos, ventas, usuarios y productos aplicando correctamente principios de **Programación Orientada a Objetos**, incluyendo **herencia, agregación, composición y asociaciones** entre múltiples entidades.

El objetivo del sistema es administrar de forma eficiente una panadería mediante un flujo realista que incluye roles de usuario, pedidos, facturación, reportes y control de inventario.

---

## 🚀 Tecnologías y Librerías Utilizadas

- **Java 8+**
- **JCalendar** – selección de fechas
- **iText** – generación automática de **PDF** (boletas, facturas, reportes)
- **JFreeChart** – gráficos estadísticos (ventas, productos más vendidos)
- **JDBC** – conexión con base de datos
- **Swing** – interfaz gráfica de usuario

---

## 🧱 Arquitectura y Diseño

El proyecto implementa los pilares de la POO:

- **Encapsulamiento**
- **Abstracción**
- **Herencia**
- **Polimorfismo**

Y además relaciones entre clases:

- **Asociación**
- **Agregación**
- **Composición**
- **Herencia**

### 📌 Entidades principales (12)

- **Categoria**
- **Producto**
- **DetalleVenta**
- **Pedido**
- **Cliente**
- **Usuario**
- **Rol**
- **Empleado**
- **TipoPago**
- **Comprobante**
- **Factura**
- **Boleta**

Cada entidad incluye atributos, comportamientos y relaciones modeladas de acuerdo a un escenario real.

---

## 🔐 Roles de Usuario y Permisos

El sistema implementa **control de acceso por roles**, permitiendo un flujo realista de trabajo:

### 👨‍🍳 **Administrador**
- Acceso total al sistema  
- Gestión de usuarios, roles, empleados  
- Administración de inventario y productos  
- Consulta de reportes, ventas, gráficos y comprobantes  

### 🛍️ **Vendedor**
- Registro de pedidos  
- Gestión básica de clientes  
- Visualización limitada de productos disponibles  

### 💵 **Cajero**
- Procesamiento de pagos  
- Emisión de **Boletas** y **Facturas** (PDF con iText)  
- Registro de ventas diarias  

---

## 🔄 Flujo Principal del Sistema

1. El **Vendedor** registra un pedido.
2. El pedido pasa al **Cajero**, quien genera el cobro.
3. El sistema emite un comprobante (Boleta o Factura) en **PDF** usando *iText*.
4. El **Administrador** puede revisar reportes, ventas y estadísticas generadas con *JFreeChart*.

---

## 📊 Funcionalidades destacadas

✔ CRUD completo de todas las entidades  
✔ Generación de **boletas y facturas en PDF**  
✔ Reportes estadísticos con **gráficos (JFreeChart)**  
✔ Validación de datos y manejo de excepciones  
✔ Módulo de login con roles y permisos  
✔ Control de inventario  
✔ Registro y gestión de pedidos  
✔ Ventas con detalle y métodos de pago  


## 👨‍💻 Autor

**Josue Cusquisiban**  
