SETLOCAL
pushd %~dp0\..
SET COSMICMOD_RELEASEMODE=DEV
call gradlew build
pause