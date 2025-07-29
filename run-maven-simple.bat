@echo off
echo Running Eventra with Maven (Simple approach)...

REM Set Java home
set "JAVA_HOME=C:\PROGRA~1\Java\jdk-11"
set "PATH=%JAVA_HOME%\bin;%PATH%"

REM Verify Java is available
java -version
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Java not found.
    pause
    exit /b 1
)

echo Java found successfully!

REM Try to run Maven directly without the wrapper
echo Running Maven...
mvn clean javafx:run

if %ERRORLEVEL% NEQ 0 (
    echo Maven not found, trying with wrapper...
    call mvnw.cmd clean javafx:run
)

pause 