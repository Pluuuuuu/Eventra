@echo off
echo Final attempt to run Eventra...

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

REM Clean previous build
if exist "target\classes" rmdir /s /q target\classes
if exist "target\lib" rmdir /s /q target\lib

REM Create directories
mkdir target\classes
mkdir target\lib

REM Download JavaFX 8 (more compatible)
echo Downloading JavaFX 8...
powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/openjfx/javafx-controls/8.0.202/javafx-controls-8.0.202.jar' -OutFile 'target\lib\javafx-controls-8.0.202.jar'"
powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/openjfx/javafx-fxml/8.0.202/javafx-fxml-8.0.202.jar' -OutFile 'target\lib\javafx-fxml-8.0.202.jar'"
powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/openjfx/javafx-base/8.0.202/javafx-base-8.0.202.jar' -OutFile 'target\lib\javafx-base-8.0.202.jar'"
powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/openjfx/javafx-graphics/8.0.202/javafx-graphics-8.0.202.jar' -OutFile 'target\lib\javafx-graphics-8.0.202.jar'"

REM Download other dependencies
echo Downloading other dependencies...
powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/com/zaxxer/HikariCP/5.0.1/HikariCP-5.0.1.jar' -OutFile 'target\lib\HikariCP-5.0.1.jar'"
powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/slf4j/slf4j-api/1.7.36/slf4j-api-1.7.36.jar' -OutFile 'target\lib\slf4j-api-1.7.36.jar'"
powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/slf4j/slf4j-simple/1.7.36/slf4j-simple-1.7.36.jar' -OutFile 'target\lib\slf4j-simple-1.7.36.jar'"
powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/mindrot/jbcrypt/0.4/jbcrypt-0.4.jar' -OutFile 'target\lib\jbcrypt-0.4.jar'"
powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/com/h2database/h2/2.2.224/h2-2.2.224.jar' -OutFile 'target\lib\h2-2.2.224.jar'"

REM Compile Java files
echo Compiling Java files...
javac -cp "target\lib\*;src\main\java" -d target\classes src\main\java\com\eventra\*.java src\main\java\com\eventra\controller\*.java src\main\java\com\eventra\dao\*.java src\main\java\com\eventra\model\*.java src\main\java\com\eventra\util\*.java

if %ERRORLEVEL% EQU 0 (
    echo Build successful! Starting application...
    echo.
    java -cp "target\classes;target\lib\*" com.eventra.App
) else (
    echo Build failed!
)

pause 