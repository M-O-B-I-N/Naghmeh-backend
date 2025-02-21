# Naghmeh Backend

Naghmeh is a backend service for the Naghmeh project, a comprehensive collection of Iranian poets' works spanning across different centuries. This backend is designed to serve data for the iOS, Android, and Desktop apps.

The backend is built using **Ktor** and **Exposed** with **SQLite** for data storage.

## Features

- A simple backend service for storing and serving Iranian poetry.
- **Exposed ORM** for database interaction.
- **SQLite** database for local storage (lightweight, simple setup).
- RESTful API to retrieve data.

## Tech Stack

- **Ktor**: Kotlin-based framework for building the server.
- **Exposed**: Kotlin ORM framework for interacting with the SQLite database.
- **SQLite**: Lightweight, serverless SQL database for development.

## Getting Started

### Prerequisites

Before getting started, make sure you have the following installed:

- **Kotlin** 1.6 or higher
- **Java** 21 or higher
- **Gradle** 8.9

### Clone the Repository

```bash
git clone https://github.com/yourusername/naghmeh-backend.git
cd naghmeh-backend
