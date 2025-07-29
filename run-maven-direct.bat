@echo off
echo Running Eventra with direct Maven...

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

REM Check if Maven is available
mvn -version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo Maven not found. Downloading Maven...
    
    REM Create maven directory if it doesn't exist
    if not exist "maven" mkdir maven
    
    REM Download Maven if not present
    if not exist "maven\apache-maven-3.8.9\bin\mvn.cmd" (
        echo Downloading Maven 3.8.9...
        powershell -Command "Invoke-WebRequest -Uri 'https://archive.apache.org/dist/maven/maven-3/3.8.9/binaries/apache-maven-3.8.9-bin.zip' -OutFile 'maven.zip'"
        powershell -Command "Expand-Archive -Path 'maven.zip' -DestinationPath 'maven' -Force"
        del maven.zip
    )
    
    set "M2_HOME=%CD%\maven\apache-maven-3.8.9"
    set "PATH=%M2_HOME%\bin;%PATH%"
)

echo Running Maven clean and javafx:run...
mvn clean javafx:run

pause 