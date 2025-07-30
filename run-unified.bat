@echo off
echo ========================================
echo Eventra Application - Unified Runner
echo ========================================
echo.

REM Set Java home for Java 21 (consistent across all systems)
set "JAVA_HOME=C:\Program Files\Java\jdk-21"
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

echo Java 21 found successfully!
echo.

REM Check if Maven is available
mvn --version >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    echo Maven detected - Using Maven build system
    echo.
    
    REM Clean and compile with Maven
    echo Building project with Maven...
    call mvn clean compile
    
    if %ERRORLEVEL% NEQ 0 (
        echo Maven build failed! Trying batch build system...
        echo.
        goto :batch_build
    )
    
    echo Maven build successful!
    echo.
    
    REM Run with JavaFX Maven plugin
    echo Running application with Maven JavaFX plugin...
    call mvn javafx:run
    
) else (
    echo Maven not found - Using batch build system
    echo.
    goto :batch_build
)

goto :end

:batch_build
echo Using batch build system...
echo.

REM First build the project using batch system
echo Building project with batch system...
call build.bat

if %ERRORLEVEL% NEQ 0 (
    echo Batch build failed!
    pause
    exit /b 1
)

echo Batch build successful!
echo.

REM Run the application using batch system
echo Running application with batch system...
java -cp "target\classes;target\lib\*" --add-modules javafx.controls,javafx.fxml com.eventra.App

:end
echo.
echo Application finished.
pause 