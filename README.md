# 🌤️ The Weatheroo App

A high-performance Spring Boot REST API that orchestrates the synchronization of historical weather data across six continents, providing sophisticated visualization-ready averages.

## 🚀 Tech Stack
* **Java 21**
* **Spring Boot 3.4.x** (Web, Data JPA, Scheduling, Validation)
* **PostgreSQL** (Production Database)
* **H2 Database** (Unit & Integration Testing)
* **Docker & Docker Compose** (Containerization)
* **Lombok** (Boilerplate reduction)
* **WeatherAPI** (External Data Source)

---

## 🏗️ Architectural Highlights
This project demonstrates several advanced Spring Boot and System Design patterns:
* **Orchestrator-Worker Pattern**: Prevents connection pool exhaustion by separating long-running `@Async` orchestration from granular, short-lived `@Transactional` database operations.
* **Idempotent Synchronization**: Implemented "check-before-insert" logic to ensure no duplicate records are created, even during overlapping manual and scheduled syncs.
* **Windowed Data Fetching**: A sliding 30-day chunking algorithm ensures reliable historical data retrieval within external API payload constraints.
* **12-Factor App Configuration**: Fully externalized city mapping and rate-limit throttling via `application.properties` and Environment Variables.

---

## 📖 API Documentation & Access

Once the application is running, you can access the following interfaces:

* **🖥️ Frontend Dashboard**: [http://localhost:8080/](http://localhost:8080/)
* **📖 Interactive Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
* **📡 API Base Path**: `http://localhost:8080/api/v1/weather`

---

## 🛠️ Local Setup Instructions

### 1. Configure Environment Variables
The application requires a valid WeatherAPI key.
1. Create a free account at [WeatherAPI.com](https://www.weatherapi.com/).
2. In the root directory, create a `.env` file.
3. Add your key: `WEATHER_API_KEY=your_actual_key_here`.

### 2. Start Infrastructure
Ensure Docker is running, then execute:
```bash
docker-compose up -d