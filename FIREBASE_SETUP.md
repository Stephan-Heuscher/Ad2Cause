# Firebase Setup Instructions for Ad2Cause

## Prerequisites
You need to create a Firebase project and download the `google-services.json` configuration file.

## Steps to Setup Firebase

### 1. Create Firebase Project
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Click "Add project" or select existing project
3. Follow the setup wizard to create your project

### 2. Add Android App to Firebase Project
1. In Firebase Console, click on "Add app" and select Android
2. Enter the following details:
   - **Android package name**: `ch.heuscher.ad2cause`
   - **App nickname**: Ad2Cause (optional)
   - **Debug signing certificate SHA-1**: (See section below to get this)

### 3. Get SHA-1 Certificate Fingerprint
Run this command in your project directory:

```bash
# For debug keystore (development)
keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android

# For release keystore (production)
keytool -list -v -keystore /path/to/your/release/keystore -alias your-key-alias
```

Copy the SHA-1 fingerprint and paste it in the Firebase Console.

### 4. Download google-services.json
1. After registering your app, download the `google-services.json` file
2. Place it in the `app/` directory of this project:
   ```
   Ad2Cause/
   ├── app/
   │   ├── google-services.json  ← Place file here
   │   ├── build.gradle
   │   └── src/
   ```

### 5. Enable Firebase Services

#### Enable Firebase Authentication
1. In Firebase Console, go to **Authentication** → **Sign-in method**
2. Enable **Google** sign-in provider
3. Set your project's public-facing name (e.g., "Ad2Cause")
4. Add your support email

#### Enable Cloud Firestore
1. In Firebase Console, go to **Firestore Database**
2. Click **Create Database**
3. Start in **Test mode** (we'll add security rules later)
4. Choose your Cloud Firestore location (e.g., `us-central1`)

#### Enable Firebase Storage
1. In Firebase Console, go to **Storage**
2. Click **Get Started**
3. Start in **Test mode** (we'll add security rules later)
4. Use the same location as Firestore

### 6. Deploy Security Rules
After testing, deploy the security rules to protect your data:

#### Firestore Security Rules
In Firebase Console → Firestore Database → Rules, paste:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {

    // Helper functions
    function isSignedIn() {
      return request.auth != null;
    }

    function isOwner(userId) {
      return isSignedIn() && request.auth.uid == userId;
    }

    function isAdmin() {
      return isSignedIn() &&
             get(/databases/$(database)/documents/users/$(request.auth.uid)).data.isAdmin == true;
    }

    // Users collection
    match /users/{userId} {
      // Users can read their own document
      allow read: if isOwner(userId);
      // Users can create and update their own document
      allow create, update: if isOwner(userId) &&
                              request.resource.data.isAdmin == false; // Can't make themselves admin
      allow delete: if false; // No deletions allowed
    }

    // Causes collection
    match /causes/{causeId} {
      // Everyone can read approved causes
      allow read: if resource.data.status == 'approved';
      // Admins can read all causes
      allow read: if isAdmin();

      // Only signed-in users can create causes (they start as pending)
      allow create: if isSignedIn() &&
                      request.resource.data.status == 'pending' &&
                      request.resource.data.createdBy == request.auth.uid &&
                      request.resource.data.imageUrl != null; // Must have image

      // Only admins can update causes (for approval)
      allow update: if isAdmin();

      // Only admins can delete causes
      allow delete: if isAdmin();
    }

    // Earning History collection
    match /users/{userId}/earnings/{earningId} {
      // Users can only read their own earnings
      allow read: if isOwner(userId);
      // Only the app backend should create earnings (in production, use Cloud Functions)
      allow create: if isOwner(userId) &&
                      request.resource.data.userId == userId;
      // No updates or deletes
      allow update, delete: if false;
    }
  }
}
```

#### Storage Security Rules
In Firebase Console → Storage → Rules, paste:

```javascript
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {

    // Helper functions
    function isSignedIn() {
      return request.auth != null;
    }

    function isValidImage() {
      return request.resource.size < 5 * 1024 * 1024 && // Max 5MB
             request.resource.contentType.matches('image/.*'); // Must be an image
    }

    // Cause images
    match /causes/{imageId} {
      // Anyone can read approved cause images
      allow read: if true;

      // Only signed-in users can upload images
      allow create: if isSignedIn() && isValidImage();

      // Users can update their own uploaded images
      allow update: if isSignedIn() && isValidImage();

      // Only allow deletion by the uploader
      allow delete: if isSignedIn();
    }
  }
}
```

### 7. Create Admin User
To approve causes, you need at least one admin user:

1. Sign in to the app with a Google account
2. Note the User ID (visible in Firebase Console → Authentication → Users)
3. In Firebase Console → Firestore Database → users collection
4. Find your user document and edit it
5. Add field: `isAdmin: true`

### 8. Build Configuration
The project is already configured with Firebase dependencies. Just sync Gradle:

```bash
./gradlew build
```

## Security Best Practices

1. **Never commit google-services.json to public repositories**
   - Add it to `.gitignore` if needed

2. **Use proper security rules in production**
   - The rules above provide basic security
   - Review and adjust based on your needs

3. **Enable App Check** (recommended for production)
   - Protects against abuse and unauthorized access
   - Set up in Firebase Console → App Check

4. **Monitor Usage**
   - Set up billing alerts in Google Cloud Console
   - Monitor Firestore read/write operations
   - Monitor Storage usage

5. **Backup Your Data**
   - Enable automated Firestore backups
   - Export important data regularly

## Testing

After setup, test the following:
1. Sign in with Google account
2. Create a new cause (should be pending)
3. Check Firestore console for the pending cause
4. Approve the cause as admin
5. Verify cause appears in app
6. Watch an ad and verify earnings are synced

## Troubleshooting

### Build fails with "google-services.json not found"
- Ensure the file is in `app/` directory
- Run `./gradlew clean build`

### Sign-in fails
- Verify SHA-1 fingerprint is added in Firebase Console
- Check that Google sign-in is enabled
- Ensure you're using a valid Google account

### Firestore permission denied
- Check that security rules are deployed
- Verify user is signed in
- Check that the document paths match the rules

### Images won't upload
- Verify Storage is enabled in Firebase Console
- Check Storage security rules are deployed
- Ensure image size is under 5MB

## Additional Resources
- [Firebase Android Documentation](https://firebase.google.com/docs/android/setup)
- [Firestore Security Rules](https://firebase.google.com/docs/firestore/security/get-started)
- [Firebase Authentication](https://firebase.google.com/docs/auth/android/start)
