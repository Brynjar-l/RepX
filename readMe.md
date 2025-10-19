# 🏋️‍♂️ RepX — Workout Logger: Backend

**RepX** is a **Kotlin + Spring Boot** backend for a smart workout logging application.  
It allows users to create accounts, manage workouts, and log progress over time.  
Built for scalability and clean architecture; ready for web or mobile frontends in 2026.

---

## 🚀 Tech Stack:

| Layer | Technology |
|-------|-------------|
| Language | Kotlin |
| Framework | Spring Boot 3 |
| Database | PostgreSQL 17 |
| ORM | Spring Data JPA + Hibernate |
| Migration | Flyway |
| Security | BCrypt (password hashing) |
| Build Tool | Maven |

---

## 🏃 How to Run

1. **Clone the repository:**
```bash
git clone https://github.com/Brynjar-l/RepX.git
cd RepX
```

2. **Get the `.env` file from discort channel:** ***(and place it in the projects root dir)***

3. **Compile and Run:**
 ```bash
mvn clean compile
mvn spring-boot:run
  ```

## 🧪 API Testing with Postman:

Can set baseURL as `http://localhost:8080/`

### `Create User`: </br>
  Method: **POST** </br>
  Endpoint: {{**baseURL**}}/api/users </br>
    Body (JSON): {</br>
      &nbsp;&nbsp;"email": "stimmi@repx.app",</br>
      &nbsp;&nbsp;"password": "stimmi1234",</br>
      &nbsp;&nbsp;"displayName": "STIMMI"
    </br>}

### `list Users`: </br>
  Method: **GET** </br>
  Endpoint: {{**baseURL**}}/api/users?page=0&size=20

### `User by ID`: </br>
  Method: **GET** </br>
  Endpoint: {{**baseURL**}}/api/users/{id}

### `Delete User`:
  Method: **DELETE** </br>
  Endpoint: {{**baseURL**}}/api/users/{id}

## Contributors:
#### **Brynjar**: 
  - HÍ: [brs87@hi.is](mailto:brs87@hi.is)
  - Github: [Brynjar-l](https://github.com/)
#### **Halla**:
  - HÍ: [hts11@hi.is](mailto:hts11@hi.is)
  - Github: [x](https://github.com/)
#### **Ignas**:
  - HÍ: [igp4@hi.is](mailto:igp4@hi.is)
  - Github: [y](https://github.com/)
#### **Styrmir**:
  - HÍ: [soa61@hi.is](mailto:soa61@hi.is)
  - Github: [z](https://github.com/)


## Project Milestones and Responsibilities:

### `Inception`
- **Deliverables**: Vision and use case document, project skeleton (on a VCS, e.g. Github), and project plan.


### `Elaboration`
- **P.O**: [Brynjar](mailto:brs87@hi.is)
- **Deliverables**: Behaviour model, Design model, and first code deliverables.

### `Construction 1.`
- **P.O:** [Styrmir](mailto:soa61@hi.is)
- **Deliverables**: Second code deliverables.

### `Construction 2.`
- **P.O**: [Halla](mailto:hts11@hi.is)
- **Deliverables**: Third code deliverables.

### `Transition`
- **P.O**: [Ignas](mailto:igp4@hi.is)
- **Deliverables**: Final code deliverables, final slides and project turn-in.
