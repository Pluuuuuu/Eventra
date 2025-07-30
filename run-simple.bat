@echo off
echo Running Eventra with Maven Wrapper...

REM Set Java home
set "JAVA_HOME=C:\Program Files\Java\jdk-21"
set "PATH=%JAVA_HOME%\bin;%PATH%"

REM Verify Java
java -version
if %ERRORLEVEL% NEQ 0 (
    echo Java not found!
    pause
    exit /b 1
)

echo Java found! Running with Maven wrapper...
call mvnw.cmd clean compile javafx:run

pause 