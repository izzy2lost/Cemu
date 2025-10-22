@echo off
REM Simple Android build script for Wee U
REM Requires: Java 21+, Android SDK/NDK
REM Usage: build_android_simple.bat [version_major] [version_minor]
REM Example: build_android_simple.bat 2 1

setlocal enabledelayedexpansion

echo Building Wee U for Android...
echo.

REM Quick Java check
java -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Java not found in PATH
    exit /b 1
)
echo.

REM Set version if provided
if not "%1"=="" (
    if not "%2"=="" (
        set "EMULATOR_VERSION_MAJOR=%1"
        set "EMULATOR_VERSION_MINOR=%2"
        echo Building version %1.%2
    )
)

REM Process translations if msgunfmt is available
where msgunfmt >nul 2>&1
if not errorlevel 1 (
    echo Processing translations...
    for /d %%L in (bin\resources\*) do (
        if exist "%%L\cemu.mo" (
            set "language_code=%%~nxL"
            if not exist "src\android\app\src\main\assets\translations\!language_code!" mkdir "src\android\app\src\main\assets\translations\!language_code!"
            msgunfmt "%%L\cemu.mo" -o "src\android\app\src\main\assets\translations\!language_code!\cemu.po" 2>nul
        )
    )
)

REM Build
cd src\android
call gradlew.bat assembleRelease
set BUILD_RESULT=%ERRORLEVEL%

if %BUILD_RESULT% equ 0 (
    echo.
    echo Build successful!
    echo APK: src\android\app\build\outputs\apk\release\app-release.apk
) else (
    echo.
    echo Build failed with error code %BUILD_RESULT%
)

cd ..\..
exit /b %BUILD_RESULT%
