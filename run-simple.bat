@echo off
echo Running Eventra (Simple Build Approach)...

REM Set Java home
set "JAVA_HOME=C:\PROGRA~1\Java\jdk-11"
set "PATH=%JAVA_HOME%\bin;%PATH%"

REM Build the project
echo Building project...
call build.bat

REM Check if build was successful
if %ERRORLEVEL% NEQ 0 (
    echo Build failed!
    pause
    exit /b 1
)

echo Build successful! Starting application...
echo.

REM Run the application
java -cp "target\classes;target\lib\javafx-controls-11.0.12.jar;target\lib\javafx-fxml-11.0.12.jar;target\lib\javafx-base-11.0.12.jar;target\lib\javafx-graphics-11.0.12.jar;target\lib\javafx-graphics-11.0.12-win.jar;target\lib\HikariCP-5.0.1.jar;target\lib\slf4j-api-1.7.36.jar;target\lib\slf4j-simple-1.7.36.jar;target\lib\jbcrypt-0.4.jar;target\lib\h2-2.2.224.jar" --add-modules javafx.controls,javafx.fxml com.eventra.App

pause 