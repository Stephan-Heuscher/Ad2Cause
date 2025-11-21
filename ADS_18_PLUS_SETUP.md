# 18+ Ads Button Setup Guide

## Overview

A hidden third button for "Ads 18+" has been added to the home screen. This button is **disabled by default** and can be enabled through app preferences.

## Implementation Details

### Ad Configuration

- **App ID**: `ca-app-pub-5567609971256551~4548078693`
- **Ad Unit ID**: `ca-app-pub-5567609971256551/9232656056`
- **Ad Type**: `ADS_18_PLUS`
- **Button Label**: "🔞 Ads 18+"

### Files Modified

1. **EarningHistory.kt** - Added `ADS_18_PLUS` to `AdType` enum
2. **AdManager.kt** - Added 18+ ad unit ID and handling
3. **HomeFragment.kt** - Added button logic and visibility control
4. **fragment_home.xml** - Added third button (hidden by default)
5. **strings.xml** - Added button text resource
6. **PreferencesManager.kt** (NEW) - Settings management utility

## How to Enable the 18+ Ads Button

The button is hidden by default. To enable it, use the `PreferencesManager`:

### Option 1: Enable programmatically in code

```kotlin
import ch.heuscher.ad2cause.utils.PreferencesManager

// In your Activity or Fragment
val preferencesManager = PreferencesManager(context)
preferencesManager.setAds18PlusEnabled(true)
```

### Option 2: Create a Settings Screen (Recommended)

You can create a settings screen where users can toggle this option:

```kotlin
// In your settings Fragment/Activity
class SettingsFragment : Fragment() {
    private lateinit var preferencesManager: PreferencesManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferencesManager = PreferencesManager(requireContext())

        // Example: Toggle switch for 18+ ads
        binding.enable18PlusAdsSwitch.isChecked = preferencesManager.isAds18PlusEnabled()
        binding.enable18PlusAdsSwitch.setOnCheckedChangeListener { _, isChecked ->
            preferencesManager.setAds18PlusEnabled(isChecked)
            // You may want to notify the HomeFragment to update visibility
        }
    }
}
```

### Option 3: Enable via Debug Menu

For testing purposes, you can add a debug option:

```kotlin
// In your debug menu or developer options
if (BuildConfig.DEBUG) {
    preferencesManager.setAds18PlusEnabled(true)
}
```

## Button Behavior

- **Default State**: Hidden (GONE)
- **When Enabled**: Visible on home screen below the Interactive Ad button
- **Click Behavior**: Same as other ad buttons - requires active cause selection
- **Ad Loading**: Uses same AdManager infrastructure as other ad types
- **Rewards**: Tracked in Firestore with `ADS_18_PLUS` ad type

## Testing

To test the 18+ ads button:

1. Enable the button using one of the methods above
2. Restart the app or navigate back to the home screen
3. The button should appear below "⭐ Interactive Ad (Earn More)"
4. Click the button to load and show the 18+ ad

## Security & Compliance

- Button is hidden by default for privacy and compliance
- Enable only for appropriate users/regions based on your app's requirements
- Consider adding age verification before enabling this feature
- Review Google's AdMob policies for adult content advertising

## PreferencesManager API

```kotlin
// Check if 18+ ads are enabled
val isEnabled: Boolean = preferencesManager.isAds18PlusEnabled()

// Enable 18+ ads
preferencesManager.setAds18PlusEnabled(true)

// Disable 18+ ads
preferencesManager.setAds18PlusEnabled(false)
```

## Next Steps

Consider implementing:

1. A settings screen with toggle for 18+ ads
2. Age verification before enabling the feature
3. User profile settings to persist preference
4. Analytics to track 18+ ad engagement
5. A/B testing to measure impact on revenue

## Google Mobile Ads SDK Integration

The implementation follows the Google Mobile Ads SDK rewarded ad guidelines. The 18+ ad unit ID is properly configured in AdManager and uses the same rewarded ad flow as the other ad types.

For more information, see:
- [Google Mobile Ads SDK Guide](https://developers.google.com/admob/android/quick-start)
- [Rewarded Ads Implementation](https://developers.google.com/admob/android/rewarded)
