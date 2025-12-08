@echo off
setlocal enabledelayedexpansion

echo ========================================
echo Wee U Android Build Script
echo ========================================
echo.

REM Check if we're in the right directory
if not exist "src\android" (
    echo ERROR: src\android directory not found!
    echo Please run this script from the root of the WeeU repository.
    pause
    exit /b 1
)

REM Check if vcpkg dependencies are installed
if not exist "dependencies\vcpkg\installed\arm64-android" (
    echo WARNING: vcpkg dependencies not found at dependencies\vcpkg\installed\arm64-android
    echo.
    echo Please ensure you have extracted the vcpkg dependencies:
    echo   1. Run the GitHub Actions workflow "Build vcpkg Android Dependencies"
    echo   2. Download the vcpkg-android-arm64.tar.gz artifact
    echo   3. Extract it: tar -xzf vcpkg-android-arm64.tar.gz -C dependencies\vcpkg\
    echo.
    set /p CONTINUE="Continue anyway? (y/N): "
    if /i not "!CONTINUE!"=="y" (
        echo Build cancelled.
        pause
        exit /b 1
    )
)

REM Check for gettext (msgfmt/msgunfmt)
where msgunfmt >nul 2>&1
if errorlevel 1 (
    echo WARNING: msgunfmt not found in PATH
    echo Translation files will not be processed.
    echo.
    echo To install gettext on Windows:
    echo   - Using Chocolatey: choco install gettext
    echo   - Using Scoop: scoop install gettext
    echo   - Or download from: https://mlocati.github.io/articles/gettext-iconv-windows.html
    echo.
    set SKIP_TRANSLATIONS=1
) else (
    set SKIP_TRANSLATIONS=0
)

REM Process translation files
if "!SKIP_TRANSLATIONS!"=="0" (
    echo.
    echo ========================================
    echo Processing translation files...
    echo ========================================
    
    for /d %%L in (bin\resources\*) do (
        if exist "%%L\cemu.mo" (
            set "language_code=%%~nxL"
            set "po_file=src\android\app\src\main\assets\translations\!language_code!\cemu.po"
            
            echo Processing !language_code!...
            
            if not exist "src\android\app\src\main\assets\translations\!language_code!" (
                mkdir "src\android\app\src\main\assets\translations\!language_code!"
            )
            
            msgunfmt "%%L\cemu.mo" -o "!po_file!" 2>nul
            if errorlevel 1 (
                echo   WARNING: Failed to process %%L\cemu.mo
            ) else (
                echo   Created !po_file!
            )
        )
    )
    echo Translation processing complete.
) else (
    echo Skipping translation processing.
)

REM Set version if provided
set VERSION_MAJOR=%1
set VERSION_MINOR=%2

if not "!VERSION_MAJOR!"=="" (
    if not "!VERSION_MINOR!"=="" (
        echo.
        echo Building version !VERSION_MAJOR!.!VERSION_MINOR!
        set "EMULATOR_VERSION_MAJOR=!VERSION_MAJOR!"
        set "EMULATOR_VERSION_MINOR=!VERSION_MINOR!"
    )
)

REM Check for Java 21
echo.
echo ========================================
echo Checking Java version...
echo ========================================

REM Basic presence check without piping (avoid cmd redirection quirks)
java -version >nul 2>nul
if errorlevel 1 (
    echo ERROR: Java not found in PATH
    echo The build requires Java 21 or higher.
    pause
    exit /b 1
)

REM Parse major version from `java -version` output
set "JAVA_VERSION="
for /f "tokens=3" %%v in ('java -version 2^>^&1 ^| findstr /i "version"') do (
    set "JAVA_VERSION=%%v"
)
set "JAVA_VERSION=!JAVA_VERSION:"=!"
for /f "tokens=1 delims=." %%a in ("!JAVA_VERSION!") do set "JAVA_MAJOR=%%a"

if !JAVA_MAJOR! LSS 21 (
    echo WARNING: Java !JAVA_MAJOR! detected. Java 21 or higher is recommended.
    echo.
    set /p CONTINUE="Continue anyway? (y/N): "
    if /i not "!CONTINUE!"=="y" (
        echo Build cancelled.
        pause
        exit /b 1
    )
) else (
    echo Java !JAVA_MAJOR! detected.
)

REM Set environment variable to use pre-installed vcpkg dependencies
set "VCPKG_ROOT=%CD%\dependencies\vcpkg"
set "ANDROID_NDK_HOME=C:\Android\ndk\29.0.14206865"

echo VCPKG_ROOT set to: %VCPKG_ROOT%
echo ANDROID_NDK_HOME set to: %ANDROID_NDK_HOME%

REM Build the Android app
echo.
echo ========================================
echo Building Android APK...
echo ========================================
echo.

cd src\android

REM Clean previous builds (optional)
set /p CLEAN="Clean previous builds? (y/N): "
if /i "!CLEAN!"=="y" (
    echo Cleaning...
    call gradlew.bat clean
)

echo.
echo Building Release APK...
call gradlew.bat assembleRelease

if errorlevel 1 (
    echo.
    echo ========================================
    echo BUILD FAILED!
    echo ========================================
    cd ..\..
    pause
    exit /b 1
)

echo.
echo ========================================
echo Running tests...
echo ========================================
call gradlew.bat testRelease

if errorlevel 1 (
    echo.
    echo WARNING: Tests failed!
    echo The APK was built but tests did not pass.
)

cd ..\..

echo.
echo ========================================
echo BUILD SUCCESSFUL!
echo ========================================
echo.
echo APK files are located at:
echo   src\android\app\build\outputs\apk\release\app-release.apk
echo   src\android\app\build\outputs\apk\debug\app-debug.apk
echo.

REM List the APK files
if exist "src\android\app\build\outputs\apk\release\*.apk" (
    echo Release APK:
    dir /b "src\android\app\build\outputs\apk\release\*.apk"
    echo.
)

if exist "src\android\app\build\outputs\apk\debug\*.apk" (
    echo Debug APK:
    dir /b "src\android\app\build\outputs\apk\debug\*.apk"
    echo.
)

echo Build complete!
pause
