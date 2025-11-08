# Firestore Data Not Showing - Debug Guide

## Issue
Images are uploading to Firebase Storage, but data is not appearing in Firestore database.

## Most Likely Cause: Firestore Security Rules

### 1. Check Your Firestore Security Rules

Go to [Firebase Console](https://console.firebase.google.com):
1. Select your project
2. Click **Firestore Database** in the left menu
3. Click the **Rules** tab

### 2. Verify Your Current Rules

Your rules might look like this (blocking writes):
```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow read, write: if false;  // ❌ This blocks all writes!
    }
  }
}
```

### 3. Update to Allow Authenticated Users

Replace with these rules to allow authenticated users to write:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {

    // Users collection - users can read/write their own data
    match /users/{userId} {
      allow read: if true;  // Anyone can read user profiles
      allow write: if request.auth != null && request.auth.uid == userId;

      // Earnings subcollection
      match /earnings/{earningId} {
        allow read: if request.auth != null && request.auth.uid == userId;
        allow write: if request.auth != null && request.auth.uid == userId;
      }
    }

    // Causes collection
    match /causes/{causeId} {
      allow read: if true;  // Anyone can read causes

      // Only authenticated users can create causes
      allow create: if request.auth != null
                    && request.resource.data.createdBy == request.auth.uid
                    && request.resource.data.status == 'PENDING';

      // Only admins can approve/reject
      allow update: if request.auth != null
                    && get(/databases/$(database)/documents/users/$(request.auth.uid)).data.isAdmin == true;

      // Only admins can delete
      allow delete: if request.auth != null
                    && get(/databases/$(database)/documents/users/$(request.auth.uid)).data.isAdmin == true;
    }
  }
}
```

**Click "Publish" to save the rules.**

---

## Check the Android Logs

I've added comprehensive logging to help debug. After updating rules, test the app and check logcat:

### Filter by Tags:
```
AuthRepository
FirebaseRepository
CauseRepository
RequestCauseFragment
```

### Expected Log Flow for Cause Creation:

```
RequestCauseFragment: Cause created successfully: [Cause Name]
CauseRepository: Creating cause in Firestore: [Cause Name]
CauseRepository: Firestore cause object: FirestoreCause(...)
FirebaseRepository: Creating cause in Firestore for user: [userId]
FirebaseRepository: Cause data to save: name=[name], category=[category], status=PENDING
FirebaseRepository: Cause successfully created with ID: [documentId]
CauseRepository: Cause created in Firestore with ID: [documentId]
CauseRepository: Cause saved locally with ID: [documentId]
```

### If You See Errors:

```
FirebaseRepository: Failed to create cause in Firestore
```

This will show the exact error message from Firebase, likely:
- **Permission Denied** → Update your Firestore rules (see above)
- **Network Error** → Check internet connection
- **Missing Field** → Data model mismatch

---

## Verify User Authentication

### Check User Creation Logs:

When you sign in, you should see:
```
AuthRepository: Creating/updating user in Firestore: [userId]
AuthRepository: User created successfully: [email]
```

Or:
```
AuthRepository: User exists, updating last sign-in
AuthRepository: User updated successfully
```

### If User Creation Fails:

Check Firestore rules allow user creation:
```javascript
match /users/{userId} {
  allow write: if request.auth != null && request.auth.uid == userId;
}
```

---

## Verify Data in Firebase Console

After fixing rules and submitting a cause:

1. Go to **Firestore Database** → **Data** tab
2. You should see:
   ```
   📁 causes
      📄 [random-id-1]
         - name: "Your Cause Name"
         - status: "PENDING"
         - category: "Education"
         - createdBy: "[your-user-id]"
         - imageUrl: "https://firebasestorage..."
         - isUserAdded: true
         - createdAt: [timestamp]

   📁 users
      📄 [your-user-id]
         - email: "your@email.com"
         - displayName: "Your Name"
         - totalEarned: 0
         - activeCauseId: null
         - isAdmin: false
         - createdAt: [timestamp]
         - lastSignInAt: [timestamp]
   ```

---

## Common Issues & Solutions

### Issue: "PERMISSION_DENIED"
**Solution**: Update Firestore security rules (see section 3 above)

### Issue: Images upload but no Firestore data
**Solution**: Storage and Firestore have separate security rules. Check **both**:
- Storage Rules (allows image upload) ✓
- Firestore Rules (blocks data write) ❌ ← Fix this

### Issue: User not created in Firestore
**Solution**: Check `AuthRepository` logs. Verify rules allow user creation.

### Issue: Earnings not recorded
**Solution**:
1. Verify user document exists in Firestore
2. Check earnings subcollection rules allow writes
3. Review logs for `recordEarning` errors

---

## Testing Checklist

- [ ] Firestore security rules updated
- [ ] Rules published in Firebase Console
- [ ] Sign in to app
- [ ] Check logs: User created successfully
- [ ] Request a new cause
- [ ] Check logs: Cause created in Firestore with ID
- [ ] Go to Firebase Console → Firestore → causes collection
- [ ] Verify cause document exists with status: PENDING
- [ ] Watch an ad (if approved causes exist)
- [ ] Check logs: Earning recorded successfully
- [ ] Go to Firebase Console → Firestore → users → [your-id] → earnings
- [ ] Verify earning document exists

---

## Still Not Working?

1. **Enable Firestore Debug Logging** (add to MainActivity.onCreate):
   ```kotlin
   FirebaseFirestore.setLoggingEnabled(true)
   ```

2. **Check Android Logcat** for all Firebase tags:
   ```
   adb logcat | grep -i firebase
   ```

3. **Verify Firebase project configuration**:
   - Check `google-services.json` is in `app/` folder
   - Verify project ID matches Firebase Console

4. **Test with Firebase Emulator** (optional):
   - Use local Firestore emulator
   - See all write attempts in real-time

---

## Next Steps After Fixing

Once data appears in Firestore:

1. **Approve causes** (make yourself admin):
   - Go to Firestore Console
   - Open `users/{your-user-id}`
   - Set `isAdmin: true`
   - In app, you can now approve pending causes

2. **Verify cross-device sync**:
   - Sign in on another device
   - Verify causes and earnings sync

3. **Test earnings tracking**:
   - Watch ads
   - Check Firestore for earning records
   - Verify `totalEarned` updates in user document
