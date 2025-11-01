# Ad2Cause Development Workspace

This file documents the setup and development guidelines for the Ad2Cause Android application.

## Project Status

✅ **Complete and functional Android application generated**

### Completed Steps:

1. ✅ **Project Structure** - Full MVVM Android project with Jetpack components
2. ✅ **Database Setup** - Room database with pre-populated sample causes
3. ✅ **UI Screens** - Home, Causes, Cause Detail, and Stats screens
4. ✅ **AdMob Integration** - Rewarded ads with callback handling
5. ✅ **ViewModels** - Reactive data management with StateFlow and LiveData
6. ✅ **Navigation** - Fragment-based navigation with Material 3 design
7. ✅ **Adapters** - RecyclerView adapter with DiffUtil for efficient updates

## Key Files Overview

### Core Application
- `MainActivity.kt` - Entry point, database initialization, navigation setup
- `AndroidManifest.xml` - App configuration and AdMob metadata

### Data Layer
- `Ad2CauseDatabase.kt` - Room database singleton
- `CauseDao.kt` - Database access object
- `CauseRepository.kt` - Repository pattern implementation
- `Cause.kt` - Data model

### ViewModels
- `CauseViewModel.kt` - Manages cause data and selections
- `AdViewModel.kt` - Manages ad state and rewards

### Ad Integration
- `AdManager.kt` - Google AdMob SDK wrapper with callbacks

### UI Layer
- `HomeFragment.kt` - Main screen with ad buttons
- `CausesFragment.kt` - Cause list with search and add functionality
- `CauseDetailFragment.kt` - Detailed cause view
- `StatsFragment.kt` - Statistics display
- `CauseAdapter.kt` - RecyclerView adapter for causes

### Layouts (XML)
- `activity_main.xml` - Main activity layout
- `fragment_home.xml` - Home screen layout
- `fragment_causes.xml` - Causes list layout
- `fragment_cause_detail.xml` - Cause detail layout
- `fragment_stats.xml` - Stats screen layout
- `item_cause_card.xml` - Cause card item layout
- `dialog_add_cause.xml` - Add cause dialog

### Resources
- `strings.xml` - String resources
- `colors.xml` - Material 3 color palette
- `themes.xml` - Material 3 theme styles
- `bottom_nav_menu.xml` - Bottom navigation menu
- `nav_graph.xml` - Navigation graph

## Build Configuration

### Gradle Files
- `build.gradle` (root) - Top-level Gradle configuration
- `app/build.gradle` - App module configuration with dependencies
- `settings.gradle` - Project settings
- `gradle.properties` - Gradle properties
- `app/proguard-rules.pro` - ProGuard rules for code shrinking

## Important Configuration Steps Before Release

### 1. AdMob Setup (CRITICAL)
Edit `app/src/main/java/com/ad2cause/ads/AdManager.kt`:
```kotlin
// Replace with your actual Ad Unit ID from AdMob console
private const val REWARDED_AD_UNIT_ID = "ca-app-pub-xxxxxxxxxxxxxxxx/yyyyyyyyyyyyyy"
```

### 2. AndroidManifest Update
Edit `app/src/main/AndroidManifest.xml`:
```xml
<meta-data
    android:name="com.google.android.gms.ads.APPLICATION_ID"
    android:value="ca-app-pub-xxxxxxxxxxxxxxxx~yyyyyyyyyy" />
```

### 3. Testing
For testing/development, use Google's test Ad Unit ID:
- Test Rewarded Ad Unit: `ca-app-pub-3940256099942544/5224354917`

## Database

### Pre-populated Sample Causes
The application automatically creates and populates 10 sample causes on first launch:
1. Clean Water for Africa
2. Animal Rescue Initiative
3. Education for Children
4. Renewable Energy Projects
5. Medical Research
6. Food Security Program
7. Climate Action
8. Poverty Alleviation
9. Ocean Conservation
10. Mental Health Awareness

### Persistence
- Uses Room database for local persistence
- Falls back to destructive migration for development (change in production)
- SharedPreferences stores active cause ID across sessions

## Development Notes

### Architecture Highlights
- **MVVM Pattern**: Clean separation of concerns
- **Repository Pattern**: Abstracts data layer
- **Flow/LiveData**: Reactive programming
- **StateFlow**: Modern state management
- **DiffUtil**: Efficient list updates

### Ad Reward Flow
1. User taps "Watch Video Ad" button
2. Check if cause is selected (required)
3. AdManager loads rewarded ad via AdMob SDK
4. Ad displays to user
5. On successful completion: `onRewardEarned` callback
6. Reward amount ($0.01) added to active cause
7. UI updates with confirmation message
8. Next ad is loaded for next viewing

### Navigation
- Uses Android Navigation Component
- Fragment-based navigation with back stack management
- Bottom navigation for main tabs

## Dependencies

Key dependencies included:
- Jetpack Core, AppCompat, ConstraintLayout
- Material 3 Design
- Lifecycle, ViewModel, LiveData
- Room Database
- Navigation
- Google Mobile Ads SDK
- Coil (image loading)
- Retrofit (networking)

## Building the Project

```bash
# Sync Gradle
./gradlew clean

# Build debug
./gradlew assembleDebug

# Build release (requires signing)
./gradlew assembleRelease

# Run tests
./gradlew test
```

## Device Requirements

- **Minimum Android Version**: Android 8.0 (API 26)
- **Target Android Version**: Android 14 (API 34)
- **RAM**: 2GB+ recommended
- **Storage**: ~100MB for app + data

## Common Issues & Solutions

### Issue: Gradle sync fails
**Solution**: Update Android Studio to latest version, clear Gradle cache

### Issue: Ad Unit ID not working
**Solution**: Verify AdMob account setup, use test Ad Unit ID for development

### Issue: Database errors
**Solution**: Clear app data, rebuild project

### Issue: Images not loading
**Solution**: Check network connectivity, verify image URLs are accessible

## Next Steps for Production

1. [ ] Register with Google AdMob and get production Ad Unit IDs
2. [ ] Implement proper database migrations (replace destructiveMigration)
3. [ ] Add user authentication if needed
4. [ ] Implement backend API for cause data sync
5. [ ] Add comprehensive error logging
6. [ ] Implement analytics
7. [ ] Add unit and UI tests
8. [ ] Security audit of data handling
9. [ ] Prepare privacy policy and terms of service
10. [ ] Sign and upload APK/AAB to Google Play Store

## Resources

- [Android Jetpack Documentation](https://developer.android.com/jetpack)
- [Google Mobile Ads SDK](https://developers.google.com/admob)
- [Material Design 3](https://m3.material.io/)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)

---

**Development Status**: ✅ Complete and ready for customization
**Last Updated**: 2025
