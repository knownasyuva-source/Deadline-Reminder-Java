Deadline Reminder - Java Swing + MySQL (Beginner-friendly)

Overview
- Small Java Swing app to manage deadlines (Assignment / Project).
- Uses JDBC + MySQL Connector/J (placed in `lib/mysql-connector-j-8.0.33.jar`).

Quick setup
1. Ensure MySQL is running and create the DB/table by running `sql/create_deadline_db.sql` (or let the app create the table).
2. Edit database credentials in `src/com/deadlinereminder/DBUtil.java` if needed.

Build & run (Windows PowerShell)
```powershell
# compile
javac -cp "lib/mysql-connector-j-8.0.33.jar" -d out (Get-ChildItem -Recurse -Filter *.java | ForEach-Object { $_.FullName })

# run
java -cp "out;lib/mysql-connector-j-8.0.33.jar" com.deadlinereminder.Main
```

Notes
- The default DB URL uses `jdbc:mysql://localhost:3306/deadlineDB`.
- Edit `DBUtil` to change `USER` and `PASS`.
- This project is intentionally simple and split into separate classes for learning.
