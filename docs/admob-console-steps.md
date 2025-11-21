# AdMob Console: SSV Configuration Steps (Visual Guide)

## 🎯 Quick Navigation Path

```
AdMob Console → Apps → [Your App] → App Settings →
Rewarded ad server-side verification → Add callback URL
```

---

## Step-by-Step with Screenshots Guide

### Step 1: Access AdMob Console

1. Go to: **https://apps.admob.com/**
2. Sign in with your Google account
3. You'll see your dashboard

```
┌─────────────────────────────────────┐
│  AdMob by Google               [👤] │
├─────────────────────────────────────┤
│  📱 Apps                             │
│  📊 Reports                          │
│  💰 Payments                         │
│  ⚙️  Settings                        │
└─────────────────────────────────────┘
```

---

### Step 2: Navigate to Your App

Click **"Apps"** in the left sidebar:

```
┌─────────────────────────────────────┐
│  📱 Apps                             │
├─────────────────────────────────────┤
│  All apps (1)                        │
│                                      │
│  ┌──────────────────────────────┐   │
│  │  Ad2Cause                    │   │
│  │  Android • ID: ca-app-pub-xxx│   │
│  │  Status: ✓ Serving           │   │
│  └──────────────────────────────┘   │
│                                      │
└─────────────────────────────────────┘
```

Click on **"Ad2Cause"** (your app name)

---

### Step 3: Open App Settings

You'll see your app overview. Look for **"App settings"**:

```
┌─────────────────────────────────────┐
│  Ad2Cause                            │
├─────────────────────────────────────┤
│  Overview  Ad units  ⚙️ App settings│
│                          ^           │
│                          |           │
│                    Click here        │
└─────────────────────────────────────┘
```

---

### Step 4: Find SSV Section

Scroll down to find **"Rewarded ad server-side verification settings"**:

```
┌─────────────────────────────────────────────┐
│  App settings                                │
├─────────────────────────────────────────────┤
│                                              │
│  App name: Ad2Cause                          │
│  Package name: ch.heuscher.ad2cause          │
│  Store listing: [View in Play Store]        │
│                                              │
│  ─────────────────────────────────────       │
│                                              │
│  🎁 Rewarded ad server-side verification     │
│                                              │
│  Verify rewarded ad impressions on your      │
│  server to help prevent fraud.               │
│                                              │
│  [ + Add callback URL ]                      │
│                                              │
└─────────────────────────────────────────────┘
```

---

### Step 5: Add Your Callback URL

Click **"+ Add callback URL"** button:

```
┌─────────────────────────────────────────────┐
│  Add callback URL                            │
├─────────────────────────────────────────────┤
│                                              │
│  Callback URL *                              │
│  ┌─────────────────────────────────────┐    │
│  │ https://your-domain.com/api/admob/  │    │
│  │ verify-reward                       │    │
│  └─────────────────────────────────────┘    │
│                                              │
│  ⚠️  URL must:                               │
│  • Use HTTPS (not HTTP)                      │
│  • Be publicly accessible                    │
│  • Respond within 5 seconds                  │
│  • Return HTTP 200 for valid requests        │
│                                              │
│  [ Test URL ]  [ Cancel ]  [ Save ]          │
│                                              │
└─────────────────────────────────────────────┘
```

---

### Step 6: Test Your URL

Click **"Test URL"** to verify it's accessible:

```
┌─────────────────────────────────────────────┐
│  Testing callback URL...                     │
├─────────────────────────────────────────────┤
│                                              │
│  ✅ URL is accessible                        │
│  ✅ Returns HTTP 200                         │
│  ✅ Response time: 143ms                     │
│                                              │
│  Your callback URL is configured correctly.  │
│                                              │
│  [ OK ]                                      │
│                                              │
└─────────────────────────────────────────────┘
```

---

### Step 7: Copy Public Key

After saving, you'll see your configuration with a **public key**:

```
┌─────────────────────────────────────────────┐
│  🎁 Rewarded ad server-side verification     │
├─────────────────────────────────────────────┤
│                                              │
│  Status: ✓ Active                            │
│                                              │
│  Callback URL:                               │
│  https://your-domain.com/api/admob/verify... │
│  [ Edit ] [ Remove ]                         │
│                                              │
│  Public key (for signature verification):    │
│  ┌─────────────────────────────────────┐    │
│  │ -----BEGIN PUBLIC KEY-----          │    │
│  │ MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQ...  │    │
│  │ ...                                 │    │
│  │ -----END PUBLIC KEY-----            │    │
│  └─────────────────────────────────────┘    │
│  [ Copy to clipboard ] 📋                    │
│                                              │
└─────────────────────────────────────────────┘
```

**IMPORTANT:** Click **"Copy to clipboard"** and save this key in your server code!

---

### Step 8: Configure Custom Data (Optional)

You can also set custom data at the ad unit level:

```
AdMob → Ad units → [Your Rewarded Ad] → Settings
```

---

## 🎯 Example URLs for Different Hosting

### Firebase Functions:
```
https://us-central1-your-project.cloudfunctions.net/admobCallback
```

### Google Cloud Run:
```
https://admob-verifier-abc123-uc.a.run.app/verify
```

### Your Own Domain:
```
https://api.yourdomain.com/admob/verify-reward
```

### Vercel:
```
https://your-project.vercel.app/api/admob-verify
```

### AWS Lambda + API Gateway:
```
https://abc123.execute-api.us-east-1.amazonaws.com/prod/admob-verify
```

---

## 🧪 Testing Your Setup

### Method 1: Use AdMob's Test Button
Click the **"Test URL"** button in the AdMob console.

### Method 2: Use ngrok for Local Testing
```bash
# Terminal 1: Start your server
node server.js

# Terminal 2: Create tunnel
ngrok http 3000

# Output:
# Forwarding: https://abc123.ngrok.io -> localhost:3000

# Use in AdMob:
https://abc123.ngrok.io/api/admob/verify-reward
```

### Method 3: Test Ad in Your App
1. Build and run your app
2. Watch a test ad
3. Check your server logs for the callback

---

## 📊 What You'll See in Logs

When a user watches an ad, your server will receive:

```
Incoming request to /api/admob/verify-reward
Query parameters:
{
  ad_network: '5450213213286189855',
  ad_unit: 'ca-app-pub-5567609971256551/1555083848',
  custom_data: 'cause_id:3,cause_name:Safe Home Button',
  reward_amount: '10',
  reward_item: 'coins',
  timestamp: '1234567890123',
  transaction_id: 'abc123def456',
  user_id: '',
  signature: 'MEUCIQCy...',
  key_id: '3335741209'
}
```

---

## ⚠️ Common Errors and Solutions

### Error: "URL not accessible"
```
❌ Cannot reach callback URL
```
**Solutions:**
- Ensure your server is running
- Check HTTPS is enabled
- Verify firewall rules
- Test with: `curl -I https://your-url.com/endpoint`

### Error: "Invalid SSL certificate"
```
❌ SSL certificate verification failed
```
**Solutions:**
- Use a valid SSL certificate (Let's Encrypt is free)
- Don't use self-signed certificates
- Ensure certificate chain is complete

### Error: "Response timeout"
```
❌ Callback URL did not respond within 5 seconds
```
**Solutions:**
- Optimize your server response time
- Respond immediately, process async
- Use database indexes for fast lookups

---

## 🔍 Verification Checklist

Before going live:

- [ ] Callback URL uses HTTPS
- [ ] Server responds within 5 seconds
- [ ] Public key is correctly stored
- [ ] Signature verification works
- [ ] Duplicate transaction check implemented
- [ ] Database tables created
- [ ] Error logging configured
- [ ] Test ad successfully triggered callback
- [ ] Custom data (cause info) is received correctly
- [ ] Rewards are credited to database

---

## 📞 Need Help?

If you're stuck, check:
1. **AdMob Help Center:** https://support.google.com/admob
2. **Developer Docs:** https://developers.google.com/admob/android/rewarded-ads-ssv
3. **Server logs:** Check what's being received
4. **Network logs:** Use Postman to test your endpoint

---

## 🚀 Next Steps

After SSV is configured:
1. Monitor your logs for callbacks
2. Verify signatures are valid
3. Check database for credited rewards
4. Build analytics dashboard
5. Set up monitoring/alerts
