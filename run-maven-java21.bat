@echo off
echo Running Eventra with Maven and Java 21...

REM Set Java home to Java 21
set "JAVA_HOME=C:\Program Files\Java\jdk-21"
set "PATH=%JAVA_HOME%\bin;%PATH%"

REM Verify Java 21 is available
java -version
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Java 21 not found. Please install Java 21 first.
    echo Expected Java home: %JAVA_HOME%
    pause
    exit /b 1
)

echo Java 21 found successfully!
echo Running Maven with Java 21...

REM Run Maven with Java 21
call mvnw.cmd clean javafx:run

pause 