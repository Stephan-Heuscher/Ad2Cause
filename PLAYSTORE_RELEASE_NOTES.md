# Ad2Cause — Play Store Release Notes (v1.0.3)

Short (Promotional) — 1 line
--------------------------------
Small actions. Big impact — watch short rewarded ads to support causes you care about. Dark mode & onboarding added.

What’s new (20–160 characters)
--------------------------------
New: Dark mode, first-run walkthrough, improved settings & onboarding, plus SSV-backed ad verification for secure rewards.

Full release notes (for Play Console “What’s new” / Release notes)
--------------------------------
Thank you for supporting causes with Ad2Cause! This release focuses on usability, accessibility and reliability:

- Dark mode: You can now switch to a dark theme from Settings. We respect your viewing preferences — lighter eyes, reduced battery usage on OLED devices.
- Guided onboarding: New users will see a helpful 3-step walkthrough that explains how to select causes and earn rewards. You can re-open the walkthrough anytime from Settings.
- Settings screen: Easily toggle dark mode and restart the onboarding walkthrough.
- Secure ad rewards: Improved Server-Side Verification (SSV) flow for rewarded ads — we added robust logging and verification via Firebase/Firestore so rewards are confirmed securely before being applied.
- Small UX polish: Pre-ad guidance, improved icons, accessibility-focused contrast, and other polish across the app.

Notes for testers & device requirements
--------------------------------
- Minimum Android: 8.0 (API 26)
- Target: Android 14+ (latest SDK)
- Testing SSV (Server‑Side Verification): use your AdMob ad unit configured for SSV callbacks and ensure the backend callback URL is set in AdMob. Test ad units (Google's generic test ad IDs) do not trigger SSV callbacks.

Thanks for using Ad2Cause — we appreciate your feedback. If you hit any issues please send feedback via the app menu or open an issue on the project's GitHub repository.

— The Ad2Cause Team
