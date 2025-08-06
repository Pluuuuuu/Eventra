# FXML Screenshotter Utility

This utility automatically generates PNG screenshots of all FXML files in your Eventra project for documentation purposes.

## What it does

- Scans the `src/main/resources/fxml` directory for all `.fxml` files
- Loads each FXML file and renders it in a JavaFX scene
- Takes a snapshot of each rendered scene
- Saves high-quality PNG images to the `screenshots` directory
- Names each file to match the original FXML filename (e.g., `Login.fxml` → `Login.png`)

## How to run

### Option 1: Using the batch file (Windows)
```bash
run-screenshotter.bat
```

### Option 2: Using PowerShell (Windows)
```powershell
.\run-screenshotter.ps1
```

### Option 3: Using Maven directly
```bash
mvn compile exec:java -Dexec.mainClass="tools.BatchFXMLScreenshotter"
```

### Option 4: From IntelliJ IDEA
1. Open `src/main/java/tools/BatchFXMLScreenshotter.java`
2. Right-click in the editor
3. Select "Run 'BatchFXMLScreenshotter.main()'"

## Output

The utility will create a `screenshots` directory in your project root containing PNG files for each FXML:
- `AdminDashboard.png`
- `AdminManageEvents.png`
- `AttendeeEvents.png`
- `AttendeeProfile.png`
- `CreateEvent.png`
- `Dashboard.png`
- `EventDetails.png`
- `Login.png`
- `MySchedule.png`
- `SignUp.png`
- `Welcome.png`
- And all other FXML files...

## Requirements

- Java 21
- Maven
- JavaFX (already configured in pom.xml)
- A display environment (the utility needs to show windows briefly)

## Notes

- The utility runs headless-style but briefly shows each window to capture the screenshot
- Each FXML is rendered at its natural size based on the layout
- The process is automatic and will exit when complete
- If you add new FXML files, just run the utility again to generate screenshots for them

## Troubleshooting

If you encounter issues:
1. Make sure Maven dependencies are downloaded: `mvn clean compile`
2. Ensure you have a display environment (won't work on headless servers)
3. Check that all FXML files are valid and can be loaded
4. Verify JavaFX is properly configured in your environment 