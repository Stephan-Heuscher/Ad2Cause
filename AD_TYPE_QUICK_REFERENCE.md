# Quick Reference - Ad Type Implementation

## Your AdMob Configuration

```
App ID:                    ca-app-pub-5567609971256551~4548078693
Interactive Ad Unit:       ca-app-pub-5567609971256551/1555083848
Non-Interactive Ad Unit:   ca-app-pub-5567609971256551/1251831518
```

## Code Usage

### Load Non-Interactive Ad (Default)
```kotlin
val adManager = AdManager(context)
adManager.loadRewardedAd()  // Uses NON_INTERACTIVE by default
adManager.showRewardedAd(activity)
```

### Load Interactive Ad
```kotlin
val adManager = AdManager(context)
adManager.loadRewardedAd(AdManager.AdType.INTERACTIVE)
adManager.showRewardedAd(activity)
```

### Set User Preference
```kotlin
adManager.setAdType(AdManager.AdType.INTERACTIVE)
adManager.loadRewardedAd()  // Will now use INTERACTIVE
```

### Check Current Ad Type
```kotlin
val currentType = adManager.getCurrentAdType()
Log.d("AdType", "Current: $currentType")  // INTERACTIVE or NON_INTERACTIVE
```

## UI Implementation Options

### Option 1: Settings Toggle
```kotlin
RadioButton(
    selected = userPreference == AdType.INTERACTIVE,
    onClick = { adManager.setAdType(AdType.INTERACTIVE) }
)
Text("Interactive Ads (Earn More)")
```

### Option 2: Dual Buttons
```kotlin
Button(onClick = { watchAd(AdType.INTERACTIVE) }) {
    Text("Watch Interactive Ad")
}
Button(onClick = { watchAd(AdType.NON_INTERACTIVE) }) {
    Text("Watch Standard Ad")
}
```

### Option 3: Info with Selection
```kotlin
Column {
    Text("Choose your ad preference:")
    
    Row {
        RadioButton(
            selected = adType == INTERACTIVE,
            onClick = { setAdType(INTERACTIVE) }
        )
        Column {
            Text("Interactive Ads")
            Text("Higher earnings, more interaction", fontSize = 12.sp)
        }
    }
    
    Row {
        RadioButton(
            selected = adType == NON_INTERACTIVE,
            onClick = { setAdType(NON_INTERACTIVE) }
        )
        Column {
            Text("Standard Ads")
            Text("Easy to watch, quick rewards", fontSize = 12.sp)
        }
    }
}
```

## Testing Checklist

- [ ] App compiles without errors
- [ ] AdManager loads INTERACTIVE ads successfully
- [ ] AdManager loads NON_INTERACTIVE ads successfully
- [ ] Switching between types works
- [ ] Rewards are earned correctly
- [ ] AdMob dashboard shows impressions
- [ ] UI displays ad type selection correctly

## Files to Update

**Frontend UI:**
- `fragment_home.xml` - Add buttons/toggle for ad type selection
- `HomeFragment.kt` - Handle ad type selection clicks

**Preferences (Optional):**
- `PreferenceFragment.kt` - Settings screen for ad type

**Strings:**
- `strings.xml` - Add labels like "Interactive Ads", "Standard Ads"

## Earnings Info

| Ad Type | Relative eCPM | Use Case |
|---------|---------------|----------|
| Interactive | 1.5x - 3x | Higher revenue target |
| Non-Interactive | 1x (baseline) | Accessibility, ease of use |

**Mixed Strategy:** Offer both to users - they'll choose based on their preference!

---

**Status**: ✅ All Ad Unit IDs configured and ready to use
