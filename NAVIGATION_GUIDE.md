# Ad2Cause - Project Structure & Navigation Guide

## 🎯 Visual Project Map

```
Ad2Cause (Android App)
│
├── 📱 HOME SCREEN (HomeFragment.kt)
│   ├── Display Active Cause
│   ├── Show Total Earnings ($0.XX)
│   ├── Button: "Watch Video Ad"
│   ├── Button: "Engage with Interactive Ad"
│   └── Bottom Navigation Menu
│
├── 📋 CAUSES SCREEN (CausesFragment.kt)
│   ├── Search Bar (ready for implementation)
│   ├── Cause List (RecyclerView)
│   │   └── Cause Cards
│   │       ├── Cause Image
│   │       ├── Cause Name
│   │       └── Category
│   ├── FAB (+) Button
│   │   └── Opens Add Cause Dialog
│   │       ├── Name Input
│   │       ├── Category Input
│   │       ├── Description Input
│   │       └── Save/Cancel Buttons
│   └── Empty State Message
│
├── 📖 CAUSE DETAIL SCREEN (CauseDetailFragment.kt)
│   ├── Cause Image (Full Width)
│   ├── Cause Name
│   ├── Cause Category
│   ├── Cause Description
│   ├── Total Earned Display
│   └── "Set as Active Cause" Button
│
└── 📊 STATS SCREEN (StatsFragment.kt)
    ├── Total Earnings Card ($0.XX)
    └── Placeholder for Future Stats
```

---

## 🗂️ File Organization

### Kotlin Classes by Feature

#### 🎬 Ad Management
```
AdManager.kt
├── initializeMobileAds()      - Initialize AdMob
├── loadRewardedAd()           - Load ad from AdMob
├── showRewardedAd(activity)   - Display ad
├── isAdReady()               - Check if ad loaded
└── Callbacks
    ├── onRewardEarned
    ├── onAdFailedToLoad
    └── onAdClosed
```

#### 🗄️ Database
```
Ad2CauseDatabase.kt
└── singleton instance
    └── causeDao()

CauseDao.kt
├── insertCause(cause)
├── updateCause(cause)
├── deleteCause(cause)
├── getAllCauses()
├── getCauseById(id)
├── searchCausesByName(query)
├── getUserAddedCauses()
└── getPredefinedCauses()

CauseRepository.kt
└── Wrapper around DAO
    ├── Abstracts database operations
    ├── Provides clean API
    └── Easy to mock for testing

Cause.kt
├── @Entity("causes")
├── id (PrimaryKey)
├── name
├── description
├── category
├── imageUrl
├── isUserAdded
└── totalEarned
```

#### 📊 ViewModel
```
CauseViewModel.kt
├── allCauses: Flow<List<Cause>>
├── activeCause: StateFlow<Cause?>
├── setActiveCause(cause)
├── addNewCause(name, description, category)
├── updateActiveCauseEarnings(amount)
├── searchCauses(query)
├── deleteCause(cause)
└── uiEvent: LiveData<UiEvent>

AdViewModel.kt
├── rewardEarnedEvent: LiveData<Double>
├── isAdLoading: LiveData<Boolean>
├── adErrorEvent: LiveData<String>
├── onRewardEarned()
├── onAdLoadingStarted()
├── onAdLoadingCompleted()
└── onAdError(message)
```

#### 🎨 UI Components
```
HomeFragment.kt
├── Observe activeCause
├── Listen for reward events
├── Setup ad buttons
└── Update UI with earnings

CausesFragment.kt
├── Setup RecyclerView with adapter
├── Handle search
├── Show add cause dialog
└── Observe cause list

CauseDetailFragment.kt
├── Load cause by ID
├── Display details
├── Handle set active action
└── Update button state

StatsFragment.kt
├── Calculate total earnings
└── Display stats

CauseAdapter.kt
├── RecyclerView.Adapter<ViewHolder>
├── DiffUtil for efficient updates
└── Item click handling
```

---

## 🔄 Data Flow Diagram

### User Watches Ad
```
User taps "Watch Ad"
    ↓
HomeFragment checks: Is cause selected?
    ↓ YES
AdManager.isAdReady()?
    ↓ YES (or loads first)
AdManager.showRewardedAd(activity)
    ↓
User watches ad
    ↓
onUserEarnedReward callback
    ↓
AdManager.onRewardEarned callback triggered
    ↓
HomeFragment receives reward amount ($0.01)
    ↓
CauseViewModel.updateActiveCauseEarnings()
    ↓
CauseRepository.updateCauseEarnings()
    ↓
CauseDao.updateCause() - Database updated
    ↓
Toast: "Earned $0.01 for [Cause]"
    ↓
Next ad loads automatically
```

### User Selects Different Cause
```
User goes to Causes tab
    ↓
CausesFragment displays all causes
    ↓
User taps cause card
    ↓
Navigation to CauseDetailFragment with cause ID
    ↓
CauseDetailFragment loads cause details
    ↓
User taps "Set as Active Cause"
    ↓
CauseViewModel.setActiveCause(cause)
    ↓
SharedPreferences updated with cause ID
    ↓
StateFlow emits new active cause
    ↓
All fragments observing activeCause update
    ↓
HomeFragment shows new active cause name
    ↓
Future ads now reward new cause
```

---

## 🎬 Fragment Navigation Map

```
Navigation Graph (nav_graph.xml)
│
├── Home Fragment (start destination)
│   ├── Bottom nav to Causes
│   ├── Bottom nav to Stats
│   └── Observes active cause
│
├── Causes Fragment
│   ├── Bottom nav to Home
│   ├── Bottom nav to Stats
│   ├── Navigates to Detail (with cause_id)
│   └── FAB opens add cause dialog
│
├── Cause Detail Fragment
│   ├── Back navigation to previous
│   └── Sets new active cause
│
└── Stats Fragment
    ├── Bottom nav to Home
    └── Bottom nav to Causes
```

---

## 💾 SharedPreferences Structure

```
SharedPreferences: "ad2cause_prefs"
│
└── Keys:
    ├── "active_cause_id" (Int)
    │   └── ID of currently selected cause
    │
    └── (Future: more preferences)
```

---

## 📱 Layout Component Tree

### MainActivity
```
FrameLayout (activity_main.xml)
└── NavHostFragment (Navigation Container)
    └── Hosts all Fragments
        ├── HomeFragment
        ├── CausesFragment
        ├── CauseDetailFragment
        └── StatsFragment
```

### HomeFragment
```
LinearLayout (vertical)
├── ScrollView
│   └── LinearLayout
│       ├── MaterialCardView (Active Cause Card)
│       │   ├── "Supporting: [Cause]" Text
│       │   └── Cause name
│       ├── MaterialCardView (Earnings Card)
│       │   ├── "Your Contribution" Label
│       │   └── Amount ($0.XX) in large text
│       └── LinearLayout (Buttons Center)
│           ├── MaterialButton (Watch Video)
│           └── MaterialButton (Interactive)
└── BottomNavigationView (Home, Causes, Stats)
```

### CausesFragment
```
LinearLayout (vertical)
├── SearchBar
├── RecyclerView
│   └── CauseAdapter
│       └── ItemCauseCard (repeated)
│           ├── ImageView (Cause Image)
│           ├── TextView (Name)
│           └── TextView (Category)
├── TextView (Empty State - hidden initially)
└── FloatingActionButton (+)
    └── Opens dialog_add_cause.xml
```

---

## 🔌 Intent & Event Flow

### MVVM Event Flow
```
UI Action (User click)
    ↓
Fragment method calls ViewModel
    ↓
ViewModel performs business logic
    ↓
ViewModel updates LiveData/StateFlow
    ↓
Fragment observes and updates UI
```

### Example: Watching an Ad
```
1. HomeFragment (UI)
   └── User taps "Watch Video Ad"
       └── Calls adManager.showRewardedAd()
       
2. AdManager (Business Logic)
   └── Shows ad from AdMob
       └── On completion: onRewardEarned callback
       
3. HomeFragment (UI)
   └── Receives callback
       └── Calls causeViewModel.updateActiveCauseEarnings(0.01)
       
4. CauseViewModel (Business Logic)
   └── Calls repository.updateCauseEarnings()
       └── Updates database
       
5. Database (Persistence)
   └── CauseDao.updateCause()
       └── Updates totalEarned field
       
6. ViewModel (Business Logic)
   └── Reloads active cause from database
       └── Updates StateFlow
       
7. Fragment (UI)
   └── Observes StateFlow change
       └── Updates earnings display
       └── Shows toast message
```

---

## 📊 Database Entity Relationships

```
Causes Table
│
├── All 10 sample causes
│   ├── Clean Water (pre-defined, $0.00)
│   ├── Animals (pre-defined, $0.00)
│   ├── Education (pre-defined, $0.00)
│   └── ...
│
└── User-Added Causes
    └── Custom causes
        ├── My Cause 1 (user-added, $0.XX)
        └── My Cause 2 (user-added, $0.XX)

Active Cause Reference
│
└── SharedPreferences: active_cause_id → Points to Cause.id in database
```

---

## 🎨 Material 3 Color Scheme

```
Primary (#FF6750A4)          - Main actions and highlights
Primary Container (#FFEADDFF) - Secondary surface emphasis
Secondary (#FF625B71)        - Support color
Tertiary (#FF7D5260)         - Accent color
Error (#FFB3261E)            - Error states
Background (#FFFBF7FB)       - App background
Surface (#FFFBF7FB)          - Card backgrounds
On Primary (#FFFFFFFF)       - Text on primary
```

---

## 🔐 Data Persistence Strategy

```
Local Storage (Room Database)
├── Causes table
│   ├── All causes (pre-defined + user-added)
│   ├── Each cause has totalEarned field
│   └── Persists indefinitely
│
Session Storage (SharedPreferences)
├── active_cause_id
├── Quickly retrieved on app start
└── Persists across app sessions

Runtime (Memory)
├── ViewModel StateFlow/LiveData
│   └── Reactive updates during session
└── Clears when activity destroyed
```

---

## 🧪 Testing Scenarios Map

### Scenario 1: Fresh Install
```
1. App opens → MainActivity
2. Database initializes (empty check)
3. 10 sample causes inserted
4. First cause set as active (ID saved to prefs)
5. HomeFragment displays first cause
6. User can watch ads or browse causes
```

### Scenario 2: Subsequent Launch
```
1. App opens → MainActivity
2. Database already populated (skip init)
3. ActiveCauseID loaded from prefs
4. HomeFragment shows saved active cause
5. Previous earnings displayed correctly
```

### Scenario 3: Complete Ad Watching
```
1. User on Home with active cause
2. Taps "Watch Video Ad"
3. Ad loads and displays
4. User completes ad
5. Reward earned: $0.01
6. Database updated
7. UI shows new earnings
8. Toast confirmation shown
```

### Scenario 4: Add Custom Cause
```
1. User on Causes tab
2. Taps FAB (+)
3. Dialog opens
4. Enters: Name, Category, Description
5. Taps Save
6. Database inserts new cause
7. List updates to show new cause
8. Can select it as active
```

---

## 📈 Code Organization Benefits

### By Layer
- **UI**: All fragments in `ui/screens`
- **Data**: All database in `data/database`
- **Models**: Entities in `data/models`
- **Repository**: Repository in `data/repository`
- **ViewModels**: Business logic in `viewmodel`
- **External**: AdMob in `ads`

### By Feature
- Ad-related: AdManager, AdViewModel
- Cause-related: CauseViewModel, CauseRepository, CauseFragment*
- Navigation: nav_graph, MainActivity
- UI: All layout files aligned with fragments

### Easy to Extend
- Add new entity → Create in `data/models`
- Add new table → Extend database in `data/database`
- Add new screen → Create fragment in `ui/screens`
- Add new data operations → Extend repository

---

## 🚀 Deployment Checklist Flow

```
Development Phase
├── ✅ Code written and tested
├── ✅ Built with test Ad Unit IDs
└── ✅ Works on emulator

Pre-Production Phase
├── ✅ Replace with real Ad Unit IDs
├── ✅ Update AndroidManifest
├── ✅ Test on real device
└── ✅ Verify ad functionality

Release Phase
├── ✅ Create signed APK
├── ✅ Test signed APK
├── ✅ Prepare Play Store listing
└── ✅ Upload to Play Store

Post-Release
├── ✅ Monitor AdMob dashboard
├── ✅ Track user feedback
├── ✅ Fix any issues
└── ✅ Plan future features
```

---

## 💡 Quick Reference

| Need | Location |
|------|----------|
| Change active cause | CauseViewModel.setActiveCause() |
| Update earnings | CauseViewModel.updateActiveCauseEarnings() |
| Load ad | AdManager.loadRewardedAd() |
| Show ad | AdManager.showRewardedAd() |
| Add cause | CauseViewModel.addNewCause() |
| Search causes | CauseViewModel.searchCauses() |
| Get all causes | CauseRepository.getAllCauses() |
| Update database | CauseDao methods |

---

**This guide helps you navigate the entire project structure! 🗺️**

Start here, then dive into specific files as needed.
