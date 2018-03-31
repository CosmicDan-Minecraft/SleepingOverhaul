SETLOCAL
pushd %~dp0\..
SET COSMICMOD_RELEASEMODE=RELEASE
call gradlew build
pause