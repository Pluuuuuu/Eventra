@echo off
echo Setting up Java environment and running Eventra...

REM Set Java home with proper quoting for paths with spaces
set "JAVA_HOME=C:\Program Files\Java\jdk-24"
set "PATH=%JAVA_HOME%\bin;%PATH%"

REM Verify Java is available
java -version
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Java not found. Please check your Java installation.
    echo Expected Java home: %JAVA_HOME%
    pause
    exit /b 1
)

echo Java found successfully!
echo Running Maven with JavaFX...

REM Run Maven with proper quoting
call mvnw.cmd clean javafx:run

pause 