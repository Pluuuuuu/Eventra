@echo off
echo Running FXML Screenshotter...
echo This will generate PNG screenshots of all FXML files in the project.

REM First, download dependencies if needed
echo Downloading dependencies...
call .\mvnw.cmd dependency:resolve

REM Run the screenshotter with proper JavaFX modules
java --module-path "target/dependency" --add-modules javafx.controls,javafx.fxml,javafx.swing --add-opens javafx.graphics/javafx.scene=ALL-UNNAMED -cp "target/classes;target/dependency/*" tools.BatchFXMLScreenshotter

echo.
echo Screenshots saved to the 'screenshots' directory.
pause 