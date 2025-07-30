@echo off
echo Running Eventra Application...

REM Set Java home with proper quoting for paths with spaces
set "JAVA_HOME=C:\Program Files\Java\jdk-24"
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

REM First build the project
echo Building project...
call build.bat

REM Run the application
echo Running application...
java -cp "target\classes;target\lib\*" --add-modules javafx.controls,javafx.fxml com.eventra.App

pause 