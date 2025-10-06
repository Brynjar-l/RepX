# 🏋️‍♂️ RepX — Gym Tracker Backend

**RepX** is a **Kotlin + Spring Boot** backend for a smart gym-tracking "logger" application.  
It allows users to create accounts, manage workouts, and log progress over time.  
Built for scalability and clean architecture — ready for web or mobile frontends in 2026.

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

2. **get .env file from discort channel:**
    ***and place it in the root of the app***

3. **Compile and Run:**
 ```bash
  mvn clean compile
  mvn spring-boot:run
  ```

## 🧪 API Testing with Postman:
  ```bash
  can set baseURL as http://localhost:8080/
  ```
  - Create User:
  Method: POST
  Endpoint: {{baseURL}}/api/users
  Body (JSON):
  {
    "email": "stimmi@repx.app",
    "password": "stimmi1234",
    "displayName": "STIMMI"
  }

  - list Users:
  Method: GET
  Endpoint: {{baseURL}}/api/users?page=0&size=20

  - User by ID:
  Method: GET
  Endpoint: {{baseURL}}/api/users/{id}

  - Delete User:
  Method: DELETE
  Endpoint: {{baseURL}}/api/users/{id}

## 👤 Author:
- Brynjar, brs87@hi.is
- Halla, hts11@hi.is
- Ignas, igp4@hi.is
- Styrmir, soa61@hi.is



# Project Milestones and Responsibilities:
## `Inception`    —    [no P.O needed]
Vision and use case document, project skeleton (on a VCS, e.g. Github), and project plan.
When: Due on the __7th of September__

## `Elaboration`    —   __[Brynjar, brs87@hi.is]__
Behaviour model, Design model, and first code deliverables.
When: Due on the __28th of Septembe__r

## `Construction 1. `   —    __[Styrmir, soa61@hi.is]__
Second code deliverables.
When: Due on the __19th of October__

## `Construction 2.`    —   __[Halla, hts11@hi.is]__
Third code deliverables.
When: Due on the __9th of November__

## `Transition`    —    __[Ignas, igp4@hi.is]__
Final code deliverables, final slides and project turn-in.
When: Due on the __23th of November__
---

## 👨🏻‍💻 Meetings:
**Teymi 21. fundar með aðstoðarkennara á fimmtudögum kl. 10:30 - 10:50, í N-120 (Askja)**
