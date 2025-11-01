# ADB WiFi Testing Guide - Ad2Cause

## Prerequisites

✅ ADB is installed at: `C:\Users\S\AppData\Local\Android\Sdk\platform-tools\adb.exe`

## Step-by-Step Setup

### Step 1: Enable Developer Mode on Your Phone

1. Go to **Settings** → **About Phone**
2. Tap **Build Number** 7 times until you see "You are now a developer"
3. Go back to Settings → **Developer Options** (now visible)
4. Enable:
   - ✅ **USB Debugging**
   - ✅ **Wireless Debugging** (Android 11+)

### Step 2: Initial Connection (USB Cable Required)

First, you need to pair using USB:

```bash
# Set ADB path for easier use
$adb = "C:\Users\S\AppData\Local\Android\Sdk\platform-tools\adb.exe"

# Connect your phone via USB cable

# List connected devices
& $adb devices

# Should show your device like:
# List of attached devices
# ZY2234XXXX          device
```

### Step 3: Connect via WiFi

#### Method A: Using Wireless Debugging (Android 11+) - **EASIEST**

```bash
$adb = "C:\Users\S\AppData\Local\Android\Sdk\platform-tools\adb.exe"

# On your phone:
# 1. Go to Settings → Developer Options
# 2. Tap "Wireless Debugging"
# 3. Tap "Pair device with pairing code"
# 4. You'll see: "Pairing code: XXXX:XXXX"
# 5. Note the IP and pairing code

# In PowerShell:
# Run the pairing command:
& $adb pair <IP>:<PORT> <PAIRING_CODE>

# Example:
# & $adb pair 192.168.1.100:12345 123456

# Once paired, connect:
& $adb connect <IP>:<PORT>

# Example:
# & $adb connect 192.168.1.100:12345
```

#### Method B: Using tcpip (All Android Versions)

```bash
$adb = "C:\Users\S\AppData\Local\Android\Sdk\platform-tools\adb.exe"

# With USB cable connected:
# 1. Enable tcpip mode
& $adb tcpip 5555

# 2. Find your phone's IP address
# Go to: Settings → About Phone → IP Address
# Or: Settings → WiFi → Connected Network → Details

# 3. Connect via WiFi
& $adb connect <PHONE_IP>:5555

# Example:
# & $adb connect 192.168.1.100:5555

# 4. Disconnect USB cable - connection persists!

# 5. Verify connection
& $adb devices
```

---

## Quick Commands

### Set up ADB Path Variable (Recommended)

Add this to your PowerShell profile to use `adb` directly:

```powershell
# Edit PowerShell profile
notepad $PROFILE

# Add this line:
$adb = "C:\Users\S\AppData\Local\Android\Sdk\platform-tools\adb.exe"
Set-Alias -Name adb -Value $adb

# Save and restart PowerShell
```

Or use an alias for this session:

```powershell
$adb = "C:\Users\S\AppData\Local\Android\Sdk\platform-tools\adb.exe"
Set-Alias -Name adb -Value $adb
```

---

## Testing Workflow

### 1. Build the App

```bash
cd c:\Users\S\OneDrive\Dokumente\Programme\Ad2Cause

# Build debug APK
./gradlew assembleDebug
```

### 2. Install via ADB WiFi

```bash
$adb = "C:\Users\S\AppData\Local\Android\Sdk\platform-tools\adb.exe"

# List devices
& $adb devices

# Install APK
& $adb install app/build/outputs/apk/debug/app-debug.apk

# Or with output:
& $adb install -r app/build/outputs/apk/debug/app-debug.apk
```

### 3. Run the App

```bash
$adb = "C:\Users\S\AppData\Local\Android\Sdk\platform-tools\adb.exe"

# Start app
& $adb shell am start -n ch.heuscher.ad2cause/.MainActivity

# Or open it manually on phone
```

### 4. View Logs

```bash
$adb = "C:\Users\S\AppData\Local\Android\Sdk\platform-tools\adb.exe"

# View all logs
& $adb logcat

# Filter by app tag
& $adb logcat | findstr "AdManager"

# Save logs to file
& $adb logcat > logs.txt

# View last 100 lines
& $adb logcat -d | tail -100
```

---

## Complete PowerShell Script

Save this as `test-adb-wifi.ps1`:

```powershell
# ADB WiFi Testing Script for Ad2Cause

$adb = "C:\Users\S\AppData\Local\Android\Sdk\platform-tools\adb.exe"
$projectPath = "c:\Users\S\OneDrive\Dokumente\Programme\Ad2Cause"

function Test-ADBConnection {
    Write-Host "Checking ADB connection..." -ForegroundColor Cyan
    & $adb devices
}

function Build-App {
    Write-Host "Building debug APK..." -ForegroundColor Cyan
    cd $projectPath
    ./gradlew clean assembleDebug
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ Build successful!" -ForegroundColor Green
    } else {
        Write-Host "❌ Build failed!" -ForegroundColor Red
        exit 1
    }
}

function Install-App {
    Write-Host "Installing app via ADB WiFi..." -ForegroundColor Cyan
    $apkPath = "$projectPath\app\build\outputs\apk\debug\app-debug.apk"
    & $adb install -r $apkPath
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ Installation successful!" -ForegroundColor Green
    } else {
        Write-Host "❌ Installation failed!" -ForegroundColor Red
    }
}

function Start-App {
    Write-Host "Starting Ad2Cause..." -ForegroundColor Cyan
    & $adb shell am start -n ch.heuscher.ad2cause/.MainActivity
    Write-Host "✅ App started!" -ForegroundColor Green
}

function View-Logs {
    Write-Host "Viewing app logs..." -ForegroundColor Cyan
    Write-Host "Press Ctrl+C to stop" -ForegroundColor Yellow
    & $adb logcat | findstr "AdManager|CauseViewModel|AdViewModel"
}

# Menu
Write-Host "`n🚀 Ad2Cause ADB WiFi Testing Tool" -ForegroundColor Cyan
Write-Host "=================================" -ForegroundColor Cyan

Write-Host "`nOptions:"
Write-Host "1. Check ADB Connection"
Write-Host "2. Build App"
Write-Host "3. Install App (requires build)"
Write-Host "4. Start App"
Write-Host "5. View Logs"
Write-Host "6. Build + Install + Start (Full Test)"
Write-Host "7. Exit"

$choice = Read-Host "`nEnter choice (1-7)"

switch ($choice) {
    "1" { Test-ADBConnection }
    "2" { Build-App }
    "3" { Install-App }
    "4" { Start-App }
    "5" { View-Logs }
    "6" {
        Build-App
        Install-App
        Start-App
    }
    "7" { Write-Host "Goodbye!" -ForegroundColor Green; exit }
    default { Write-Host "Invalid choice" -ForegroundColor Red }
}
```

**Run it:**
```bash
powershell -ExecutionPolicy Bypass -File test-adb-wifi.ps1
```

---

## Troubleshooting

### Issue: "ADB not found"
**Solution**: Use full path: `C:\Users\S\AppData\Local\Android\Sdk\platform-tools\adb.exe`

### Issue: "Device not found"
**Solution**:
1. Check USB cable connection
2. Enable USB Debugging on phone
3. Authorize the computer on phone prompt
4. Try: `adb kill-server` then `adb devices`

### Issue: "Installation failed" 
**Solution**:
1. App may already be installed - use `adb install -r` (replace)
2. Check phone has enough storage
3. Verify APK exists at the path
4. Try: `adb uninstall ch.heuscher.ad2cause` first

### Issue: WiFi connection drops
**Solution**:
1. Keep phone on same WiFi network
2. Phone screen must be on initially
3. Check firewall isn't blocking ADB port 5555
4. Reconnect: `adb connect <IP>:5555`

### Issue: "Device offline"
**Solution**:
```bash
$adb = "C:\Users\S\AppData\Local\Android\Sdk\platform-tools\adb.exe"
& $adb kill-server
& $adb devices
```

---

## Quick Test Checklist

- [ ] **Developer Mode Enabled** on phone
- [ ] **USB Debugging** enabled
- [ ] **Wireless Debugging** enabled (if Android 11+)
- [ ] **ADB devices** shows your phone
- [ ] **APK built** successfully
- [ ] **App installed** via ADB
- [ ] **App starts** on phone
- [ ] **Select a cause** in the app
- [ ] **Tap "Watch Standard Ad"** button
- [ ] **Ad loads** successfully
- [ ] **Watch ad completes**
- [ ] **Reward appears** ($0.01)
- [ ] **Earnings updated** in UI
- [ ] **Tap "Interactive Ad"** button
- [ ] **Different ad loads**
- [ ] **Check AdMob Dashboard** for impressions

---

## Testing Scenarios

### Scenario 1: Standard Ad Flow
```
1. Open Ad2Cause
2. Navigate to Causes tab
3. Select a cause
4. Return to Home
5. Tap "📺 Watch Standard Ad"
6. Ad loads (Non-Interactive Unit)
7. Watch completes
8. Reward: $0.01
9. Earnings update
```

### Scenario 2: Interactive Ad Flow
```
1. On Home screen
2. Tap "⭐ Interactive Ad (Earn More)"
3. Ad loads (Interactive Unit)
4. Interact with ad (if available)
5. Watch completes
6. Reward: $0.01
7. Earnings update
```

### Scenario 3: Add Cause Flow
```
1. Navigate to Causes tab
2. Tap + button
3. Enter cause name
4. Enter description
5. Enter category
6. Save
7. New cause appears in list
8. Select it
9. Set as active
10. Verify on Home screen
```

---

## Real Device Testing Benefits

✅ Test actual network conditions  
✅ See real ad performance  
✅ Test on actual Android version  
✅ Monitor real device logs  
✅ Test WiFi connectivity  
✅ See exact user experience  

---

## Next Steps

1. **Set up WiFi connection** using one of the methods above
2. **Build and install** the app
3. **Test both ad types**
4. **Monitor logcat** for errors
5. **Check AdMob dashboard** for impressions
6. **Verify earnings** are tracked

---

## Commands Reference

```bash
$adb = "C:\Users\S\AppData\Local\Android\Sdk\platform-tools\adb.exe"

# Connection
& $adb devices                           # List devices
& $adb connect 192.168.1.100:5555       # Connect via WiFi
& $adb disconnect                        # Disconnect
& $adb kill-server                       # Restart ADB

# Building & Installing
& $adb install app.apk                   # Install app
& $adb install -r app.apk                # Reinstall app
& $adb uninstall ch.heuscher.ad2cause    # Uninstall app

# Running
& $adb shell am start -n ch.heuscher.ad2cause/.MainActivity    # Start app
& $adb shell am force-stop ch.heuscher.ad2cause                # Stop app

# Logs
& $adb logcat                            # View live logs
& $adb logcat -c                         # Clear logs
& $adb logcat -d > logs.txt              # Save logs to file
& $adb logcat | findstr "AdManager"      # Filter logs

# Files
& $adb push local.txt /sdcard/           # Push file to phone
& $adb pull /sdcard/file.txt local.txt   # Pull file from phone

# Debugging
& $adb shell getprop ro.build.version.release  # Get Android version
& $adb shell getprop net.hostname               # Get device name
```

---

**Status**: Ready to test on real device via WiFi  
**Next Action**: Follow the setup steps above and connect your phone
