# Privacy Policy for Ad2Cause

**Effective Date:** January 7, 2025
**Last Updated:** January 7, 2025

## Introduction

Ad2Cause ("we," "our," or "us") is committed to protecting your privacy. This Privacy Policy explains how we collect, use, disclose, and safeguard your information when you use our mobile application (the "App"). Please read this Privacy Policy carefully.

By using the Ad2Cause app, you agree to the collection and use of information in accordance with this Privacy Policy. If you do not agree with the terms of this Privacy Policy, please do not use the App.

## App Overview

Ad2Cause is a rewarded advertising application that allows users to watch video advertisements and allocate the generated revenue to charitable causes of their choice. Users can browse causes managed centrally on our Firebase backend, select active causes, and optionally sign in with their Google account to synchronize data across multiple devices.

## Information We Collect

### 1. Information You Provide Directly

**Google Account Information (Optional):**
If you choose to sign in with your Google account, we collect:
- Email address
- Google account name
- Profile picture URL
- Unique Google user identifier (UID)

This information is used solely for authentication and data synchronization across your devices. **Signing in is completely optional** – you can use the app without an account, but your data will only be stored locally on your device.

**Cause Interaction Data:**
- Selection of your active charitable cause
- Custom causes you create (if any)
- Earnings accumulated per cause
- Category preferences for causes

### 2. Automatically Collected Information

**App Usage Data:**
- Total earnings accumulated per cause (calculated based on ad views)
- Active cause selection
- Timestamps of cause activations and ad completions

**Device Permissions:**
The App requests the following Android permissions:
- **INTERNET**: Required to load and display advertisements, sync data with Firebase, and authenticate users
- **ACCESS_NETWORK_STATE**: Required to check network connectivity before loading ads and syncing data
- **GET_ACCOUNTS** (Optional): Used only if you choose to sign in with Google Sign-In

### 3. Information Collected by Third Parties

**Google AdMob:**
Our App uses Google AdMob to display rewarded video advertisements. Google AdMob may collect and process the following information:
- Device advertising identifier (Advertising ID)
- IP address
- Device information (model, OS version, screen size)
- Ad interaction data (impressions, clicks, video completion)
- General location information (country/region level)

Google AdMob's data collection and use are governed by [Google's Privacy Policy](https://policies.google.com/privacy) and the [Google Ads Privacy Policy](https://policies.google.com/technologies/ads).

**Google Sign-In:**
If you choose to sign in with Google, authentication is handled by Google Sign-In services. Google's collection and use of your authentication data are governed by [Google's Privacy Policy](https://policies.google.com/privacy).

**Firebase (Google Cloud Platform):**
We use Firebase services to manage causes and synchronize user data:
- **Firebase Authentication**: Manages user sign-in (if you choose to sign in)
- **Firebase Realtime Database/Firestore**: Stores cause data and user preferences (if signed in)
- **Firebase Hosting**: Serves centrally managed cause content

Firebase's data processing is governed by [Google's Privacy Policy](https://policies.google.com/privacy) and [Firebase's Privacy and Security documentation](https://firebase.google.com/support/privacy).

## How We Use Your Information

We use the information collected to:

1. **Provide Core Functionality:**
   - Display centrally managed charitable causes
   - Track earnings per cause
   - Remember your active cause selection
   - Synchronize your data across devices (if signed in)

2. **Authentication and Account Management (Optional):**
   - Authenticate you via Google Sign-In
   - Associate your data with your account for multi-device access
   - Provide personalized experience across devices

3. **Deliver Advertisements:**
   - Load and display rewarded video ads through Google AdMob
   - Calculate hypothetical earnings based on ad completions ($0.01 per ad)

4. **Improve User Experience:**
   - Ensure the App functions properly
   - Optimize ad loading and display
   - Manage and update causes centrally

## Data Storage and Security

**Local Storage (Without Sign-In):**
If you use the app without signing in, your data is stored locally on your device using:
- Room Database (SQLite)
- SharedPreferences

**Cloud Storage (With Google Sign-In):**
If you sign in with your Google account, your data is stored on Firebase servers (Google Cloud Platform) including:
- Your active cause selection
- Custom causes you create
- Earnings per cause
- User preferences

**Data Synchronization:**
When signed in, your data syncs across all devices where you're logged in with the same Google account. Changes made on one device will be reflected on all your devices.

**Security Measures:**
- All data transmitted to Firebase is encrypted using HTTPS/TLS
- Firebase Authentication uses industry-standard OAuth 2.0 protocols
- Access to user data is restricted through Firebase Security Rules
- We implement principle of least privilege for data access
- Firebase infrastructure complies with major security certifications (ISO 27001, SOC 2, SOC 3)

While we implement reasonable security measures, please be aware that no method of transmission over the internet or electronic storage is 100% secure.

## Data Sharing and Disclosure

**We Do Not Sell Your Data:**
We do not sell, trade, or rent your personal information to third parties.

**Service Providers:**
We share data with the following service providers who process data on our behalf:
- **Google Firebase**: For cloud storage, authentication, and backend services
- **Google AdMob**: For serving advertisements
- **Google Sign-In**: For authentication services (optional)

These providers only access data necessary to perform their functions and are contractually obligated to protect your data.

**Third-Party Ad Network:**
Google AdMob may collect and use data as described in their privacy policy for the purpose of serving personalized advertisements. You can manage your ad preferences through your device settings:
- **Android:** Settings > Google > Ads > Opt out of Ads Personalization

**Legal Requirements:**
We may disclose information if required to do so by law or in response to valid requests by public authorities (e.g., court orders, government agencies).

## Children's Privacy

Ad2Cause is not intended for use by children under the age of 13. We do not knowingly collect personal information from children under 13. If you are a parent or guardian and believe your child has provided us with personal information, please contact us, and we will delete such information from our records and Firebase servers.

## Your Data Rights

**Access and Control:**
You have the right to:
- View all causes and earnings data within the App
- Add, edit, or delete custom causes at any time
- Export your data (contact us for assistance)
- Sign out of your Google account at any time

**Data Deletion:**

*Without Google Sign-In:*
- Uninstall the app or clear app data through device settings

*With Google Sign-In:*
- Sign out to disconnect your account
- Request account deletion by contacting us (email below)
- We will delete your data from Firebase within 30 days of your request

To request deletion of your account and all associated data from our Firebase servers, please contact us at the email address provided below.

**Opting Out of Personalized Ads:**
To opt out of personalized advertising:
- **Android:** Settings > Google > Ads > Opt out of Ads Personalization

## International Data Transfers

Your data may be transferred to and processed in countries outside your country of residence, including the United States, where Firebase (Google Cloud Platform) servers are located. These transfers are conducted in accordance with:
- Google's Privacy Policy
- GDPR Standard Contractual Clauses (for EEA users)
- Applicable data protection laws

Firebase maintains data centers in multiple regions. Data residency depends on your Firebase configuration and Google's infrastructure.

## Changes to This Privacy Policy

We may update our Privacy Policy from time to time. We will notify you of any changes by:
- Updating the "Last Updated" date at the top of this Privacy Policy
- Posting the new Privacy Policy in the App and on our repository
- Sending an in-app notification for material changes (if signed in)

You are advised to review this Privacy Policy periodically for any changes. Changes to this Privacy Policy are effective when they are posted.

## Third-Party Links

The App may contain links to third-party websites or services (such as cause websites or donation pages). We are not responsible for the privacy practices of these third-party sites. We encourage you to read the privacy policies of any third-party sites you visit.

## Data Retention

**Local Data (Without Sign-In):**
Data is retained on your device until you:
- Delete a specific cause
- Clear the app's data through device settings
- Uninstall the application

**Cloud Data (With Google Sign-In):**
- Data is retained on Firebase servers for as long as your account is active
- If you don't use the app for 3 years, we may delete your inactive account data
- You can request deletion at any time by contacting us

**AdMob Data:**
Data collected by Google AdMob is retained according to Google's data retention policies. Please refer to [Google's Privacy Policy](https://policies.google.com/privacy) for details.

## Your Consent

By using Ad2Cause, you consent to our Privacy Policy and agree to its terms. If you choose to sign in with Google, you provide additional consent for data synchronization and cloud storage.

## California Privacy Rights (CCPA)

If you are a California resident, you have the right to:
- Know what personal information is collected
- Know whether personal information is sold or disclosed and to whom
- Say no to the sale of personal information (Note: We do not sell personal information)
- Access your personal information
- Request deletion of personal information
- Equal service and price, even if you exercise your privacy rights

To exercise these rights, contact us at the email address below.

## European Privacy Rights (GDPR)

If you are located in the European Economic Area (EEA), you have certain data protection rights, including:
- **Right to access** your personal data
- **Right to rectification** of inaccurate data
- **Right to erasure** ("right to be forgotten")
- **Right to restrict processing**
- **Right to data portability**
- **Right to object** to processing
- **Right to withdraw consent** at any time

To exercise these rights, please contact us using the information below. We will respond to your request within 30 days.

**Legal Basis for Processing (GDPR):**
- **Consent**: When you sign in with Google, you consent to data processing
- **Legitimate Interests**: To provide app functionality and improve services
- **Contractual Necessity**: To deliver the services you request

## Contact Us

If you have any questions or concerns about this Privacy Policy or our data practices, please contact us:

**Developer:** Stephan Heuscher
**Email:** [Your contact email]
**GitHub Repository:** https://github.com/Stephan-Heuscher/Ad2Cause

**For data deletion requests**, please include:
- Your Google account email (if signed in)
- Confirmation of deletion request
- Any specific data you want removed

For issues related to Google services:
- Google AdMob: [Google's Privacy & Terms](https://policies.google.com/)
- Google Sign-In: [Google Account Help](https://support.google.com/accounts)
- Firebase: [Firebase Support](https://firebase.google.com/support/privacy)

---

## Summary of Data Collection

| Data Type | Collected | Purpose | Storage Location |
|-----------|-----------|---------|------------------|
| Google email address | Yes (optional, if signed in) | Authentication, account management | Firebase Authentication |
| Google profile info | Yes (optional, if signed in) | Display name, profile picture | Firebase Authentication |
| Google user ID (UID) | Yes (optional, if signed in) | Unique identifier for your account | Firebase Authentication |
| Cause names & descriptions | Yes (user-provided) | Display causes, track preferences | Firebase Realtime DB/Firestore (if signed in) or Local device |
| Earnings per cause | Yes (calculated) | Track hypothetical donations | Firebase Realtime DB/Firestore (if signed in) or Local device |
| Active cause selection | Yes (user choice) | Remember user preference | Firebase Realtime DB/Firestore (if signed in) or Local device |
| Device identifiers | No | N/A | N/A |
| Precise location data | No | N/A | N/A |
| Contacts | No | N/A | N/A |
| Photos/Media | No | N/A | N/A |
| Advertising data (via AdMob) | Yes (by Google) | Serve ads | Google's servers |
| Analytics data | No | N/A | N/A |

## Third-Party Services

**Google Firebase:**
- Privacy Policy: https://firebase.google.com/support/privacy
- Terms of Service: https://firebase.google.com/terms

**Google Sign-In:**
- Privacy Policy: https://policies.google.com/privacy
- Terms of Service: https://policies.google.com/terms

**Google AdMob:**
- Privacy Policy: https://policies.google.com/privacy
- Ad Settings: https://adssettings.google.com/
- How Google uses data: https://policies.google.com/technologies/partner-sites

---

*This Privacy Policy is designed to comply with Google Play Store requirements, GDPR, CCPA, and other applicable data protection regulations.*
