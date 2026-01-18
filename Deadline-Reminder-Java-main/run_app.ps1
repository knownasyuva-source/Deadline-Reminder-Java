$ErrorActionPreference = "Stop"

# Define paths
$libDir = "lib"
$outDir = "out"
$srcDir = "src"
$mysqlJar = Join-Path $libDir "mysql-connector-j-8.0.33.jar"

# Check if MySQL jar exists
if (-not (Test-Path $mysqlJar)) {
    Write-Host "Error: MySQL connector jar not found at $mysqlJar" -ForegroundColor Red
    exit 1
}

# Create output directory if it doesn't exist
if (-not (Test-Path $outDir)) {
    New-Item -ItemType Directory -Path $outDir | Out-Null
}

# Get all Java files
$sources = Get-ChildItem -Path $srcDir -Recurse -Filter "*.java" | Select-Object -ExpandProperty FullName

Write-Host "Compiling..."
javac -cp $mysqlJar -d $outDir $sources

if ($LASTEXITCODE -eq 0) {
    Write-Host "Running..."
    # Add out directory and mysql jar to classpath
    $classPath = "$outDir;$mysqlJar"
    java -cp $classPath com.deadlinereminder.Main
} else {
    Write-Host "Compilation failed." -ForegroundColor Red
}
