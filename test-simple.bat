@echo off
echo Testing basic Java compilation...

REM Set Java home
set "JAVA_HOME=C:\PROGRA~1\Java\jdk-11"
set "PATH=%JAVA_HOME%\bin;%PATH%"

REM Create a simple test class
echo Creating test class...
if not exist "test" mkdir test
echo package test; > test\TestApp.java
echo public class TestApp { >> test\TestApp.java
echo     public static void main(String[] args) { >> test\TestApp.java
echo         System.out.println("Java is working!"); >> test\TestApp.java
echo     } >> test\TestApp.java
echo } >> test\TestApp.java

REM Compile the test
echo Compiling test...
javac -d test test\TestApp.java

if %ERRORLEVEL% EQU 0 (
    echo Compilation successful!
    echo Running test...
    java -cp test test.TestApp
) else (
    echo Compilation failed!
)

pause 