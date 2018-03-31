@echo off
SETLOCAL
pushd %~dp0\..
echo If you are updating Forge, make sure you have modified build.properties with the new version string first!
pause
call gradlew.bat setupDecompWorkspace --refresh-dependencies
echo.
echo Done. You can now import/refresh build.gradle in IntelliJ. Be sure to run the genIntellijRuns task if necessary.
pause
