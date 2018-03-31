SETLOCAL
pushd %~dp0\..
SET COSMICMOD_RELEASEMODE=BETA
call gradlew build
pause