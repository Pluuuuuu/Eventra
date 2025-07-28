@echo off
echo Downloading JavaFX SDK...

REM Create directories
if not exist "target\lib" mkdir target\lib

REM Download JavaFX SDK 11.0.12
echo Downloading JavaFX SDK 11.0.12...
powershell -Command "Invoke-WebRequest -Uri 'https://download2.gluonhq.com/openjfx/11.0.12/openjfx-11.0.12_windows-x64_bin-sdk.zip' -OutFile 'javafx-sdk.zip'"

echo Extracting JavaFX SDK...
powershell -Command "Expand-Archive -Path 'javafx-sdk.zip' -DestinationPath 'target' -Force"
del javafx-sdk.zip

REM Copy the JavaFX JARs to the lib directory
echo Copying JavaFX JARs...
copy "target\javafx-sdk-11.0.12\lib\*.jar" "target\lib\"

echo JavaFX SDK downloaded successfully!
echo.
echo Now run: .\run-with-sdk.bat
pause 