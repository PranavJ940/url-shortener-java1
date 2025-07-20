# URL Shortener Java

A simple URL shortener service inspired by TinyURL, built with Java's built-in HTTP server, MySQL database, and a plain HTML/CSS/JS frontend. No frameworks are used.

## Features
- Anonymous URL shortening
- User registration and login (for custom URLs)
- Custom short URLs for logged-in users
- Redirection from short URL to original
- Simple, functional UI (no frameworks)
- Logging with SLF4J
- Unit tests with JUnit 5 and Mockito

## Tech Stack
- **Backend:** Java (`com.sun.net.httpserver.HttpServer`), JDBC, MySQL
- **Frontend:** HTML, CSS, JavaScript (no frameworks)
- **Logging:** SLF4J
- **Testing:** JUnit 5, Mockito
- **CI/CD:** GitHub Actions (optional)

## Requirements
- Java 11 or higher
- Maven
- MySQL Server

## Setup Instructions

### 1. Clone the Repository
```sh
git clone https://github.com/yourusername/url-shortener-java.git
cd url-shortener-java
```

### 2. MySQL Database Setup
- Start your MySQL server.
- Create the database and tables:
```sql
CREATE DATABASE urlshortenerdb;
USE urlshortenerdb;
CREATE TABLE urls (
    code VARCHAR(10) PRIMARY KEY,
    url VARCHAR(2048)
);
CREATE TABLE users (
    username VARCHAR(100) PRIMARY KEY,
    password VARCHAR(100)
);
```
- Update your MySQL username and password in `UrlShortenerService.java` and `UserService.java`:
  ```java
  private static final String DB_USER = "root"; // your MySQL username
  private static final String DB_PASS = "password"; // your MySQL password
  ```

### 3. Build the Project
```sh
mvn clean package
```

### 4. Run the Server
**In Eclipse:**
- Right-click `Main.java` → Run As → Java Application

**Or in the command line:**
```sh
java -cp target/url-shortener-java-1.0-SNAPSHOT.jar com.example.url_shortener_java.Main
```

### 5. Open the Frontend
- Open `frontend/index.html` in your web browser.

## Usage
- Enter a URL in the form and click **Shorten**.
- The backend will return a short URL (e.g., `/r/AbCd12`).
- Click the short URL to test redirection.

## Testing
Run unit tests with:
```sh
mvn test
```

## Project Structure
```
url-shortener-java/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/url_shortener_java/
│   │   │       ├── Main.java
│   │   │       ├── UrlShortenerService.java
│   │   │       └── UserService.java
│   └── resources/
│       └── logback.xml
├── frontend/
│   ├── index.html
│   ├── style.css
│   └── app.js
```

## API Endpoints
- `POST /shorten` — Shorten a URL (body: `url=<your-url>`, returns JSON with `shortUrl`)
- `GET /r/{code}` — Redirect to the original URL

## License
MIT

---

> **Assignment Requirements:**
> - Structured approach to executing ideas
> - Use of README.md file to document the exact requirements
> - Use of GitHub issues to keep track of the work remaining
> - Use of Git branches to isolate work on each feature
> - Use of Pull Requests to critic your own work
> - Use of Github Actions to ensure builds are run and validated on every Pull Request, and once the merge is done to the main branch.
