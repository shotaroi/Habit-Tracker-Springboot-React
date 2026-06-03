# Habit Tracker (Spring Boot + React)

Full-stack habit tracking app with a Spring Boot backend (JWT auth, PostgreSQL) and a React + Vite frontend.

## Tech Stack

- Backend: Java 21, Spring Boot, Spring Security, Spring Data JPA
- Frontend: React, TypeScript, Vite, Axios
- Database: PostgreSQL
- Build tools: Maven, npm

## Project Structure

- `backend/habittracker`: Spring Boot API
- `frontend`: React app

## Prerequisites

- Java 21+
- Maven 3.9+
- Node.js 20+ and npm
- PostgreSQL 14+

## Environment Setup

### 1) Database

Create a PostgreSQL database and user matching `backend/habittracker/src/main/resources/application.yml`:

- Database: `habit_tracker`
- Username: `habit_user`
- Password: `habit_pass`

You can adjust these values in `application.yml` if you want to use different credentials.

### 2) Backend Config

In `backend/habittracker/src/main/resources/application.yml`, review:

- `spring.datasource.url`
- `spring.datasource.username`
- `spring.datasource.password`
- `app.jwt.secret` (replace with a long random secret for real usage)
- `cors.allowed-origins` (default: `http://localhost:5173`)

### 3) Frontend Config

`frontend/.env` already defines:

```env
VITE_API_BASE_URL=http://localhost:8080
```

Change this if your backend runs on a different host/port.

## Run Locally

Open two terminals.

### Start Backend

```bash
cd backend/habittracker
mvn spring-boot:run
```

Backend runs on `http://localhost:8080`.

### Start Frontend

```bash
cd frontend
npm install
npm run dev
```

Frontend runs on `http://localhost:5173`.

## API Overview

Base URL: `http://localhost:8080`

- Auth
  - `POST /api/auth/register`
  - `POST /api/auth/login`
- Habits
  - `POST /api/habits`
  - `GET /api/habits`
  - `GET /api/habits/completed?date=YYYY-MM-DD`
  - `POST /api/habits/{habitId}/complete?date=YYYY-MM-DD`
  - `POST /api/habits/{habitId}/archive`
  - `GET /api/habits/archived`
  - `POST /api/habits/{habitId}/unarchive`
- Progress
  - `GET /api/progress/weekly?startDate=YYYY-MM-DD`

Most endpoints require a Bearer token from login/register.

## Useful Commands

### Backend

```bash
cd backend/habittracker
mvn test
```

### Frontend

```bash
cd frontend
npm run lint
npm run build
```
