# How to Run Deadline Reminder

The application uses a specific directory structure and requires running from the project root.

## Quick Start (Recommended)

1. Open PowerShell.
2. Navigate to the `Deadline-Reminder-Java-main` directory:
   ```powershell
   cd "Deadline-Reminder-Java-main"
   ```
3. Run the automated script:
   ```powershell
   .\run_app.ps1
   ```
   *Note: If you get a security error, run:*
   ```powershell
   powershell -ExecutionPolicy Bypass -File .\run_app.ps1
   ```

## Why your previous attempt failed

You likely ran:
`java -cp ... MainFrame`

This failed because:
1. **Wrong Directory**: You were in the parent folder `(2)`, but the classpath paths (`out`, `lib`) are inside `Deadline-Reminder-Java-main`.
2. **Wrong Class**: `MainFrame` (in the src root) appears to be a legacy/duplicate file. The correct entry point is `com.deadlinereminder.Main`.

## Manual Compilation (If needed)

If you prefer running commands manually, ensure you are in the `Deadline-Reminder-Java-main` folder:

```powershell
# Compile
javac -cp "lib/mysql-connector-j-8.0.33.jar" -d out (Get-ChildItem -Recurse -Filter *.java | ForEach-Object { $_.FullName })

# Run
java -cp "out;lib/mysql-connector-j-8.0.33.jar" com.deadlinereminder.Main
```
