@echo off
echo Running Eventra Application...

REM Set JavaFX modules
set JAVAFX_MODULES=javafx.controls,javafx.fxml

REM Run the application
java --module-path "C:\Program Files\Java\javafx-sdk-21.0.2\lib" --add-modules %JAVAFX_MODULES% -cp "target\classes;target\dependency\*" com.eventra.App

pause 