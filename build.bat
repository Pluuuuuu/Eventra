@echo off
echo Building Eventra Application...

REM Set Java home
set JAVA_HOME=C:\Program Files\Java\jdk-21
set PATH=%JAVA_HOME%\bin;%PATH%

REM Create directories
if not exist "target\classes" mkdir target\classes
if not exist "target\lib" mkdir target\lib

REM Download dependencies (if not already present)
if not exist "target\lib\javafx-controls-21.0.2.jar" (
    echo Downloading JavaFX dependencies...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/openjfx/javafx-controls/21.0.2/javafx-controls-21.0.2.jar' -OutFile 'target\lib\javafx-controls-21.0.2.jar'"
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/openjfx/javafx-fxml/21.0.2/javafx-fxml-21.0.2.jar' -OutFile 'target\lib\javafx-fxml-21.0.2.jar'"
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/openjfx/javafx-base/21.0.2/javafx-base-21.0.2.jar' -OutFile 'target\lib\javafx-base-21.0.2.jar'"
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/openjfx/javafx-graphics/21.0.2/javafx-graphics-21.0.2.jar' -OutFile 'target\lib\javafx-graphics-21.0.2.jar'"
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/openjfx/javafx-graphics/21.0.2/javafx-graphics-21.0.2-win.jar' -OutFile 'target\lib\javafx-graphics-21.0.2-win.jar'"
)

if not exist "target\lib\HikariCP-5.0.1.jar" (
    echo Downloading HikariCP...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/com/zaxxer/HikariCP/5.0.1/HikariCP-5.0.1.jar' -OutFile 'target\lib\HikariCP-5.0.1.jar'"
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/slf4j/slf4j-api/1.7.36/slf4j-api-1.7.36.jar' -OutFile 'target\lib\slf4j-api-1.7.36.jar'"
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/slf4j/slf4j-simple/1.7.36/slf4j-simple-1.7.36.jar' -OutFile 'target\lib\slf4j-simple-1.7.36.jar'"
)

if not exist "target\lib\jbcrypt-0.4.jar" (
    echo Downloading BCrypt...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/mindrot/jbcrypt/0.4/jbcrypt-0.4.jar' -OutFile 'target\lib\jbcrypt-0.4.jar'"
)

if not exist "target\lib\h2-2.2.224.jar" (
    echo Downloading H2 Database...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/com/h2database/h2/2.2.224/h2-2.2.224.jar' -OutFile 'target\lib\h2-2.2.224.jar'"
)

REM Compile Java files
echo Compiling Java files...
javac -cp "target\lib\*;src\main\java" -d target\classes src\main\java\com\eventra\*.java src\main\java\com\eventra\controller\*.java src\main\java\com\eventra\dao\*.java src\main\java\com\eventra\model\*.java src\main\java\com\eventra\util\*.java

if %ERRORLEVEL% EQU 0 (
    echo Build successful!
    echo You can now run the application using: run.bat
) else (
    echo Build failed!
)

pause 