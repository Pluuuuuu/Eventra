@echo off
echo Running Eventra with Java 21...

REM Set Java home to Java 21 (adjust path as needed)
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

REM Clean and rebuild with Java 21
echo Building project with Java 21...
call build.bat

REM Check if build was successful
if %ERRORLEVEL% NEQ 0 (
    echo Build failed!
    pause
    exit /b 1
)

echo Build successful! Starting application...
echo.

REM Run the application with Java 21
java -cp "target\classes;target\lib\*" --add-modules javafx.controls,javafx.fxml com.eventra.App

pause 