@echo off
echo Running Eventra Application...

REM Set JavaFX modules and native library path
set JAVAFX_MODULES=javafx.controls,javafx.fxml
set JAVAFX_PATH=target\javafx-sdk\javafx-sdk-21.0.2\lib

REM Run the application with proper JavaFX module path
java --module-path "%JAVAFX_PATH%" --add-modules %JAVAFX_MODULES% -cp "target\classes;target\lib\*" com.eventra.App

pause 