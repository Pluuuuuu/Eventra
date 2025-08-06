Write-Host "Running FXML Screenshotter..." -ForegroundColor Green
Write-Host "This will generate PNG screenshots of all FXML files in the project." -ForegroundColor Yellow

# Compile and run the screenshotter
mvn compile exec:java -Dexec.mainClass="tools.BatchFXMLScreenshotter" -Dexec.args=""

Write-Host ""
Write-Host "Screenshots saved to the 'screenshots' directory." -ForegroundColor Green
Read-Host "Press Enter to continue" 