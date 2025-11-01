# ADB WiFi Quick Reference

## One-Time Setup (5 minutes)

### On Your Phone:
1. **Settings** → **About Phone** → Tap **Build Number** 7x
2. **Settings** → **Developer Options**:
   - ✅ Enable **USB Debugging**
   - ✅ Enable **Wireless Debugging** (Android 11+)

### On Your Computer (PowerShell):

```powershell
# Set ADB path
$adb = "C:\Users\S\AppData\Local\Android\Sdk\platform-tools\adb.exe"

# Connect phone via USB cable first
# Then enable WiFi mode on port 5555:
& $adb tcpip 5555

# Get your phone's IP from: Settings → WiFi → Connected Network → IP
# Then connect via WiFi:
& $adb connect 192.168.1.100:5555  # Replace with your IP

# Verify connection:
& $adb devices
# Should show: 192.168.1.100:5555    device

# Disconnect USB cable now!
```

---

## Testing Workflow

### Build & Install:
```powershell
$adb = "C:\Users\S\AppData\Local\Android\Sdk\platform-tools\adb.exe"
$project = "c:\Users\S\OneDrive\Dokumente\Programme\Ad2Cause"

# Build
cd $project
./gradlew clean assembleDebug

# Install via WiFi
$apk = "$project\app\build\outputs\apk\debug\app-debug.apk"
& $adb install -r $apk

# Start app
& $adb shell am start -n com.ad2cause/.MainActivity
```

### View Logs:
```powershell
$adb = "C:\Users\S\AppData\Local\Android\Sdk\platform-tools\adb.exe"

# Show all logs
& $adb logcat

# Filter for app tags
& $adb logcat | findstr "AdManager"

# Save to file
& $adb logcat -d > app-logs.txt
```

---

## Automated Setup

Run this script for automated setup:

```powershell
powershell -ExecutionPolicy Bypass -File setup-adb-wifi.ps1
```

This will:
1. ✅ Check ADB installation
2. ✅ Verify phone connection
3. ✅ Set up WiFi connection
4. ✅ Build the app
5. ✅ Install on phone
6. ✅ Start the app

---

## Common Issues

| Issue | Solution |
|-------|----------|
| "Device not found" | 1. Check USB cable 2. Enable USB Debugging 3. Authorize phone |
| "Installation failed" | Use: `& $adb install -r app.apk` |
| "WiFi drops" | Keep phone on same network, run `& $adb connect IP:5555` again |
| ADB not found | Use full path: `C:\Users\S\AppData\Local\Android\Sdk\platform-tools\adb.exe` |

---

## Test Checklist

- [ ] Phone in Developer Mode
- [ ] USB Debugging enabled
- [ ] Wireless Debugging enabled
- [ ] Phone connected via WiFi
- [ ] ADB shows device as "device"
- [ ] App builds without errors
- [ ] App installs successfully
- [ ] App launches on phone
- [ ] Can select a cause
- [ ] Can watch both ad types
- [ ] Earnings update correctly

---

**Ready to test!** 🚀
