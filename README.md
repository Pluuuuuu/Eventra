# Eventra — Event Management System (JavaFX + Azure SQL)

> Desktop‑first, offline‑ready event management app with a clean MVC architecture, DAOs, and HikariCP database pooling.

## 1) Overview
Eventra unifies the full event lifecycle—planning, registration, on‑site QR check‑in, and analytics—inside a JavaFX desktop app backed by Azure SQL. The system is designed for reliability (works offline, syncs to cloud when available), security (BCrypt, TLS, parameterized queries), and maintainability (DAO pattern, layered architecture). 【10†source】

## 2) Core Features
- Event & session creation and scheduling
- Venue & speaker management
- Attendee registration & profile management
- QR‑code based check‑in
- Role‑based dashboards (SuperAdmin, Admin, Staff, Attendee)
- Analytics & reports
- Offline‑first with cloud synchronization to Azure SQL
【10†source】

## 3) Architecture (MVC + DAO)
**MVC**
- **Model**: Plain Java classes mapping to DB tables (User, Event, Session, Attendee, etc.).
- **View**: JavaFX FXML views with CSS styling.
- **Controller**: GUI logic; calls DAOs; updates views.
**DAO Layer**
- Encapsulates SQL access/CRUD (e.g., `UserDAO`, `EventDAO`, `SessionDAO`, `AttendeeDAO`, `EventSaveDAO`). Ensures parameterized queries, transactions where needed, and clean mapping to models. 【10†source】【9†source】

**Startup**
- `App.java` launches JavaFX, calls database initialization, and loads the first FXML (Welcome). 【9†source】
- `DatabaseInitializer` creates/validates lookup & main tables, inserts defaults, cleans duplicates, and runs validation. 【9†source】

## 4) Tech Stack & Key Dependencies
- **Java 21**, **JavaFX (FXML + CSS)**
- **Azure SQL** via **mssql‑jdbc**
- **HikariCP** for database connection pooling
- **BCrypt** for password hashing
- **Maven** (POM) for dependency & build management
【10†source】

## 5) Project Structure (high‑level)
```
src/
  main/
    java/com/eventra/
      App.java
      DatabaseInitializer.java
      Db.java
      controller/ (LoginController, CreateEventController, ...)
      dao/ (UserDAO, EventDAO, SessionDAO, AttendeeDAO, EventSaveDAO, ...)
      model/ (User, Event, Session, Attendee, ...)
      util/ (SessionManager, ViewUtil, RouteManager, PasswordHashGenerator, ...)
    resources/
      fxml/ (Welcome.fxml, Login.fxml, Dashboards, ...)
      css/styles.css
test/
  (TestDatabaseConnection, TestAccountCreation, TestApp, ...)
pom.xml
```
(See the documented file list in the provided “Explanation Branch” PDF for the complete inventory.) 【9†source】

## 6) Configuration
Provide DB credentials securely (env vars, JVM props, or a protected config):
```
-Ddb.url=jdbc:sqlserver://<server>:1433;databaseName=<DB>;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net
-Ddb.user=<username>
-Ddb.password=<password>
```
The app initializes the database schema on first run via `DatabaseInitializer`. 【9†source】

## 7) Build & Run
Using Maven (Java 21 installed):
```bash
# Run (JavaFX plugin)
./mvnw javafx:run

# Or package and run
./mvnw -DskipTests package
java -jar target/eventra-*.jar
```
(Commands and JavaFX packaging are declared in `pom.xml`.) 【10†source】

## 8) Database & Pooling
- **HikariCP** provides a pool of reusable DB connections to reduce latency and improve throughput (especially during peak QR check‑ins). DAOs should acquire connections with try‑with‑resources and never hold them longer than needed.
- **DatabaseInitializer** creates lookup/main tables (StatusType, RoleType, EventStatusType, SessionStatusType, RegistrationStatusType; UserM, EventM, Session, Presenter, Attendee, EventRegistration), inserts defaults, cleans duplicates, and validates structure. 【9†source】

## 9) Security
- **BCrypt**: Password hashing & verification.
- **Parameterized queries**: Prevent SQL injection in DAOs.
- **TLS**: Encrypted connections to Azure SQL.
【10†source】

## 10) Testing Utilities
- **TestDatabaseConnection**: Verifies DB access, creates a sample user/attendee using DAOs.
- **TestAccountCreation / TestConnection**: Smoke tests for connectivity and user creation.
- **DatabaseDebugger**: Checks presence and basic contents of key tables.
【9†source】

## 11) Coding Guidelines
- Controllers contain no raw SQL—**all DB access goes through DAOs**.
- Use **try‑with‑resources** for `Connection/PreparedStatement/ResultSet` to avoid leaks.
- Keep models simple (fields + getters/setters); map rows in DAOs.
- Keep **navigation** centralized in utilities like `ViewUtil`/`RouteManager`.
- Centralize session state in `SessionManager`.

## 12) Troubleshooting
- **“Pool isn’t initialized / closed”**: Ensure `Db.init()` runs before any DAO call and that no early `Db.close()` occurs. Verify HikariCP and mssql‑jdbc are on the classpath (via POM).
- **Cannot log in (Attendee)**: Check `RoleTypeID` and `StatusTypeID` are set correctly during user creation; verify BCrypt/legacy upgrade path in `UserDAO` if present. Use the test classes to diagnose. 【9†source】

## 13) License
All rights belong to maryam zd and mariam bd

---

### Credits
Prepared by: Maryam Zien Al Deen & Mariam Badawi — Supervised by Dr. Ahmad Ismail. 
