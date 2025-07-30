@echo off
echo Downloading JavaFX SDK Bundle...

REM Create directories
if not exist "target\classes" mkdir target\classes
if not exist "target\lib" mkdir target\lib
if not exist "target\javafx-sdk" mkdir target\javafx-sdk

echo Downloading JavaFX SDK...
powershell -Command "try { Invoke-WebRequest -Uri 'https://download2.gluonhq.com/openjfx/21.0.2/openjfx-21.0.2_windows-x64_bin-sdk.zip' -OutFile 'target\javafx-sdk.zip' } catch { Write-Host 'Failed to download JavaFX SDK' }"

echo Extracting JavaFX SDK...
powershell -Command "try { Expand-Archive -Path 'target\javafx-sdk.zip' -DestinationPath 'target\javafx-sdk' -Force } catch { Write-Host 'Failed to extract JavaFX SDK' }"

echo Copying JavaFX JARs to lib directory...
powershell -Command "try { Copy-Item 'target\javafx-sdk\javafx-sdk-21.0.2\lib\*.jar' -Destination 'target\lib\' } catch { Write-Host 'Failed to copy JavaFX JARs' }"

echo Downloading other dependencies...
cd target\lib

powershell -Command "try { Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/com/zaxxer/HikariCP/5.0.1/HikariCP-5.0.1.jar' -OutFile 'HikariCP-5.0.1.jar' } catch { Write-Host 'Failed to download HikariCP' }"

powershell -Command "try { Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/com/h2database/h2/2.2.224/h2-2.2.224.jar' -OutFile 'h2-2.2.224.jar' } catch { Write-Host 'Failed to download H2' }"

powershell -Command "try { Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/mindrot/jbcrypt/0.4/jbcrypt-0.4.jar' -OutFile 'jbcrypt-0.4.jar' } catch { Write-Host 'Failed to download BCrypt' }"

powershell -Command "try { Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/slf4j/slf4j-api/1.7.36/slf4j-api-1.7.36.jar' -OutFile 'slf4j-api-1.7.36.jar' } catch { Write-Host 'Failed to download SLF4J API' }"

powershell -Command "try { Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/slf4j/slf4j-simple/1.7.36/slf4j-simple-1.7.36.jar' -OutFile 'slf4j-simple-1.7.36.jar' } catch { Write-Host 'Failed to download SLF4J Simple' }"

cd ..\..

echo Compiling Java files...
javac -cp "target\lib\*" -d target\classes src\main\java\com\eventra\*.java src\main\java\com\eventra\controller\*.java src\main\java\com\eventra\dao\*.java src\main\java\com\eventra\model\*.java src\main\java\com\eventra\util\*.java

if %ERRORLEVEL% EQU 0 (
    echo Build successful!
    echo Copying resources...
    xcopy /E /I /Y src\main\resources target\classes
    echo You can now run the application using run.bat
) else (
    echo Build failed!
)

pause 