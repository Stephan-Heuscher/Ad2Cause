# Ad2Cause - Reward Ads for Good Causes

A complete Android application that allows users to watch rewarded advertisements and allocate the revenue generated to causes of their choice. The app tracks earnings and enables manual disbursement to causes based on tracked data.

## Project Overview

**Ad2Cause** is an Android application built with modern development practices and Jetpack components. Users can:

- Watch rewarded video ads to earn money for their chosen cause
- Browse and search through a curated list of causes
- Add their own causes
- Track total earnings for each cause
- Switch between different active causes

## Technical Specifications

- **Platform**: Android
- **Minimum SDK**: API Level 26 (Android 8.0 Oreo)
- **Target SDK**: API Level 34 (Latest Stable)
- **Language**: Kotlin
- **Architecture**: MVVM (Model-View-ViewModel)

## Architecture & Dependencies

### Jetpack Components
- **ViewModel**: Manages UI-related data
- **LiveData/StateFlow**: Reactive data holders
- **Room**: Local database persistence
- **Navigation**: Fragment navigation
- **Lifecycle**: Manages lifecycle-aware components

### External Libraries
- **Google Mobile Ads SDK (AdMob)**: Rewarded ad integration
- **Coil**: Image loading library
- **Retrofit**: Networking library (prepared for future use)
- **Material 3**: Modern Material Design components

## Project Structure

```
Ad2Cause/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/ad2cause/
│   │   │   │   ├── MainActivity.kt
│   │   │   │   ├── ads/
│   │   │   │   │   └── AdManager.kt
│   │   │   │   ├── data/
│   │   │   │   │   ├── database/
│   │   │   │   │   │   ├── Ad2CauseDatabase.kt
│   │   │   │   │   │   └── CauseDao.kt
│   │   │   │   │   ├── models/
│   │   │   │   │   │   └── Cause.kt
│   │   │   │   │   └── repository/
│   │   │   │   │       └── CauseRepository.kt
│   │   │   │   ├── ui/
│   │   │   │   │   ├── adapters/
│   │   │   │   │   │   └── CauseAdapter.kt
│   │   │   │   │   └── screens/
│   │   │   │   │       ├── HomeFragment.kt
│   │   │   │   │       ├── CausesFragment.kt
│   │   │   │   │       ├── CauseDetailFragment.kt
│   │   │   │   │       └── StatsFragment.kt
│   │   │   │   └── viewmodel/
│   │   │   │       ├── CauseViewModel.kt
│   │   │   │       └── AdViewModel.kt
│   │   │   ├── res/
│   │   │   │   ├── layout/
│   │   │   │   ├── menu/
│   │   │   │   ├── navigation/
│   │   │   │   ├── drawable/
│   │   │   │   └── values/
│   │   │   └── AndroidManifest.xml
│   ├── build.gradle
│   └── proguard-rules.pro
├── build.gradle
├── settings.gradle
└── README.md
```

## Features

### 1. Home/Dashboard Screen
- Displays the currently active cause
- Shows total earnings for the active cause
- Two buttons for watching ads:
  - "Watch Video Ad" (standard rewarded video)
  - "Engage with Interactive Ad" (interactive rewarded ad)
- Bottom navigation bar for screen navigation

### 2. Causes Screen
- Search bar to filter causes by name
- RecyclerView displaying all causes as Material cards
- Each card shows cause image, name, and category
- Floating Action Button (FAB) to add new causes
- Dialog interface for adding custom causes

### 3. Cause Detail Screen
- Displays full cause information
- Shows cause image, name, description, and category
- Displays total earned for this cause
- "Set as Active Cause" button
- Button disables when the cause is already active

### 4. Stats Screen
- Displays total earnings across all causes
- Placeholder for future statistics features

## Database Schema

### Causes Table
| Column | Type | Description |
|--------|------|-------------|
| id | INT (Primary Key) | Unique identifier |
| name | STRING | Cause name |
| description | STRING | Detailed description |
| category | STRING | Category (e.g., "Environment", "Animals") |
| imageUrl | STRING | URL for cause image |
| isUserAdded | BOOLEAN | Flag for user-added vs. pre-defined |
| totalEarned | DOUBLE | Total earnings for this cause |

## Ad Integration

The app uses **Google AdMob** for rewarded ads. 

### Important Setup Steps:

1. **Replace Placeholder IDs** in `AdManager.kt`:
   ```kotlin
   private const val REWARDED_AD_UNIT_ID = "your-actual-ad-unit-id"
   ```

2. **Update AndroidManifest.xml** with your AdMob App ID:
   ```xml
   <meta-data
       android:name="com.google.android.gms.ads.APPLICATION_ID"
       android:value="ca-app-pub-xxxxxxxxxxxxxxxx~yyyyyyyyyy" />
   ```

3. **For Testing**: Use Google's test Ad Unit IDs:
   - Test Rewarded Ad Unit: `ca-app-pub-3940256099942544/5224354917`

### Ad Reward Logic:
- Each completed ad rewards $0.01
- Rewards are automatically added to the active cause's total
- User receives a confirmation Toast/Snackbar message
- Earnings are persisted to the local Room database

## Shared Preferences

The app uses `SharedPreferences` to store:
- `active_cause_id`: ID of the currently selected active cause

This allows the active cause selection to persist across app sessions.

## Sample Data

The app is pre-populated with 10 sample causes across various categories:
- Environment
- Animals
- Education
- Health
- Humanitarian

## Building and Running

### Prerequisites
- Android Studio (Latest version)
- Android SDK 26+ (API Level 26 and higher)
- Kotlin plugin

### Build Steps

1. **Clone/Open** the project in Android Studio
2. **Sync** Gradle files (`File` → `Sync Now`)
3. **Build** the project (`Build` → `Make Project`)
4. **Run** on emulator or device (`Run` → `Run 'app'`)

### Gradle Commands

```bash
# Build the project
./gradlew build

# Run debug build
./gradlew installDebug

# Create release build
./gradlew assembleRelease
```

## Key Code Sections

### Initializing AdMob
```kotlin
val adManager = AdManager(context)
adManager.initializeMobileAds()
adManager.loadRewardedAd()
```

### Loading and Showing Ads
```kotlin
if (adManager.isAdReady()) {
    adManager.showRewardedAd(activity)
}
```

### Handling Rewards
```kotlin
adManager.onRewardEarned = { rewardAmount ->
    causeViewModel.updateActiveCauseEarnings(rewardAmount)
}
```

### Room Database Operations
```kotlin
val database = Ad2CauseDatabase.getDatabase(context)
val causeDao = database.causeDao()
causeDao.insertCause(newCause)
```

## Future Enhancements

- [ ] Backend integration for cause data synchronization
- [ ] User authentication and accounts
- [ ] Advanced statistics and analytics
- [ ] Payment integration for actual disbursements
- [ ] Push notifications for new causes
- [ ] Cause ratings and reviews
- [ ] Social sharing features
- [ ] Offline mode support

## Testing

The app includes:
- ViewModels with testable coroutine scopes
- Repository pattern for easy mocking
- Flow-based data handling for Rx testing

## Notes for Developers

1. **AdMob Setup**: Ensure you've set up your AdMob account and replaced placeholder IDs before release
2. **Database Migrations**: The current setup uses `fallbackToDestructiveMigration()` for development. Implement proper migrations for production
3. **Image Loading**: Coil is used for image loading with fallback placeholders
4. **Error Handling**: Comprehensive try-catch blocks and error callbacks for ad loading failures
5. **Permissions**: Required permissions are declared in `AndroidManifest.xml`:
   - `android.permission.INTERNET`
   - `android.permission.ACCESS_NETWORK_STATE`

## License

This project is provided as-is for educational and commercial use.

## Support

For issues or questions, please refer to the code comments and Android/Jetpack documentation.

---

**Happy coding! 🚀**

Build meaningful applications that make a difference! 💡
