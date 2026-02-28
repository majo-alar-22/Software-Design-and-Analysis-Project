# Intramural Soccer (JavaFX + SQLite)

Signup + Login + Dashboard stub.

## Run
```bash
mvn javafx:run
```

## Screens
- `login.fxml` → log in
- `signup.fxml` → create account
- `dashboard.fxml` → landing page after login

## Database
SQLite DB is created at:
- `${user.home}/soccer.db`

Table:
- `users(id, name, email, password_hash, salt, created_at)`
