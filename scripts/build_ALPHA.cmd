SETLOCAL
pushd %~dp0\..
SET COSMICMOD_RELEASEMODE=ALPHA
call gradlew build
pause