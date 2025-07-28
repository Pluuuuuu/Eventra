@echo off
echo Checking Java installation...

REM Check if JAVA_HOME is set
if defined JAVA_HOME (
    echo JAVA_HOME is set to: %JAVA_HOME%
) else (
    echo JAVA_HOME is not set
)

REM Try to find Java in common locations
echo.
echo Checking for Java installation...

set "JAVA_PATHS=C:\Program Files\Java\jdk-11;C:\PROGRA~1\Java\jdk-11;C:\Program Files\Java\jdk-17;C:\PROGRA~1\Java\jdk-17;C:\Program Files\Java\jdk-21;C:\PROGRA~1\Java\jdk-21;C:\Program Files\Eclipse Adoptium\jdk-11.0.21.9-hotspot;C:\PROGRA~1\Eclipse Adoptium\jdk-11.0.21.9-hotspot;C:\Program Files\Eclipse Adoptium\jdk-17.0.9.9-hotspot;C:\PROGRA~1\Eclipse Adoptium\jdk-17.0.9.9-hotspot"

for %%p in (%JAVA_PATHS%) do (
    if exist "%%p\bin\java.exe" (
        echo Found Java at: %%p
        set "FOUND_JAVA=%%p"
        goto :found_java
    )
)

echo No Java installation found in common locations.
echo Please install Java 11 or later and set JAVA_HOME.
pause
exit /b 1

:found_java
echo.
echo Setting JAVA_HOME to: %FOUND_JAVA%
set "JAVA_HOME=%FOUND_JAVA%"
set "PATH=%JAVA_HOME%\bin;%PATH%"

echo.
echo Testing Java installation:
java -version
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Java is not working properly
    pause
    exit /b 1
)

echo.
echo Java is working correctly!
echo You can now run: .\run-maven.bat
pause 