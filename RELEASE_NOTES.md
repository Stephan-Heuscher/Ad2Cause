# Release Notes

## Version 1.0.2 (Build 3) - 2025-11-21

### Overview
This release focuses on simplifying the app architecture, improving the user experience with enhanced visuals, and strengthening the core functionality around three carefully selected causes.

### New Features

#### Home Screen Enhancements
- **Icon & Description Display**: The home screen now displays the active cause's icon and full description, providing users with better context about where their ad revenue is going
- **Visual Cause Representation**: Added high-quality icons for all three supported causes (Assistive Tap, Rescue Ring, and Safe Home Button)

#### Real Cause Information
- **Authentic Cause Descriptions**: Cause descriptions are now sourced from actual GitHub repositories, ensuring accurate and up-to-date information about each project
- **Project-Based Details**: Updated Assistive Tap and other cause descriptions to reflect real project goals and impact

#### Improved Cause Request System
- **Enhanced Email Template**: Requesting new causes now includes prompts for both title and description, making it easier for users to suggest well-documented causes

#### AdMob Tracking Improvements
- **Server-Side Verification**: Implemented AdMob Server-Side Verification for cause attribution, providing more reliable tracking of ad revenue allocation
- **Attribution Accuracy**: Enhanced tracking ensures accurate revenue distribution to the selected causes

### Major Changes

#### Architecture Simplification
- **Focused Cause Selection**: Streamlined the app to focus on three high-quality, predefined causes rather than user-added causes
- **Removed Firebase Dependencies**: Eliminated all Firebase dependencies (Firestore, Authentication) to simplify the app architecture and reduce external dependencies
- **Category System Removed**: Removed the category-related functionality to simplify the cause selection experience

#### Performance & Stability
- **Local Asset Icons**: Switched from GitHub URLs to local asset icons for faster loading and improved offline experience
- **Database Migration**: Incremented Room database to version 5 to fix crashes and ensure data integrity

### Bug Fixes
- **Database Crash Fix**: Fixed critical crash by properly handling database migrations with Room version 5
- **Import Issues**: Resolved import issues that occurred after Firebase dependencies removal
- **Build Fixes**: Corrected namespace declarations and TextStyle references

### Technical Details

#### Updated Dependencies
- Target SDK: 35 (Android 15)
- Compile SDK: 35
- Room Database: Version 5

#### Removed Dependencies
- Firebase Firestore
- Firebase Authentication
- All related Firebase support libraries

### Breaking Changes
- User-added causes are no longer supported; the app now focuses on three predefined causes
- Category-based browsing has been removed
- Firebase-based authentication and data sync features have been removed

### Migration Notes
For users updating from previous versions:
- The app will automatically migrate to the new database schema (version 5)
- Previously added custom causes will not be available in this version
- Active cause selection will default to "Safe Home Button" if the previously selected cause is no longer available

### Known Limitations
- Currently limited to three predefined causes
- Manual disbursement process (no automated payments)
- Cause suggestions are handled via email rather than in-app submission

### What's Next
Future releases may include:
- Additional carefully vetted causes
- Enhanced statistics and reporting
- Improved cause discovery features
- Payment integration for automated disbursements

---

## Version 1.0.1 and Earlier

For historical reference, earlier versions included:
- Firebase integration for cause management
- User authentication via Google Sign-In
- User-submitted causes with approval workflow
- Category-based cause organization
- Firestore backend for cause data synchronization

---

### Credits
Built with modern Android development practices using Kotlin, Jetpack Compose components, Material Design 3, and AdMob integration.

### Support
For issues or questions, please refer to the README.md or submit an issue through the project repository.
