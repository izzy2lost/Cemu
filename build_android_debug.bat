@echo off
setlocal enabledelayedexpansion

echo ========================================
echo Wee U Android Build Script (Debug Mode)
echo ========================================
echo.

REM Set environment variables
set "VCPKG_ROOT=%CD%\dependencies\vcpkg"
set "ANDROID_NDK_HOME=C:\Android\ndk\29.0.14206865"

echo VCPKG_ROOT: %VCPKG_ROOT%
echo ANDROID_NDK_HOME: %ANDROID_NDK_HOME%
echo.

REM Check prerequisites
if not exist "src\android" (
    echo ERROR: src\android directory not found!
    pause
    exit /b 1
)

if not exist "%ANDROID_NDK_HOME%" (
    echo ERROR: Android NDK not found at %ANDROID_NDK_HOME%
    pause
    exit /b 1
)

if not exist "%VCPKG_ROOT%\installed\arm64-android" (
    echo ERROR: vcpkg arm64-android dependencies not found
    pause
    exit /b 1
)

echo All prerequisites found.
echo.
echo ========================================
echo Building Android APK (Debug)...
echo ========================================
echo.

cd src\android

REM Build debug APK with detailed output
call gradlew.bat assembleDebug --stacktrace --info

if errorlevel 1 (
    echo.
    echo ========================================
    echo BUILD FAILED!
    echo ========================================
    echo.
    echo Check the output above for error details.
    cd ..\..
    pause
    exit /b 1
)

cd ..\..

echo.
echo ========================================
echo BUILD SUCCESSFUL!
echo ========================================
echo.
echo Debug APK location:
echo   src\android\app\build\outputs\apk\debug\app-debug.apk
echo.

if exist "src\android\app\build\outputs\apk\debug\*.apk" (
    dir /b "src\android\app\build\outputs\apk\debug\*.apk"
)

pause
