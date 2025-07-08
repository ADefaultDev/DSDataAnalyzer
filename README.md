# 📊 DSDataAnalyzer

**DSDataAnalyzer** —  pet-project using Java and Spring, created to collect data and store it in DB, as well as to process it via DeepSeek.

---

### 🚀 Possibilities

- Data collection from various sites
- Data storage with use of PostgreSQL
- Preparation of stored data for DeepSeek
- REST API

---

### 🛠️ Stack

| Usage          | Technology     |
| -------------- | -------------- |
| Language       | Java 17+       |
| Framework      | Spring Boot    |
| Build          | Gradle         |
| Database       | PostgreSQL     |
| AI             | DeepSeek       |
| WebDriver      | Selenium       |

---

### 📆 Setup

#### 1. Clone the repository

```bash
git clone https://github.com/ADefaultDev/DSDataAnalyzer.git
cd DSDataAnalyzer
```

#### 2. Copy the template

```bash
cp src/main/resources/application-template.properties src/main/resources/application.properties
```

#### 3. Fill your config

```properties
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/DSDataAnalyzerDB
    username: your_username 
    password: your_password
```

Or you can setup project using environment:

```bash
DB_USER=your_username DB_PASS=your_password ./gradlew bootRun
```

#### 4 PostgreSQL Setup (Docker)

```bash
docker run --name ds-postgres \
  -e POSTGRES_DB=DSDataAnalyzerDB \
  -e POSTGRES_USER=your_username \
  -e POSTGRES_PASSWORD=your_password \
  -p 5432:5432 -d postgres
```

#### 5. Application start

```bash
./gradlew bootRun
```

---

### 💃 Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/adefaultdev/DSDataAnalyzer/
│   │       ├── controller/
│   │       ├── service/
│   │       ├── repository/
│   │       └── model/
│   └── resources/
│       └── application.properties
└── test/
```

---

### 📌 Future plans

-

---

### 🤖 Author

**[ADefaultDev]**

Pet-project created to explore possibilities of Spring, Selenium and DeepSeek integration.
