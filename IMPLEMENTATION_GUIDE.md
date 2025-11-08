# Firebase Backend Implementation Guide

This guide explains what has been implemented and what you need to do to complete the Firestore backend integration for Ad2Cause.

## What Has Been Implemented

### 1. Data Models
- **User**: Firestore model for user authentication and profile data
- **Cause**: Updated to support approval workflow with status (PENDING/APPROVED/REJECTED)
- **EarningHistory**: Tracks individual ad watch events and earnings
- **FirestoreCause**: Cloud version of Cause model for Firestore serialization

### 2. Repositories
- **AuthRepository**: Handles Google Sign-In and Firebase Authentication
- **FirebaseRepository**: Manages Firestore operations for causes and earnings
- **CauseRepository**: Updated with Firestore sync methods

### 3. ViewModels
- **AuthViewModel**: Manages authentication state and user data

### 4. Database Migration
- Room database upgraded from v1 to v2
- Added new fields: `firestoreId`, `status`, `createdBy`, `createdAt`, `approvedAt`, `approvedBy`

### 5. Security Rules
- `firestore.rules`: Firestore security rules for data protection
- `storage.rules`: Firebase Storage rules for image uploads

### 6. Configuration Files
- Updated build.gradle files with Firebase dependencies
- Updated AndroidManifest with permissions
- Added string resources for authentication UI
- Created `.gitignore` entry for `google-services.json`

## What You Need to Do

### Step 1: Firebase Project Setup

Follow the instructions in **FIREBASE_SETUP.md** to:

1. Create a Firebase project
2. Register your Android app
3. Download `google-services.json` and place it in `app/` directory
4. Enable Firebase Authentication (Google Sign-In)
5. Enable Cloud Firestore
6. Enable Firebase Storage
7. Deploy security rules

### Step 2: Update Web Client ID

After downloading `google-services.json`, extract the web client ID:

1. Open `app/google-services.json`
2. Find the `oauth_client` section with `"client_type": 3`
3. Copy the `client_id` value
4. Open `app/src/main/res/values/strings.xml`
5. Replace `YOUR_WEB_CLIENT_ID_FROM_FIREBASE` with your actual Web Client ID:

```xml
<string name="default_web_client_id" translatable="false">YOUR_ACTUAL_WEB_CLIENT_ID</string>
```

### Step 3: Update UI Components

The backend is ready, but you need to update the UI to integrate authentication and image uploads. Here's what needs to be done:

#### A. Add Sign-In UI to MainActivity

Create a sign-in dialog or screen:

```kotlin
// In MainActivity.kt or a new SignInFragment
class MainActivity : AppCompatActivity() {
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize AuthViewModel
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        // Check if user is signed in
        lifecycleScope.launch {
            authViewModel.isSignedIn.collect { isSignedIn ->
                if (!isSignedIn) {
                    showSignInPrompt()
                }
            }
        }
    }

    private fun showSignInPrompt() {
        // Show a dialog or redirect to sign-in screen
        // Use authViewModel.getGoogleSignInClient() to get the client
    }
}
```

#### B. Update CauseViewModel to Use FirebaseRepository

In `CauseViewModel.kt`, initialize with FirebaseRepository:

```kotlin
class CauseViewModel(application: Application) : AndroidViewModel(application) {
    private val database = Ad2CauseDatabase.getDatabase(application)
    private val firebaseRepository = FirebaseRepository()
    private val repository = CauseRepository(database.causeDao(), firebaseRepository)

    // Add sync function
    fun syncCausesFromCloud() {
        viewModelScope.launch {
            repository.syncCausesFromFirestore()
        }
    }
}
```

#### C. Update AddCauseDialog to Include Image Upload

Modify the Add Cause dialog/screen to:

1. Add image picker button
2. Upload image to Firebase Storage before creating cause
3. Pass image URL to `createUserCause()`

Example:

```kotlin
// In CausesFragment.kt or AddCauseDialog
private fun showAddCauseDialog() {
    // ... existing code ...

    // Add image picker
    val selectImageButton = dialog.findViewById<Button>(R.id.selectImageButton)
    var selectedImageUri: Uri? = null

    selectImageButton.setOnClickListener {
        // Launch image picker
        imagePickerLauncher.launch("image/*")
    }

    saveButton.setOnClickListener {
        if (selectedImageUri == null) {
            Toast.makeText(context, R.string.image_required, Toast.LENGTH_SHORT).show()
            return@setOnClickListener
        }

        // Upload image first
        viewLifecycleOwner.lifecycleScope.launch {
            val result = firebaseRepository.uploadCauseImage(selectedImageUri!!)
            if (result.isSuccess) {
                val imageUrl = result.getOrNull()!!
                // Create cause with image URL
                causesViewModel.createUserCause(name, description, category, imageUrl)
            }
        }
    }
}
```

#### D. Add Sync on App Launch

In your main Activity or Application class:

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Sync causes from Firestore on app launch
        val causeViewModel = ViewModelProvider(this)[CauseViewModel::class.java]
        causeViewModel.syncCausesFromCloud()
    }
}
```

#### E. Update Earnings Recording

When a user watches an ad, record it in Firestore:

```kotlin
// In HomeFragment.kt or wherever you handle ad rewards
private fun onAdWatchComplete(amount: Double) {
    val userId = authViewModel.getCurrentUserId()
    val activeCause = causeViewModel.activeCause.value

    if (userId != null && activeCause != null && activeCause.firestoreId != null) {
        lifecycleScope.launch {
            // Record in Firestore
            firebaseRepository.recordEarning(
                userId = userId,
                causeId = activeCause.firestoreId!!,
                causeName = activeCause.name,
                amount = amount,
                adType = AdType.NON_INTERACTIVE
            )

            // Update user's total earned
            authViewModel.updateTotalEarned(amount)

            // Update local cause earnings
            causeViewModel.updateActiveCauseEarnings(amount)
        }
    }
}
```

#### F. Admin Cause Approval UI (Optional)

Create an admin screen to approve pending causes:

```kotlin
// In a new AdminFragment.kt
class AdminFragment : Fragment() {
    private lateinit var authViewModel: AuthViewModel
    private lateinit var causeViewModel: CauseViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Check if user is admin
        lifecycleScope.launch {
            authViewModel.isAdmin.collect { isAdmin ->
                if (!isAdmin) {
                    // Hide admin features
                    return@collect
                }

                // Show pending causes
                causeViewModel.getPendingCauses().collect { pendingCauses ->
                    // Display in RecyclerView with approve/reject buttons
                }
            }
        }
    }

    private fun approveCause(cause: Cause) {
        val adminUserId = authViewModel.getCurrentUserId() ?: return
        lifecycleScope.launch {
            causeViewModel.approveCause(cause.firestoreId!!, adminUserId)
        }
    }
}
```

### Step 4: Update Layout Files

You'll need to create/update these layout files:

1. **fragment_sign_in.xml**: Sign-in screen with Google Sign-In button
2. **dialog_add_cause.xml**: Update to include image picker button
3. **fragment_admin.xml**: Admin screen for approving causes (optional)
4. **item_cause.xml**: Update to show pending/approved status badges

Example for image picker in add cause dialog:

```xml
<!-- In dialog_add_cause.xml or similar -->
<Button
    android:id="@+id/selectImageButton"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="@string/select_image"
    android:drawableStart="@android:drawable/ic_menu_gallery" />

<ImageView
    android:id="@+id/selectedImagePreview"
    android:layout_width="match_parent"
    android:layout_height="200dp"
    android:scaleType="centerCrop"
    android:visibility="gone" />
```

### Step 5: Testing

1. **Build the app** to ensure everything compiles
2. **Sign in** with a Google account
3. **Create a cause** with an image - it should be PENDING
4. **Sign in as admin** and approve the cause
5. **Watch an ad** and verify earnings sync
6. **Sign out and sign in** on another device - verify data syncs

### Step 6: Deploy Security Rules

Deploy the security rules to Firebase:

```bash
# Install Firebase CLI if not already installed
npm install -g firebase-tools

# Login to Firebase
firebase login

# Initialize Firebase in your project
firebase init

# Select Firestore and Storage when prompted
# Choose the files: firestore.rules and storage.rules

# Deploy the rules
firebase deploy --only firestore:rules,storage:rules
```

Alternatively, deploy via Firebase Console:
1. Go to Firebase Console → Firestore Database → Rules
2. Copy content from `firestore.rules` and publish
3. Go to Storage → Rules
4. Copy content from `storage.rules` and publish

## Architecture Overview

### Data Flow

1. **Authentication**:
   - User signs in with Google → AuthRepository → Firebase Auth
   - User data created/updated in Firestore users collection

2. **Creating Causes**:
   - User picks image → Upload to Storage → Get URL
   - Create cause with status=PENDING → Firestore causes collection
   - Also saved locally in Room database

3. **Approving Causes**:
   - Admin approves → Update Firestore (status=APPROVED)
   - Sync to local database

4. **Watching Ads**:
   - User watches ad → Record in Firestore earnings subcollection
   - Update user's totalEarned in Firestore
   - Update local cause earnings in Room

5. **Syncing**:
   - On app launch → Fetch approved causes from Firestore
   - Update local Room database
   - User data stays in sync via real-time listeners

### Security Model

- **Users**: Can read/write their own data only
- **Causes**:
  - Anyone can read approved causes
  - Users can create causes (become pending)
  - Only admins can approve/reject
- **Earnings**: Users can only read/write their own earnings
- **Images**: Public read, authenticated write

## Common Issues and Solutions

### Issue: Build fails with "google-services.json not found"
**Solution**: Make sure `google-services.json` is in the `app/` directory (not `app/src/`)

### Issue: Sign-in fails with "API not enabled"
**Solution**: Ensure Google Sign-In is enabled in Firebase Console → Authentication → Sign-in method

### Issue: Firestore permission denied
**Solution**:
1. Deploy the security rules from `firestore.rules`
2. Make sure user is signed in
3. Check that the data structure matches the rules

### Issue: Images won't upload
**Solution**:
1. Deploy the storage rules from `storage.rules`
2. Check that Firebase Storage is enabled
3. Ensure image is under 5MB

### Issue: Data not syncing across devices
**Solution**:
1. Verify user is signed in on both devices
2. Check internet connection
3. Look for error logs in Logcat
4. Ensure `syncCausesFromFirestore()` is called on app launch

## Performance Considerations

### Offline Support
The app uses Room database for offline caching:
- Causes are cached locally
- Earnings are tracked locally
- Sync happens when online

### Pagination (Future Enhancement)
For large datasets, consider:
- Implementing pagination in Firestore queries
- Limiting initial sync to recent items
- Loading more on scroll

### Real-time vs Polling
Current implementation uses:
- Real-time listeners for causes (optional)
- Manual sync on app launch
- Consider battery impact of real-time listeners

## Next Steps

1. Complete the UI integration (Steps 3-4)
2. Set up Firebase project (Steps 1-2)
3. Test thoroughly (Step 5)
4. Deploy security rules (Step 6)
5. Consider adding:
   - Push notifications for cause approval
   - Admin dashboard web app
   - Analytics for earnings trends
   - Social sharing features

## Support

If you encounter issues:
1. Check `FIREBASE_SETUP.md` for setup instructions
2. Review Firebase Console logs
3. Check Android Studio Logcat for errors
4. Verify security rules are deployed correctly

## File Reference

### New Files Created
- `app/src/main/java/ch/heuscher/ad2cause/data/models/User.kt`
- `app/src/main/java/ch/heuscher/ad2cause/data/models/EarningHistory.kt`
- `app/src/main/java/ch/heuscher/ad2cause/data/repository/AuthRepository.kt`
- `app/src/main/java/ch/heuscher/ad2cause/data/repository/FirebaseRepository.kt`
- `app/src/main/java/ch/heuscher/ad2cause/viewmodel/AuthViewModel.kt`
- `firestore.rules`
- `storage.rules`
- `FIREBASE_SETUP.md`
- `IMPLEMENTATION_GUIDE.md`

### Modified Files
- `build.gradle` (project-level)
- `app/build.gradle`
- `app/src/main/java/ch/heuscher/ad2cause/data/models/Cause.kt`
- `app/src/main/java/ch/heuscher/ad2cause/data/database/CauseDao.kt`
- `app/src/main/java/ch/heuscher/ad2cause/data/database/Ad2CauseDatabase.kt`
- `app/src/main/java/ch/heuscher/ad2cause/data/repository/CauseRepository.kt`
- `app/src/main/res/values/strings.xml`
- `app/src/main/AndroidManifest.xml`
- `.gitignore`

### Files You Need to Create
- `app/google-services.json` (download from Firebase Console)
- Sign-in UI components
- Image picker UI components
- Admin approval UI (optional)
