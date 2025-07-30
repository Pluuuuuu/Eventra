# Build System Conflicts Resolution

## Conflicts Identified

### 1. Java Version Mismatch
- **Problem**: Different batch files were using different Java versions
  - `run-app.bat`: Java 11 (`jdk-11`)
  - `build.bat`: Java 21 (`jdk-21`)
  - `run-maven.bat`: Java 21 (`jdk-21`)
  - `pom.xml`: Configured for Java 21

- **Solution**: Standardized all scripts to use Java 21

### 2. Build System Conflicts
- **Batch System**: 
  - Manually downloads JAR dependencies
  - Uses `javac` for compilation
  - Runs with `java -cp` command
  - Stores dependencies in `target/lib/`

- **Maven System**:
  - Uses Maven dependency management
  - Uses Maven for compilation
  - Runs with `javafx:run` plugin
  - Stores dependencies in Maven repository

### 3. Dependency Management Conflicts
- **Problem**: Two different ways of managing dependencies
  - Batch system downloads JARs manually via PowerShell
  - Maven system uses Maven's dependency resolution

## Solutions Implemented

### 1. Unified Runner (`run-unified.bat`)
- Automatically detects if Maven is available
- Falls back to batch system if Maven fails
- Uses consistent Java 21 across all systems
- Provides clear error messages and fallback options

### 2. Maven-Only Runner (`run-maven-only.bat`)
- Pure Maven solution for when you want to use Maven exclusively
- Bypasses batch system entirely
- Requires Maven to be installed

### 3. Fixed Version Conflicts
- Updated `run-app.bat` to use Java 21
- Updated `run-maven.bat` to use Java 21
- Ensured `pom.xml` has all necessary JavaFX dependencies

## Usage Recommendations

### For Development (Recommended)
```bash
run-unified.bat
```
- Automatically chooses the best available build system
- Provides fallback options
- Works regardless of Maven installation status

### For Maven-Only Environment
```bash
run-maven-only.bat
```
- Requires Maven to be installed
- Uses Maven's dependency management
- Cleaner build process

### For Batch-Only Environment
```bash
run-app.bat
```
- Uses the batch build system
- Downloads dependencies manually
- Works without Maven

## Key Changes Made

1. **Standardized Java Version**: All scripts now use Java 21
2. **Added Missing Dependencies**: Added `javafx-base` and `javafx-graphics` to `pom.xml`
3. **Created Unified Runner**: Automatically handles both build systems
4. **Fixed Maven Configuration**: Added exec plugin for easier Maven execution

## Future Recommendations

1. **Choose One Build System**: Decide whether to use Maven or batch system as primary
2. **Remove Unused Scripts**: Clean up redundant batch files
3. **Standardize on Maven**: Consider migrating fully to Maven for better dependency management
4. **Add CI/CD**: Consider adding GitHub Actions or similar for automated builds 