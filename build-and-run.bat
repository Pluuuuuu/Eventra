@echo off
echo Building Eventra Application...
echo.

REM Clean and compile
call mvn clean compile

if %ERRORLEVEL% NEQ 0 (
    echo Build failed!
    pause
    exit /b 1
)

echo.
echo Build successful! Running application...
echo.

REM Run the application
call mvn javafx:run

pause 