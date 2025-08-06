@echo off
echo Running Simple FXML Screenshotter...
echo This will generate PNG screenshots of all FXML files in the project.

REM Set JavaFX modules and native library path (same as run.bat)
set JAVAFX_MODULES=javafx.controls,javafx.fxml,javafx.swing
set JAVAFX_PATH=target\javafx-sdk\javafx-sdk-21.0.2\lib

REM Run the screenshotter with proper JavaFX module path
java --module-path "%JAVAFX_PATH%" --add-modules %JAVAFX_MODULES% --add-opens javafx.graphics/javafx.scene=ALL-UNNAMED -cp "target\classes;target\lib\*" tools.SimpleFXMLScreenshotter

echo.
echo Screenshots saved to the 'screenshots' directory.
pause 