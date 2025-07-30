@echo off
echo Running Eventra Application with Maven...

REM Set Java home for Java 21
set "JAVA_HOME=C:\Program Files\Java\jdk-21"
set "PATH=%JAVA_HOME%\bin;%PATH%"

REM Verify Java is available
java -version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Java not found. Please check your Java installation.
    echo Expected Java home: %JAVA_HOME%
    echo.
    echo Try running: .\check-java.bat
    pause
    exit /b 1
)

echo Java found successfully!

REM Clean and compile the project
echo Building project with Maven...
call mvnw.cmd clean compile

if %ERRORLEVEL% NEQ 0 (
    echo Build failed! Check the errors above.
    pause
    exit /b 1
)

echo Build successful!

REM Run the application using JavaFX Maven plugin
echo Running application...
call mvnw.cmd javafx:run

pause 