#!/usr/bin/env powershell
# ADB WiFi Quick Setup for Ad2Cause
# This script helps connect your phone via WiFi and test the app

$adb = "C:\Users\S\AppData\Local\Android\Sdk\platform-tools\adb.exe"
$projectPath = "c:\Users\S\OneDrive\Dokumente\Programme\Ad2Cause"

# Colors for output
$colors = @{
    Success = "Green"
    Error = "Red"
    Info = "Cyan"
    Warning = "Yellow"
}

function Write-Status {
    param([string]$Message, [string]$Type = "Info")
    $color = $colors[$Type] ?? "White"
    Write-Host $Message -ForegroundColor $color
}

# Banner
Write-Host ""
Write-Host "╔════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║  Ad2Cause - ADB WiFi Testing Setup    ║" -ForegroundColor Cyan
Write-Host "╚════════════════════════════════════════╝" -ForegroundColor Cyan
Write-Host ""

# Check ADB
Write-Status "Step 1: Checking ADB..." -Type Info
if (Test-Path $adb) {
    Write-Status "✅ ADB found at: $adb" -Type Success
} else {
    Write-Status "❌ ADB not found!" -Type Error
    exit 1
}

Write-Host ""
Write-Status "Step 2: Checking for connected devices..." -Type Info
Write-Host "Output of 'adb devices':"
& $adb devices
Write-Host ""

# Ask for WiFi setup
Write-Status "Step 3: WiFi Connection Setup" -Type Info
Write-Host ""
Write-Host "Prerequisites on your phone:"
Write-Host "  1. Go to Settings → Developer Options"
Write-Host "  2. Enable: USB Debugging"
Write-Host "  3. Enable: Wireless Debugging (if Android 11+)"
Write-Host ""

$setupChoice = Read-Host "Is your phone connected via USB and ready? (Y/n)"

if ($setupChoice -eq "n" -or $setupChoice -eq "N") {
    Write-Status "Please connect your phone via USB first, then run this script again." -Type Warning
    exit 0
}

Write-Host ""
Write-Status "Found connected device! Setting up WiFi..." -Type Info

# Get phone IP
Write-Host ""
Write-Host "Phone's local IP address:"
Write-Host "On your phone, go to:"
Write-Host "  Settings → WiFi → Connected Network → IP Address"
Write-Host ""

$phoneIP = Read-Host "Enter your phone's IP address (e.g., 192.168.1.100)"

if (-not $phoneIP) {
    Write-Status "No IP provided. Exiting." -Type Error
    exit 1
}

Write-Host ""
Write-Status "Enabling WiFi ADB on port 5555..." -Type Info
& $adb tcpip 5555

Write-Host ""
Write-Status "Connecting to $phoneIP via WiFi..." -Type Info
& $adb connect "${phoneIP}:5555"

Write-Host ""
Write-Status "Verifying connection..." -Type Info
& $adb devices

Write-Host ""
Write-Status "You can now disconnect the USB cable!" -Type Success

# Ask to build and install
Write-Host ""
$buildChoice = Read-Host "Would you like to build and install the app now? (y/N)"

if ($buildChoice -eq "y" -or $buildChoice -eq "Y") {
    Write-Host ""
    Write-Status "Building debug APK..." -Type Info
    cd $projectPath
    ./gradlew clean assembleDebug
    
    if ($LASTEXITCODE -eq 0) {
        Write-Status "✅ Build successful!" -Type Success
        
        Write-Host ""
        Write-Status "Installing app..." -Type Info
        $apkPath = "$projectPath\app\build\outputs\apk\debug\app-debug.apk"
        & $adb install -r $apkPath
        
        if ($LASTEXITCODE -eq 0) {
            Write-Status "✅ Installation successful!" -Type Success
            
            Write-Host ""
            Write-Status "Starting Ad2Cause..." -Type Info
            & $adb shell am start -n com.ad2cause/.MainActivity
            Write-Status "✅ App launched on your phone!" -Type Success
        } else {
            Write-Status "❌ Installation failed!" -Type Error
        }
    } else {
        Write-Status "❌ Build failed!" -Type Error
    }
}

Write-Host ""
Write-Status "═══════════════════════════════════════" -Type Info
Write-Status "Setup Complete! 🚀" -Type Success
Write-Status "═══════════════════════════════════════" -Type Info
Write-Host ""
Write-Host "Next steps:"
Write-Host "  1. Open Ad2Cause on your phone"
Write-Host "  2. Go to Causes tab and select a cause"
Write-Host "  3. Return to Home tab"
Write-Host "  4. Tap 'Watch Standard Ad' button"
Write-Host "  5. Watch the ad complete"
Write-Host "  6. Check your earnings!"
Write-Host ""
Write-Host "For live logs, run:"
Write-Host "  & '$adb' logcat | findstr 'AdManager'"
Write-Host ""
