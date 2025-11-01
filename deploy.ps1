#!/usr/bin/env powershell
# Ad2Cause - Build, Install, and Test Deployment Script
# Builds the debug APK, installs on phone via WiFi, and launches the app

$adb = "C:\Users\S\AppData\Local\Android\Sdk\platform-tools\adb.exe"
$projectPath = "c:\Users\S\OneDrive\Dokumente\Programme\Ad2Cause"
$phoneIP = "192.168.68.50"
$phonePort = 5555

# Colors
$colors = @{
    Success = "Green"
    Error = "Red"
    Info = "Cyan"
    Warning = "Yellow"
}

function Write-Status {
    param([string]$Message, [string]$Type = "Info")
    $color = if ($colors.ContainsKey($Type)) { $colors[$Type] } else { "White" }
    Write-Host $Message -ForegroundColor $color
}

# Banner
Write-Host ""
Write-Host "╔════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║  Ad2Cause - Build & Deploy Script     ║" -ForegroundColor Cyan
Write-Host "╚════════════════════════════════════════╝" -ForegroundColor Cyan
Write-Host ""

# Step 1: Verify ADB connection
Write-Status "Step 1: Verifying ADB WiFi connection..." -Type Info
$devices = & $adb devices | findstr "device"
if ($devices -match "$phoneIP") {
    Write-Status "✅ Phone connected at $phoneIP" -Type Success
} else {
    Write-Status "⚠️  Phone not found. Attempting to reconnect..." -Type Warning
    & $adb connect "${phoneIP}:${phonePort}"
    Start-Sleep -Seconds 2
}

Write-Host ""

# Step 2: Build APK
Write-Status "Step 2: Building debug APK..." -Type Info
Write-Host "This may take 2-5 minutes on first build..."
Write-Host ""

Set-Location $projectPath
./gradlew clean assembleDebug

if ($LASTEXITCODE -ne 0) {
    Write-Status "❌ Build failed!" -Type Error
    exit 1
}

Write-Status "✅ Build successful!" -Type Success
Write-Host ""

# Step 3: Verify APK exists
Write-Status "Step 3: Verifying APK..." -Type Info
$apkPath = "$projectPath\app\build\outputs\apk\debug\app-debug.apk"

if (-not (Test-Path $apkPath)) {
    Write-Status "❌ APK not found at $apkPath" -Type Error
    exit 1
}

$apkSize = (Get-Item $apkPath).Length / 1MB
Write-Status "✅ APK found (Size: $([Math]::Round($apkSize, 2)) MB)" -Type Success
Write-Host ""

# Step 4: Install on device
Write-Status "Step 4: Installing app on device..." -Type Info
& $adb install -r $apkPath

if ($LASTEXITCODE -ne 0) {
    Write-Status "❌ Installation failed!" -Type Error
    exit 1
}

Write-Status "✅ Installation successful!" -Type Success
Write-Host ""

# Step 5: Launch app
Write-Status "Step 5: Launching Ad2Cause..." -Type Info
& $adb shell am start -n ch.heuscher.ad2cause/.MainActivity

Start-Sleep -Seconds 2
Write-Status "✅ App launched!" -Type Success
Write-Host ""

# Step 6: Show device info
Write-Status "Step 6: Device info" -Type Info
& $adb shell getprop ro.product.model | ForEach-Object { Write-Host "Device: $_" }
& $adb shell getprop ro.build.version.release | ForEach-Object { Write-Host "Android: $_" }
Write-Host ""

# Step 7: Show next steps
Write-Status "═══════════════════════════════════════" -Type Info
Write-Status "Deployment Complete! 🚀" -Type Success
Write-Status "═══════════════════════════════════════" -Type Info
Write-Host ""
Write-Host "Next steps:"
Write-Host "  1. Select a cause in the Causes tab"
Write-Host "  2. Return to Home tab"
Write-Host "  3. Tap '📺 Watch Standard Ad' button"
Write-Host "  4. Watch the ad complete"
Write-Host "  5. Check earnings update!"
Write-Host ""
Write-Host "To view live logs:"
Write-Host "  & '$adb' logcat | findstr 'AdManager|AdViewModel'"
Write-Host ""
Write-Host "To uninstall:"
Write-Host "  & '$adb' uninstall ch.heuscher.ad2cause"
Write-Host ""
