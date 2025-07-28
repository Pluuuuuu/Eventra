@echo off
echo Building Eventra Application...

REM Create directories
if not exist "target\classes" mkdir target\classes
if not exist "target\lib" mkdir target\lib

echo Downloading JavaFX Controls...
powershell -Command "try { Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/openjfx/javafx-controls/21.0.2/javafx-controls-21.0.2.jar' -OutFile 'target\lib\javafx-controls-21.0.2.jar' } catch { Write-Host 'Failed to download javafx-controls' }"

echo Downloading JavaFX FXML...
powershell -Command "try { Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/openjfx/javafx-fxml/21.0.2/javafx-fxml-21.0.2.jar' -OutFile 'target\lib\javafx-fxml-21.0.2.jar' } catch { Write-Host 'Failed to download javafx-fxml' }"

echo Downloading JavaFX Base...
powershell -Command "try { Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/openjfx/javafx-base/21.0.2/javafx-base-21.0.2.jar' -OutFile 'target\lib\javafx-base-21.0.2.jar' } catch { Write-Host 'Failed to download javafx-base' }"

echo Downloading JavaFX Graphics...
powershell -Command "try { Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/openjfx/javafx-graphics/21.0.2/javafx-graphics-21.0.2.jar' -OutFile 'target\lib\javafx-graphics-21.0.2.jar' } catch { Write-Host 'Failed to download javafx-graphics' }"

echo Downloading HikariCP...
powershell -Command "try { Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/com/zaxxer/HikariCP/5.0.1/HikariCP-5.0.1.jar' -OutFile 'target\lib\HikariCP-5.0.1.jar' } catch { Write-Host 'Failed to download HikariCP' }"

echo Downloading H2 Database...
powershell -Command "try { Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/com/h2database/h2/2.2.224/h2-2.2.224.jar' -OutFile 'target\lib\h2-2.2.224.jar' } catch { Write-Host 'Failed to download H2' }"

echo Downloading BCrypt...
powershell -Command "try { Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/mindrot/jbcrypt/0.4/jbcrypt-0.4.jar' -OutFile 'target\lib\jbcrypt-0.4.jar' } catch { Write-Host 'Failed to download BCrypt' }"

echo Downloading SLF4J API...
powershell -Command "try { Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/slf4j/slf4j-api/1.7.36/slf4j-api-1.7.36.jar' -OutFile 'target\lib\slf4j-api-1.7.36.jar' } catch { Write-Host 'Failed to download SLF4J API' }"

echo Downloading SLF4J Simple...
powershell -Command "try { Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/slf4j/slf4j-simple/1.7.36/slf4j-simple-1.7.36.jar' -OutFile 'target\lib\slf4j-simple-1.7.36.jar' } catch { Write-Host 'Failed to download SLF4J Simple' }"

echo Compiling Java files...
javac -cp "target\lib\*" -d target\classes src\main\java\com\eventra\*.java src\main\java\com\eventra\controller\*.java src\main\java\com\eventra\dao\*.java src\main\java\com\eventra\model\*.java src\main\java\com\eventra\util\*.java

if %ERRORLEVEL% EQU 0 (
    echo Build successful!
    echo Copying resources...
    xcopy /E /I /Y src\main\resources target\classes
    echo You can now run the application using: run.bat
) else (
    echo Build failed!
)

pause 