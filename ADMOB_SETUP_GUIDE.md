# AdMob Configuration Guide

## Overview

This guide walks you through setting up Google AdMob for the Ad2Cause application.

---

## 🔑 Two Configuration Options

### Option 1: Development/Testing (Recommended First)
Use Google's test Ad Unit IDs - no AdMob account setup needed

### Option 2: Production
Set up AdMob account and use real Ad Unit IDs

---

## 📋 Option 1: Development Setup (Test IDs)

### When to Use
- During development and testing
- In debug builds
- Before going to production

### Test Ad Unit ID
```
ca-app-pub-3940256099942544/5224354917
```

### How to Configure

#### Step 1: Open AdManager.kt
File: `app/src/main/java/com/ad2cause/ads/AdManager.kt`

#### Step 2: Find the Rewarded Ad Unit ID
Look for this line (around line 33):
```kotlin
private const val REWARDED_AD_UNIT_ID = "ca-app-pub-xxxxxxxxxxxxxxxx/yyyyyyyyyyyyyy"
```

#### Step 3: Replace with Test ID
Change it to:
```kotlin
private const val REWARDED_AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917"
```

#### Step 4: Build and Run
The app will now use test ads when you run it.

### What to Expect
- Ads will load quickly
- Ads are actual Google test ads
- Can click "Complete the Ad" to simulate completion
- No real revenue generated
- No AdMob account needed

---

## 🚀 Option 2: Production Setup

### Prerequisites
1. Google Account
2. Google AdMob Account (register at https://admob.google.com)
3. Payment information (may be required)
4. App with final package name

### Step 1: Create AdMob Account
1. Go to https://admob.google.com
2. Sign in with your Google Account
3. Follow the setup wizard
4. Accept terms and agreements

### Step 2: Create AdMob App
1. Click "Apps" in left menu
2. Click "Add App"
3. Select "Android"
4. Enter app name (e.g., "Ad2Cause")
5. Click "Create"

### Step 3: Get Your App ID
After creating the app:
1. You'll see your **AdMob App ID** (format: `ca-app-pub-xxxxxxxxxxxxxxxx~yyyyyyyyyy`)
2. Copy this ID
3. Keep it safe - you'll need it for AndroidManifest.xml

### Step 4: Create Ad Unit
1. Still in your app's AdMob dashboard
2. Click "Ad Units"
3. Click "Create new ad unit"
4. Select "Rewarded"
5. Enter ad unit name (e.g., "Main Rewarded Ad")
6. Click "Create Ad Unit"
7. Copy the **Ad Unit ID** (format: `ca-app-pub-xxxxxxxxxxxxxxxx/yyyyyyyyyyyyyy`)

### Step 5: Update Your App

#### Update AdManager.kt
File: `app/src/main/java/com/ad2cause/ads/AdManager.kt`

Find this line:
```kotlin
private const val REWARDED_AD_UNIT_ID = "ca-app-pub-xxxxxxxxxxxxxxxx/yyyyyyyyyyyyyy"
```

Replace with your Ad Unit ID:
```kotlin
private const val REWARDED_AD_UNIT_ID = "ca-app-pub-1234567890123456/1234567890"
```

#### Update AndroidManifest.xml
File: `app/src/main/AndroidManifest.xml`

Find this section:
```xml
<meta-data
    android:name="com.google.android.gms.ads.APPLICATION_ID"
    android:value="ca-app-pub-xxxxxxxxxxxxxxxx~yyyyyyyyyy" />
```

Replace with your App ID:
```xml
<meta-data
    android:name="com.google.android.gms.ads.APPLICATION_ID"
    android:value="ca-app-pub-1234567890123456~0987654321" />
```

### Step 6: Build Release APK
1. Build → Generate Signed Bundle/APK
2. Follow the signing wizard
3. This is required for Play Store

---

## 🧪 Testing Ad Functionality

### Using Test IDs (Before Production)

#### What You'll See
1. Open the app
2. Go to Home screen
3. Select a cause (if not already selected)
4. Tap "Watch Video Ad" button
5. Test ad dialog appears
6. Click to complete ad
7. Reward earned: $0.01

#### Troubleshooting Test Ads
| Issue | Solution |
|-------|----------|
| Ad doesn't load | Check your test ID is correct |
| "Invalid request" error | Verify Ad Unit ID format |
| Ad shows forever | Check internet connection |
| App crashes | Check logcat for errors |

### Using Production IDs (After AdMob Setup)

#### Prerequisites
1. AdMob account set up
2. Ad Unit IDs configured in code
3. Real Ad Unit IDs in build (not test IDs)
4. May take a few hours for ads to appear

#### Wait Time
- First ads may take 1-2 hours to appear
- This is normal AdMob processing
- Check AdMob dashboard to see if ad unit is active

#### If Ads Don't Show
1. Check AdMob dashboard:
   - Is the ad unit active?
   - Does it show any impressions?
2. Check app code:
   - Is the correct Ad Unit ID in code?
   - Was the app rebuilt after changing IDs?
3. Wait longer - can take 24 hours sometimes

---

## 🔄 Switching Between Test and Production

### To Use Test Ads (Development)
```kotlin
// In AdManager.kt
private const val REWARDED_AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917"
```

### To Use Production Ads
```kotlin
// In AdManager.kt
private const val REWARDED_AD_UNIT_ID = "your-production-ad-unit-id"
```

**Important**: Change this before building release APK!

---

## 📊 Google Test Ad Unit IDs

### Complete List of Test IDs

| Ad Type | Test Ad Unit ID |
|---------|-----------------|
| Rewarded | ca-app-pub-3940256099942544/5224354917 |
| Interstitial | ca-app-pub-3940256099942544/1033173712 |
| Banner | ca-app-pub-3940256099942544/6300978111 |

### Test Device IDs

If you want to serve test ads from your device:
1. Run app once with test Ad Unit ID
2. Check logcat for your device ID
3. Add to AdMob account as test device (optional)

---

## ⚠️ Important Considerations

### Before Releasing to Play Store
- [ ] Use REAL Ad Unit IDs (not test)
- [ ] Have AdMob account fully set up
- [ ] Tested ad functionality thoroughly
- [ ] Read AdMob policies and guidelines
- [ ] Ensure app complies with AdMob requirements
- [ ] Set up payment method in AdMob

### AdMob Policy Compliance
The app must NOT:
- Click ads programmatically
- Force users to watch ads
- Reward clicks instead of ad completion
- Use misleading ad placements
- Violate personal data policies

**Ensure you read and comply with:**
https://support.google.com/admob/answer/6001347

### Revenue Timeline
- Earnings tracked in real-time
- Payments typically monthly
- Must reach $100 minimum threshold
- Payments processed to associated AdSense account

---

## 🆘 Troubleshooting AdMob Setup

### Issue: "No ad configuration" error
**Solution**: 
1. Verify Ad Unit ID is correct format
2. Check AdMob dashboard - is ad unit active?
3. Try test Ad Unit ID first
4. Check internet connection

### Issue: Ad loads but doesn't display
**Solution**:
1. Check if ad impression was counted in AdMob
2. Try reloading app
3. Check phone internet connection
4. Try with test Ad Unit ID

### Issue: No impressions in AdMob dashboard
**Solution**:
1. Verify Ad Unit ID is in code
2. Rebuild app after changing ID
3. Clear app cache (Settings → Apps → Ad2Cause → Clear Cache)
4. Wait up to 24 hours for AdMob to process

### Issue: Ad Unit ID syntax error
**Check format**:
- Should be: `ca-app-pub-XXXXXXXXXXXXXXXX/YYYYYYYYYYYYYY`
- 16 X's and 10 Y's for this rewarded ad type
- No spaces or extra characters

### Issue: App ID in manifest is wrong
**Check format**:
- Should be: `ca-app-pub-XXXXXXXXXXXXXXXX~YYYYYYYYYY`
- 16 X's and 10 Y's
- Note: Uses TILDE (~) not SLASH (/)

---

## 📚 Useful Links

- [AdMob Console](https://admob.google.com)
- [AdMob Help Center](https://support.google.com/admob)
- [Google Mobile Ads SDK Docs](https://developers.google.com/admob/android/quick-start)
- [Ad Formats Guide](https://support.google.com/admob/answer/6128738)
- [Policy Guidelines](https://support.google.com/admob/answer/6001347)

---

## 🎓 Next Steps

1. **Testing Phase**
   - Use test Ad Unit IDs
   - Test all ad functionality
   - Verify rewards are calculated correctly

2. **Setup Phase**
   - Create AdMob account
   - Get production Ad Unit IDs
   - Update app code

3. **Build Phase**
   - Create signed APK
   - Test on real device
   - Verify ads work

4. **Release Phase**
   - Submit to Play Store
   - Monitor AdMob dashboard
   - Optimize ad placement

---

## 💡 Pro Tips

1. **Test ID**: Keep a test ID version for internal testing
2. **Version Control**: Don't commit production IDs to public repos
3. **Separate Builds**: Create debug and release build variants
4. **Monitor**: Check AdMob dashboard regularly for performance
5. **Update**: Google's test IDs may change - check documentation

---

## 📝 Quick Reference

### For Development
```kotlin
// Use test ID
private const val REWARDED_AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917"
```

### For Production
```kotlin
// Use your real ID from AdMob
private const val REWARDED_AD_UNIT_ID = "your-actual-ad-unit-id-from-admob"
```

**Don't forget to update AndroidManifest.xml too!**

---

**Happy earning! 💰**

Remember: Test thoroughly with test IDs before going live with production IDs.
