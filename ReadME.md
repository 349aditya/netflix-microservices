# 🎬 Netflix Streaming Platform (Microservices)

A Netflix-inspired video streaming platform built using **Spring Boot Microservices**. This project is designed with a scalable, service-oriented architecture where each service has a dedicated responsibility.

> 🚧 This project is currently under development.

---

## 🏗️ Architecture

The application is divided into the following microservices:

| Service | Description |
|----------|-------------|
| 📹 **Video Service** | Manages video metadata, uploads, categories, thumbnails, and video information. |
| ⚙️ **Encoding Service** | Processes uploaded videos and converts them into streaming-friendly formats and multiple resolutions. |
| 📺 **Streaming Service** | Streams encoded video content efficiently to clients with support for adaptive playback (planned). |
| 📝 **Content Service** | Handles content management such as movies, TV shows, genres, recommendations, and related metadata. |

---

## 🛠️ Tech Stack

- Java
- Spring Boot
- AWS S3
- Spring Data JPA
- MySQL
- Apache Kafka (Event-Driven Communication)
- Redis (Caching)
- Docker
- Gradle
- REST APIs

---

## 📂 Project Structure

```
netflix/
│
├── content-service/
├── encoding-service/
├── streaming-service/
├── video-service/
│
├── docker-compose.yml
└── README.md
```

---

## 🔄 Service Responsibilities

### 📹 Video Service
- Upload videos
- Store video metadata
- Generate upload events
- Manage thumbnails
- Communicate with Encoding Service

### ⚙️ Encoding Service
- Listen for uploaded video events
- Encode videos into multiple qualities
- Generate HLS/DASH files
- Store encoded assets
- Notify Streaming Service

### 📺 Streaming Service
- Stream encoded video content
- Support byte-range requests
- Serve HLS playlists
- Manage playback sessions
- Optimize streaming performance

### 📝 Content Service
- Manage movies and TV shows
- Store genres and categories
- Search content
- Manage recommendations
- Maintain content metadata

---

## 📡 Microservice Communication

The services communicate using:

- REST APIs
- Apache Kafka Events
- Asynchronous Event Processing

Example flow:

```
User Uploads Video
        │
        ▼
Video Service
        │
        ▼
Kafka Event
        │
        ▼
Encoding Service
        │
        ▼
Encoded Successfully
        │
        ▼
Streaming Service
        │
        ▼
Video Available for Streaming
```

---

## 🚀 Getting Started

### Clone Repository

```bash
git clone https://github.com/349aditya/netflix-microservices.git

cd netflix-microservices
```

## 📌 Future Features

- User Authentication (JWT)
- Subscription Plans
- Watch History
- Continue Watching
- Recommendation Engine
- Search Service
- Notifications
- Admin Dashboard
- API Gateway
- Service Discovery (Eureka)
- Distributed Tracing
- Centralized Configuration
- Kubernetes Deployment
- CI/CD Pipeline

---

## 📖 Learning Objectives

This project demonstrates:

- Microservice Architecture
- Event-Driven Design
- Spring Boot Best Practices
- Kafka Messaging
- Video Processing Workflow
- Dockerized Development
- Scalable Backend Design
- RESTful API Development

---

## 📄 License

This project is intended for educational and portfolio purposes.