# Medical Clinic-Project

This repository contains a Software Engineering university project following a full development pipeline, from requirements engineering and database modeling to a containerized network deployment. The project focuses on the digital recreation of a medical clinic's workflow, bridging the gap between complex healthcare logic and a scalable, Client-Server architecture.

## How does the application work?

The application is a distributed management system where patients and doctors interact through a secure TCP/IP socket connection. Unlike a simple local app, this project simulates a real-world environment where data persistence is handled by an ORM-mapped PostgreSQL database, ensuring that medical records, appointments, and treatments are stored reliably and accessible concurrently.

## Tech Stack & Tools

* **Logic & Backend:** Java 21 (JDK 21)
* **Database & Persistence:** PostgreSQL 16 managed through Hibernate 6 / Jakarta Persistence (JPA).
* **Communication:** Socket Programming (Multithreaded Server).
* **Infrastructure:** Docker & Docker Compose for full-stack orchestration.
* **Build Tool:** Maven.

## Features

* **Role-Based Access Control:** Users can register and login as either a **Patient** or a **Doctor**, each having a dedicated interface and set of permissions.
* **Patient Management:** * Book medical appointments with specific doctors.
    * View and edit personal appointment history.
* **Doctor Workflow:**
    * Manage assigned patients and provide medical treatments/recommendations.
    * Review and edit existing appointments to optimize the clinic's schedule.
* **Real-time Synchronization:** The Client-Server architecture ensures that all updates are immediately reflected in the central database.
* **Containerized Deployment:** The entire environment is ready to run with a single command.

## Architecture Overview

The project follows a distributed microservices-like pattern:
1.  **Server Module:** The central hub that processes business logic and interacts with the PostgreSQL container.
2.  **Client Module:** An interactive CLI tool that enables users to communicate with the server.
3.  **Database Container:** A dedicated SQL instance that ensures data integrity and persistence.

## How to run locally

### Prerequisites
* **Docker Desktop** installed and running.

### Instructions
1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/alexanghel17/Medical-Clinic-Project.git](https://github.com/alexanghel17/Medical-Clinic-Project.git)
    cd Medical-Clinic-Project
    ```
2.  **Start the system:**
    ```bash
    docker-compose up --build
    ```
3.  **Interact with the Client:**
    Open a new terminal and attach to the client container to start the interactive menu:
    ```bash
    docker attach clinica-client
    ```

---
Developed by Anghel Mihai Alexandru - 2025
