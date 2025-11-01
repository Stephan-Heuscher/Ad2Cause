# Quick Start Guide - Ad2Cause

## 🚀 Immediate Next Steps

### Step 1: Open in Android Studio
1. Launch Android Studio
2. Select "Open" (or File → Open)
3. Navigate to the `Ad2Cause` folder
4. Click Open

### Step 2: Sync Gradle
- Android Studio will prompt you to sync. Click **"Sync Now"**
- Wait for the Gradle sync to complete (check the bottom status bar)

### Step 3: Configure AdMob (CRITICAL FOR TESTING)

#### Option A: Use Test Ad Unit IDs (Development)
1. Open `app/src/main/java/com/ad2cause/ads/AdManager.kt`
2. Uncomment the test AD Unit ID:
```kotlin
// Test ID - Use this for development
private const val REWARDED_AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917"
```

#### Option B: Use Your Production IDs (After Setup)
1. Go to [Google AdMob Console](https://admob.google.com)
2. Create an app and get your Ad Unit ID
3. Replace in `AdManager.kt`:
```kotlin
private const val REWARDED_AD_UNIT_ID = "your-actual-id-here"
```
4. Also update in `AndroidManifest.xml`:
```xml
<meta-data
    android:name="com.google.android.gms.ads.APPLICATION_ID"
    android:value="ca-app-pub-xxxxxxxxxxxxxxxx~yyyyyyyyyy" />
```

### Step 4: Build the Project
```
Ctrl + F9 (or Build → Make Project)
```

### Step 5: Run on Emulator or Device
```
Shift + F10 (or Run → Run 'app')
```

---

## 📱 Testing the App

### First Launch
- App initializes with 10 sample causes
- One cause is automatically set as active (stored in SharedPreferences)

### Testing Ad Flow
1. Go to **Home** tab
2. If no cause selected, tap a button to see error message
3. If cause selected, you'll see earnings total
4. Tap **"Watch Video Ad"** button
5. Ad loads and displays
6. Watch it to completion
7. Earn $0.01 for the active cause
8. Toast notification confirms reward

### Testing Cause Selection
1. Go to **Causes** tab
2. View list of all causes (10 pre-populated + any you add)
3. Tap a cause card to see details
4. Tap **"Set as Active Cause"** button
5. Return to **Home** tab to see new active cause

### Testing Add Cause
1. Go to **Causes** tab
2. Tap the **+** (FAB) button
3. Fill in cause details:
   - Name
   - Category
   - Description
4. Tap **Save**
5. New cause appears in list

### Testing Stats
1. Go to **Stats** tab
2. See total earnings across all causes
3. Watch ads and see total update

---

## 🗂️ Project Structure at a Glance

```
Ad2Cause/
├── .github/copilot-instructions.md
├── .gitignore
├── README.md
├── COMPLETION_SUMMARY.md
├── build.gradle (root)
├── gradle.properties
├── settings.gradle
│
└── app/
    ├── build.gradle
    ├── proguard-rules.pro
    ├── src/main/
    │   ├── AndroidManifest.xml
    │   ├── java/com/ad2cause/
    │   │   ├── MainActivity.kt
    │   │   ├── ads/AdManager.kt
    │   │   ├── data/
    │   │   │   ├── database/
    │   │   │   ├── models/
    │   │   │   └── repository/
    │   │   ├── ui/
    │   │   │   ├── adapters/
    │   │   │   └── screens/
    │   │   └── viewmodel/
    │   └── res/
    │       ├── layout/
    │       ├── values/
    │       ├── menu/
    │       ├── navigation/
    │       └── drawable/
```

---

## 🎯 Key Files to Know

### Configuration
- **AndroidManifest.xml** - App permissions and AdMob setup
- **build.gradle** - Dependencies
- **gradle.properties** - Build properties

### Core Logic
- **MainActivity.kt** - Entry point
- **AdManager.kt** - Ad loading and display
- **CauseViewModel.kt** - Main business logic

### UI
- **HomeFragment.kt** - Watch ads here
- **CausesFragment.kt** - Browse causes
- **CauseDetailFragment.kt** - View cause details

### Database
- **Ad2CauseDatabase.kt** - Database singleton
- **CauseDao.kt** - Database queries
- **CauseRepository.kt** - Data abstraction

---

## 🧪 Common Testing Scenarios

### Scenario 1: Watch an Ad
1. Ensure a cause is active (visible on Home screen)
2. Tap "Watch Video Ad"
3. Test ad displays (or shows error if not configured)
4. Complete ad viewing
5. Check earnings updated

### Scenario 2: Switch Active Cause
1. Go to Causes tab
2. Select different cause
3. Go to Home tab
4. Watch ad
5. Earnings now go to new cause

### Scenario 3: Add Custom Cause
1. Go to Causes tab
2. Tap FAB (+)
3. Enter: Name, Category, Description
4. Tap Save
5. Cause appears in list

### Scenario 4: Search Causes
1. Go to Causes tab
2. Type in search (ready for implementation)
3. List filters based on name

---

## 🔍 Important Code Sections

### Loading an Ad
```kotlin
// In AdManager.kt
adManager.loadRewardedAd()
```

### Showing an Ad
```kotlin
// In HomeFragment.kt
if (adManager.isAdReady()) {
    adManager.showRewardedAd(requireActivity())
}
```

### Handling Reward
```kotlin
// In HomeFragment.kt
adManager.onRewardEarned = { rewardAmount ->
    causeViewModel.updateActiveCauseEarnings(rewardAmount)
}
```

### Setting Active Cause
```kotlin
// In CauseDetailFragment.kt
causeViewModel.setActiveCause(cause)
```

### Observing Active Cause
```kotlin
// In HomeFragment.kt
lifecycleScope.launch {
    causeViewModel.activeCause.collect { cause ->
        binding.activeCauseName.text = cause?.name ?: "No cause"
    }
}
```

---

## ⚠️ Common Issues & Solutions

### Issue: Build fails with "Cannot resolve androidx"
**Solution**: Sync Gradle (`File → Sync Now`)

### Issue: Ad doesn't show
**Solution**: 
- Check AdMob configuration
- Use test Ad Unit ID for development
- Check logcat for error messages

### Issue: Database empty
**Solution**: 
- Clear app data (Settings → Apps → Ad2Cause → Storage → Clear)
- Rebuild and reinstall

### Issue: Navigation broken
**Solution**: 
- Verify nav_graph.xml exists
- Check fragment IDs in navigation actions

### Issue: Images not loading
**Solution**: 
- Check network connectivity
- Verify placeholder URLs are accessible
- Check logcat for image loading errors

---

## 📊 Database Queries

### Get all causes
```kotlin
repository.getAllCauses()  // Returns Flow<List<Cause>>
```

### Get active cause
```kotlin
repository.getCauseById(causeId)
```

### Add earnings
```kotlin
repository.updateCauseEarnings(causeId, 0.01)
```

### Search causes
```kotlin
repository.searchCausesByName("water")
```

---

## 🚀 Build Variants

### Debug Build (Development)
```bash
./gradlew assembleDebug
```
- Debuggable
- No code shrinking
- Useful for development

### Release Build (Production)
```bash
./gradlew assembleRelease
```
- Requires signing
- Code shrinking enabled
- For Play Store

---

## 📋 Pre-Launch Checklist

- [ ] AdMob configuration complete
- [ ] Android manifest updated
- [ ] Ad loads and displays
- [ ] Reward calculated correctly ($0.01)
- [ ] Database initializes with sample data
- [ ] Active cause persists on app restart
- [ ] Navigation works between all screens
- [ ] Images load properly
- [ ] No crashes on rapid clicks
- [ ] Tested on minimum SDK (API 26)
- [ ] Tested on target SDK (API 34)

---

## 📞 Getting Help

### Where to Look
1. **Logcat** (at bottom of Android Studio) - See error messages
2. **README.md** - Detailed project information
3. **COMPLETION_SUMMARY.md** - Project overview
4. **Code comments** - Extensive inline documentation
5. **Android Documentation** - General Android help

### Check These First
- Is Gradle synced?
- Is your SDK updated?
- Are all permissions in manifest?
- Is AdMob configured?
- Is the emulator running?

---

## 🎓 Next Learning Steps

After getting the app running:
1. Read through MainActivity.kt to understand initialization
2. Look at CauseViewModel.kt to see MVVM pattern
3. Check AdManager.kt to understand ad integration
4. Study Fragment implementations for navigation
5. Explore Room database setup

---

## 🎯 Quick Commands

| Action | Command |
|--------|---------|
| Build | `Ctrl + F9` |
| Run | `Shift + F10` |
| Debug | `Shift + F9` |
| Stop | `Ctrl + F2` |
| Clean | `Build → Clean Project` |
| Rebuild | `Build → Rebuild Project` |
| Sync Gradle | `File → Sync Now` |

---

## 💡 Pro Tips

1. **Test Mode**: Use Google's test Ad Unit ID during development
2. **LogCat**: Monitor logcat while testing to see debug messages
3. **Hot Reload**: Use "Apply Changes" to test UI modifications
4. **Emulator**: Use API 30+ emulator for better performance
5. **Breakpoints**: Set breakpoints in code for debugging

---

**You're all set! Happy developing! 🚀**

If you have questions, refer to the inline code comments—they explain every important section.
