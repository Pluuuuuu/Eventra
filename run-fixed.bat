@echo off
echo ========================================
echo Eventra Application - Fixed Runner
echo ========================================
echo.

REM Set environment variables properly
set "JAVA_HOME=C:\Program Files\Java\jdk-24"
set "PATH=%JAVA_HOME%\bin;%PATH%"
set "MAVEN_OPTS=-Xmx512m"

REM Verify Java installation
echo Checking Java installation...
java -version
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Java not found! Please install Java 21.
    pause
    exit /b 1
)

echo Java found successfully!
echo.

REM Check if config.properties exists and has proper database config
echo Checking database configuration...
if not exist "src\main\resources\config.properties" (
    echo ERROR: config.properties not found!
    echo Please copy config.properties.template to config.properties and configure your database.
    pause
    exit /b 1
)

REM Try to use Maven wrapper with proper environment
echo Attempting to run with Maven wrapper...
call mvnw.cmd clean compile javafx:run
if %ERRORLEVEL% EQU 0 (
    echo Application completed successfully!
    goto :end
)

echo Maven wrapper failed. Trying alternative approach...
echo.

REM Alternative: Try to run with Maven if available
mvn --version >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    echo Maven found! Running with Maven...
    call mvn clean compile javafx:run
    if %ERRORLEVEL% EQU 0 (
        echo Application completed successfully!
        goto :end
    )
)

echo Maven not available. Trying batch system...
echo.

REM Fallback to batch system with proper dependency download
echo Setting up batch build system...
if not exist "target\lib" mkdir target\lib

REM Download JavaFX dependencies properly
echo Downloading JavaFX dependencies...
powershell -Command "& {[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/openjfx/javafx-controls/21.0.2/javafx-controls-21.0.2.jar' -OutFile 'target\lib\javafx-controls-21.0.2.jar'}"
powershell -Command "& {[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/openjfx/javafx-fxml/21.0.2/javafx-fxml-21.0.2.jar' -OutFile 'target\lib\javafx-fxml-21.0.2.jar'}"
powershell -Command "& {[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/openjfx/javafx-base/21.0.2/javafx-base-21.0.2.jar' -OutFile 'target\lib\javafx-base-21.0.2.jar'}"
powershell -Command "& {[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/openjfx/javafx-graphics/21.0.2/javafx-graphics-21.0.2.jar' -OutFile 'target\lib\javafx-graphics-21.0.2.jar'}"
powershell -Command "& {[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/openjfx/javafx-graphics/21.0.2/javafx-graphics-21.0.2-win.jar' -OutFile 'target\lib\javafx-graphics-21.0.2-win.jar'}"

REM Download other dependencies
echo Downloading other dependencies...
powershell -Command "& {[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/com/zaxxer/HikariCP/5.0.1/HikariCP-5.0.1.jar' -OutFile 'target\lib\HikariCP-5.0.1.jar'}"
powershell -Command "& {[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/slf4j/slf4j-api/1.7.36/slf4j-api-1.7.36.jar' -OutFile 'target\lib\slf4j-api-1.7.36.jar'}"
powershell -Command "& {[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/slf4j/slf4j-simple/1.7.36/slf4j-simple-1.7.36.jar' -OutFile 'target\lib\slf4j-simple-1.7.36.jar'}"
powershell -Command "& {[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/mindrot/jbcrypt/0.4/jbcrypt-0.4.jar' -OutFile 'target\lib\jbcrypt-0.4.jar'}"
powershell -Command "& {[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/com/h2database/h2/2.2.224/h2-2.2.224.jar' -OutFile 'target\lib\h2-2.2.224.jar'}"

REM Compile the application
echo Compiling application...
if not exist "target\classes" mkdir target\classes

javac -cp "target\lib\*" -d target\classes src\main\java\com\eventra\*.java src\main\java\com\eventra\controller\*.java src\main\java\com\eventra\dao\*.java src\main\java\com\eventra\model\*.java src\main\java\com\eventra\util\*.java

if %ERRORLEVEL% NEQ 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo Compilation successful!
echo.

REM Run the application
echo Starting Eventra application...
java -cp "target\classes;target\lib\*" --add-modules javafx.controls,javafx.fxml com.eventra.App

:end
echo.
echo Application finished.
pause 