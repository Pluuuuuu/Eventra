@echo off
echo Running Eventra Application...

REM Set JavaFX modules
set JAVAFX_MODULES=javafx.controls,javafx.fxml

REM Run the application
java --module-path "target\lib" --add-modules %JAVAFX_MODULES% -cp "target\classes;target\lib\*" com.eventra.App

pause 