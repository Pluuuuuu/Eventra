@echo off
echo Running Eventra with Java 11 and compatible JavaFX...

REM Set Java home to Java 11
set "JAVA_HOME=C:\PROGRA~1\Java\jdk-11"
set "PATH=%JAVA_HOME%\bin;%PATH%"

REM Verify Java 11 is available
java -version
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Java 11 not found.
    pause
    exit /b 1
)

echo Java 11 found successfully!

REM Clean previous build
if exist "target\classes" rmdir /s /q target\classes
if exist "target\lib" rmdir /s /q target\lib

REM Create directories
mkdir target\classes
mkdir target\lib

REM Download JavaFX 11 (compatible with Java 11)
echo Downloading JavaFX 11...
powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/openjfx/javafx-controls/11.0.2/javafx-controls-11.0.2.jar' -OutFile 'target\lib\javafx-controls-11.0.2.jar'"
powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/openjfx/javafx-fxml/11.0.2/javafx-fxml-11.0.2.jar' -OutFile 'target\lib\javafx-fxml-11.0.2.jar'"
powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/openjfx/javafx-base/11.0.2/javafx-base-11.0.2.jar' -OutFile 'target\lib\javafx-base-11.0.2.jar'"
powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/openjfx/javafx-graphics/11.0.2/javafx-graphics-11.0.2.jar' -OutFile 'target\lib\javafx-graphics-11.0.2.jar'"
powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/openjfx/javafx-graphics/11.0.2/javafx-graphics-11.0.2-win.jar' -OutFile 'target\lib\javafx-graphics-11.0.2-win.jar'"

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
    java -cp "target\classes;target\lib\*" --add-modules javafx.controls,javafx.fxml com.eventra.App
) else (
    echo Build failed!
)

pause 