@echo off
setlocal EnableExtensions
cd /d "%~dp0"
title CU Drive Car Rental System

set "PROJECT_DIR=%~dp0"
set "RUNTIME_DIR=%PROJECT_DIR%runtime"
set "TOMCAT_VERSION=10.1.61"
set "TOMCAT_ARCHIVE=apache-tomcat-%TOMCAT_VERSION%-windows-x64.zip"
set "TOMCAT_URL=https://dlcdn.apache.org/tomcat/tomcat-10/v%TOMCAT_VERSION%/bin/%TOMCAT_ARCHIVE%"
set "TOMCAT_SHA512=33831f4200eda8638bbf9794a99c4666a144e3b66c87933e57ef53ab7cf23e75bc0e71ae2ddd338c838c674b94d8e673179e5015a0c5500f90f2e992820b52f5"
set "DATA_DIR=%USERPROFILE%\car-rental-data"

where java >nul 2>nul
if errorlevel 1 (
  echo ERROR: Java was not found. Install Java 17 and add it to PATH.
  pause
  exit /b 1
)
if not defined JAVA_HOME (
  for /f "delims=" %%J in ('where java') do if not defined JAVA_EXE set "JAVA_EXE=%%J"
  for %%J in ("%JAVA_EXE%") do set "JAVA_HOME=%%~dpJ.."
)
if not exist "%JAVA_HOME%\bin\java.exe" (
  echo ERROR: JAVA_HOME does not point to a JDK. Set JAVA_HOME to your Java 17 folder.
  pause
  exit /b 1
)
set "PATH=%JAVA_HOME%\bin;%PATH%"
for /f "tokens=3" %%V in ('java -version 2^>^&1 ^| findstr /i "version"') do set "JAVA_VERSION=%%~V"
for /f "tokens=1 delims=." %%M in ("%JAVA_VERSION%") do set "JAVA_MAJOR=%%M"
if not defined JAVA_MAJOR (
  echo ERROR: Could not determine the Java version. Install Java 17 or later.
  pause
  exit /b 1
)
if %JAVA_MAJOR% LSS 17 (
  echo ERROR: Java 17 or later is required. Found Java %JAVA_VERSION%.
  pause
  exit /b 1
)

where mvn >nul 2>nul
if not errorlevel 1 (
  echo Building the car rental application...
  call mvn -q clean package
  if errorlevel 1 (
    echo ERROR: Maven build failed. See the output above.
    pause
    exit /b 1
  )
) else (
  if not exist "%PROJECT_DIR%target\car-rental.war" (
    echo ERROR: Maven is not installed and the built WAR is missing.
    echo Install Maven or restore target\car-rental.war, then run this file again.
    pause
    exit /b 1
  )
  echo Maven was not found; using the existing target\car-rental.war.
)

set "TOMCAT_HOME="
if defined CATALINA_HOME if exist "%CATALINA_HOME%\bin\startup.bat" set "TOMCAT_HOME=%CATALINA_HOME%"
if not defined TOMCAT_HOME if exist "%RUNTIME_DIR%\apache-tomcat-%TOMCAT_VERSION%\bin\startup.bat" set "TOMCAT_HOME=%RUNTIME_DIR%\apache-tomcat-%TOMCAT_VERSION%"

if not defined TOMCAT_HOME (
  echo Tomcat 10.1 was not found. Downloading the official Apache Tomcat %TOMCAT_VERSION% Windows ZIP...
  if not exist "%RUNTIME_DIR%" mkdir "%RUNTIME_DIR%"
  powershell -NoProfile -ExecutionPolicy Bypass -Command "$ErrorActionPreference='Stop'; $ProgressPreference='SilentlyContinue'; $zip='%RUNTIME_DIR%\%TOMCAT_ARCHIVE%'; Invoke-WebRequest -UseBasicParsing -Uri '%TOMCAT_URL%' -OutFile $zip; $sha=[System.Security.Cryptography.SHA512]::Create(); $stream=[System.IO.File]::OpenRead($zip); try { $actual=[BitConverter]::ToString($sha.ComputeHash($stream)).Replace('-','').ToLowerInvariant() } finally { $stream.Dispose(); $sha.Dispose() }; if ($actual -ne '%TOMCAT_SHA512%') { Remove-Item -LiteralPath $zip -Force; throw 'Tomcat SHA-512 check failed.' }; Add-Type -AssemblyName System.IO.Compression.FileSystem; [System.IO.Compression.ZipFile]::ExtractToDirectory($zip,'%RUNTIME_DIR%')"
  if errorlevel 1 (
    echo ERROR: Tomcat download, integrity check, or extraction failed.
    pause
    exit /b 1
  )
  set "TOMCAT_HOME=%RUNTIME_DIR%\apache-tomcat-%TOMCAT_VERSION%"
)

if not exist "%TOMCAT_HOME%\bin\startup.bat" (
  echo ERROR: Tomcat startup.bat was not found in "%TOMCAT_HOME%".
  pause
  exit /b 1
)

if not exist "%DATA_DIR%" mkdir "%DATA_DIR%"
set "CAR_RENTAL_DATA=%DATA_DIR%"
copy /Y "%PROJECT_DIR%target\car-rental.war" "%TOMCAT_HOME%\webapps\car-rental.war" >nul
if errorlevel 1 (
  echo ERROR: Could not copy the application into Tomcat's webapps folder.
  echo Close Tomcat and run start.bat again.
  pause
  exit /b 1
)

set "CATALINA_HOME=%TOMCAT_HOME%"
echo Starting CU Drive...
call "%TOMCAT_HOME%\bin\startup.bat"
if errorlevel 1 (
  echo ERROR: Tomcat did not start. Check that port 8080 is available.
  pause
  exit /b 1
)

echo Waiting for the application to deploy...
timeout /t 8 /nobreak >nul
start "" "http://localhost:8080/car-rental/"
echo.
echo CU Drive is starting at http://localhost:8080/car-rental/
echo XML data folder: %DATA_DIR%
echo Sample admin: admin@admin / admin
echo Sample customer: demo@demo / demo
echo Keep the Tomcat console window open while using the system.
echo Close the Tomcat console window to stop the server.
echo.
pause
endlocal
