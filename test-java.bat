@echo off
echo Testing current Java installation...

REM Check current JAVA_HOME
if defined JAVA_HOME (
    echo Current JAVA_HOME: %JAVA_HOME%
) else (
    echo JAVA_HOME is not set
    pause
    exit /b 1
)

REM Test if Java works with current JAVA_HOME
echo.
echo Testing Java with current JAVA_HOME...
java -version
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Java is not working with current JAVA_HOME
    echo.
    echo Let's try to find a working Java installation...
    goto :find_java
) else (
    echo SUCCESS: Java is working correctly!
    echo.
    echo You can now try running the application with:
    echo .\run-maven.bat
    pause
    exit /b 0
)

:find_java
echo.
echo Searching for Java installations...

REM Check common Java locations
set "JAVA_LOCATIONS=C:\Program Files\Java\jdk-11;C:\PROGRA~1\Java\jdk-11;C:\Program Files\Java\jdk-17;C:\PROGRA~1\Java\jdk-17;C:\Program Files\Java\jdk-21;C:\PROGRA~1\Java\jdk-21"

for %%p in (%JAVA_LOCATIONS%) do (
    if exist "%%p\bin\java.exe" (
        echo Found Java at: %%p
        echo Testing this Java installation...
        set "TEST_JAVA_HOME=%%p"
        set "TEST_PATH=%%p\bin;%PATH%"
        
        set "JAVA_HOME=%TEST_JAVA_HOME%"
        set "PATH=%TEST_PATH%"
        
        java -version >nul 2>&1
        if !ERRORLEVEL! EQU 0 (
            echo SUCCESS: This Java installation works!
            echo Setting JAVA_HOME to: %%p
            echo.
            echo You can now try running the application with:
            echo .\run-maven.bat
            pause
            exit /b 0
        )
    )
)

echo.
echo No working Java installation found.
echo Please install Java 11 or later.
pause
exit /b 1 