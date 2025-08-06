@echo off
echo Running FXML Screenshotter...
echo This will generate PNG screenshots of all FXML files in the project.

REM Compile and run the screenshotter
mvn compile exec:java -Dexec.mainClass="tools.BatchFXMLScreenshotter" -Dexec.args=""

echo.
echo Screenshots saved to the 'screenshots' directory.
pause 