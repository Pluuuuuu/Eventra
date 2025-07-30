@echo off
echo ========================================
echo Eventra Application - Final Runner
echo ========================================
echo.

REM Set environment variables
set "JAVA_HOME=C:\Program Files\Java\jdk-21"
set "PATH=%JAVA_HOME%\bin;%PATH%"
set "MAVEN_OPTS=-Xmx512m"

REM Verify Java
echo Checking Java installation...
java -version
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Java not found!
    pause
    exit /b 1
)

echo Java found successfully!
echo.

REM Try Maven wrapper first
echo Trying Maven wrapper...
call mvnw.cmd clean compile javafx:run
if %ERRORLEVEL% EQU 0 (
    echo Application completed successfully!
    goto :end
)

echo Maven wrapper failed, trying batch system...
echo.

REM Fallback to batch system
call build.bat
if %ERRORLEVEL% NEQ 0 (
    echo Batch build failed!
    pause
    exit /b 1
)

echo Batch build successful! Running application...
java -cp "target\classes;target\lib\*" --add-modules javafx.controls,javafx.fxml com.eventra.App

:end
echo.
echo Application finished.
pause 