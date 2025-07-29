@echo off
@REM ----------------------------------------------------------------------------
@REM Maven Start Up Batch script for Windows
@REM ----------------------------------------------------------------------------

@REM Required ENV vars:
@REM JAVA_HOME - location of a JDK home dir

@REM Optional ENV vars
@REM M2_HOME - location of maven2's installed home dir
@REM MAVEN_BATCH_ECHO - set to 'on' to enable the echoing of the batch commands
@REM MAVEN_BATCH_PAUSE - set to 'on' to wait for a key stroke before ending
@REM MAVEN_OPTS - parameters passed to the Java VM when running Maven
@REM     e.g. to debug Maven itself, use
@REM set MAVEN_OPTS=-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000
@REM MAVEN_SKIP_RC - flag to disable loading of mavenrc files

@REM ----------------------------------------------------------------------------

@setlocal

set ERROR_CODE=0

set MAVEN_PROJECTBASEDIR=%~dp0

if not "%MAVEN_SKIP_RC%"=="true" (
  for %%i in ("%MAVEN_PROJECTBASEDIR%") do set MAVEN_PROJECTBASEDIR=%%~fsi
  if exist "%MAVEN_PROJECTBASEDIR%\.mvn\mavenrc_pre.bat" call "%MAVEN_PROJECTBASEDIR%\.mvn\mavenrc_pre.bat"
  if exist "%USERPROFILE%\.mavenrc" call "%USERPROFILE%\.mavenrc"
  if exist "%MAVEN_PROJECTBASEDIR%\.mvn\mavenrc.bat" call "%MAVEN_PROJECTBASEDIR%\.mvn\mavenrc.bat"
)

set WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain

set WRAPPER_JAR="%MAVEN_PROJECTBASEDIR%.mvn\wrapper\maven-wrapper.jar"

@REM Fix: use quotes around JAVA_EXE in case JAVA_HOME contains spaces
if not defined JAVA_HOME goto error_no_java_home

set "JAVA_EXE=%JAVA_HOME%\bin\java.exe"
if not exist "%JAVA_EXE%" goto error_no_java_home

@REM Add the Maven wrapper JAR to the classpath
set CLASSW=%WRAPPER_JAR%

@REM Execute the Maven wrapper
"%JAVA_EXE%" %MAVEN_OPTS% -classpath %CLASSW% "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" %WRAPPER_LAUNCHER% %*

goto end

:error_no_java_home
echo.
echo ERROR: JAVA_HOME not found in your environment.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.
echo.
set ERROR_CODE=1
goto end

:end
@endlocal

if "%MAVEN_BATCH_PAUSE%"=="on" pause

exit /b %ERROR_CODE%
