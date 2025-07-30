@echo off
echo ========================================
echo Eventra Application - Maven Only
echo ========================================
echo.

REM Set Java home for Java 21
set "JAVA_HOME=C:\Program Files\Java\jdk-21"
set "PATH=%JAVA_HOME%\bin;%PATH%"

REM Verify Java is available
java -version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Java not found. Please check your Java installation.
    echo Expected Java home: %JAVA_HOME%
    pause
    exit /b 1
)

echo Java 21 found successfully!
echo.

REM Check if Maven is available
mvn --version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Maven not found. Please install Maven or use run-unified.bat
    pause
    exit /b 1
)

echo Maven detected!
echo.

REM Clean, compile and run with Maven
echo Building and running with Maven...
call mvn clean compile javafx:run

echo.
echo Application finished.
pause 