# 💼 Financial API - Prueba Técnica (TrinityFs)

> API REST desarrollada como evaluación técnica para el proceso de selección en TrinityFs (Neiva).

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.5-6DB33F?style=flat-square&logo=spring)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-336791?style=flat-square&logo=postgresql)
![MapStruct](https://img.shields.io/badge/MapStruct-1.6.3-F6D046?style=flat-square)

## 📌 Contexto del Proyecto

Este repositorio contiene la solución a la prueba técnica requerida por **TrinityFs**. El objetivo principal fue construir una API robusta y escalable para la gestión de datos financieros, demostrando buenas prácticas de desarrollo en el ecosistema Java y Spring Boot.

El enfoque estuvo puesto en la limpieza del código, la correcta separación de responsabilidades y el manejo adecuado de las excepciones.

## 🏗️ Arquitectura y Patrones de Diseño

La aplicación sigue una **Arquitectura en Capas (N-Tier Architecture)**, separando claramente las responsabilidades para facilitar el mantenimiento y la escalabilidad del código.

La estructura del proyecto se divide en:
- `controller/`: Exposición de endpoints REST.
- `service/`: Lógica de negocio principal.
- `repository/`: Capa de persistencia usando Spring Data JPA.
- `entity/`: Modelos de base de datos.
- `dto/`: Objetos de Transferencia de Datos.
- `mapper/`: Transformación de entidades a DTOs utilizando MapStruct.
- `exception/`: Manejo global de excepciones (GlobalExceptionHandler) usando `@ControllerAdvice`.

### Decisiones Técnicas Destacadas

1. **Uso del Patrón DTO:** Se implementaron *Data Transfer Objects (DTO)* para asegurar que el contrato de la API no quede acoplado directamente al esquema de la base de datos (Entidades).
2. **MapStruct:** Para evitar el código "boilerplate" (código repetitivo) de mapeo manual entre Entidades y DTOs, se integró **MapStruct**, generando los mapeadores en tiempo de compilación para un rendimiento óptimo.
3. **Manejo Centralizado de Excepciones:** Se implementó una respuesta estandarizada para los errores (400 Bad Request, 404 Not Found, etc.) mejorando la experiencia del cliente que consume la API.
4. **Validación de Datos:** Uso de `spring-boot-starter-validation` (Jakarta Validation) para validar los payloads de entrada antes de que lleguen a la lógica de negocio.

## 🚀 Guía de Instalación (Setup)

### Prerrequisitos
- Java 21 instalado (`java -version`).
- PostgreSQL instalado y corriendo.
- Maven (Opcional, el proyecto incluye un *wrapper*).

### Pasos para ejecutar

1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/Chatesito/Prueba-Tecnica.git
   cd Prueba-Tecnica
   ```

2. **Configuración de la Base de Datos:**
   Crea una base de datos local en PostgreSQL (por ejemplo, `financial_db`). Luego, configura tus credenciales en el archivo `src/main/resources/application.properties` (o `application.yml`):
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/financial_db
   spring.datasource.username=tu_usuario
   spring.datasource.password=tu_contraseña
   
   # Opcional para desarrollo:
   spring.jpa.hibernate.ddl-auto=update
   ```

3. **Compilar y Ejecutar:**
   Utiliza el wrapper de Maven provisto en el repositorio para descargar las dependencias y levantar el servidor:
   ```bash
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```

La API estará disponible en `http://localhost:8080`.

---
*Desarrollado por Juan David Rivera Chate.*
