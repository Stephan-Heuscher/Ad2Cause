# AdMob Server-Side Verification - Quick Start

This guide helps you set up server-side verification for AdMob rewarded ads with cause tracking.

## 📚 Documentation Files

- **[AdMob Console Steps](./admob-console-steps.md)** - Visual guide for AdMob UI
- **[Full Setup Guide](./admob-ssv-setup.md)** - Complete implementation guide
- **[Server Example](../server-example/admob-ssv-server.js)** - Node.js/Express example

---

## ⚡ Quick Start (5 Minutes)

### 1. AdMob Console Configuration

**Path:** AdMob Console → Apps → Ad2Cause → App Settings → SSV Settings

**Add callback URL:**
```
https://your-domain.com/api/admob/verify-reward
```

**Requirements:**
- Must use HTTPS
- Must be publicly accessible
- Must respond within 5 seconds
- Must return HTTP 200 for success

### 2. Copy Public Key

After saving, copy the public key shown in the AdMob console:
```
-----BEGIN PUBLIC KEY-----
MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQ...
-----END PUBLIC KEY-----
```

### 3. What AdMob Sends

When a user completes an ad, AdMob makes a GET request:

```
GET https://your-domain.com/api/admob/verify-reward?
  ad_unit=ca-app-pub-xxx/yyy
  &custom_data=cause_id:3,cause_name:Safe%20Home%20Button  ← YOUR CAUSE DATA
  &reward_amount=10
  &transaction_id=abc123
  &timestamp=1234567890123
  &signature=MEUCIQCy...
```

### 4. Parse Cause Information

Extract cause data from `custom_data` parameter:
```javascript
// Input: "cause_id:3,cause_name:Safe Home Button"
const parts = customData.split(',');
const causeId = parts[0].split(':')[1];      // "3"
const causeName = parts[1].split(':')[1];    // "Safe Home Button"
```

---

## 🎯 What You Get

### In Your Server Logs:
```javascript
{
  causeId: "3",
  causeName: "Safe Home Button",
  rewardAmount: 10,
  transactionId: "abc123def456",
  timestamp: 1234567890123
}
```

### In Your Database:
Track which causes generate the most revenue:

| Cause Name | Total Earned | Ads Watched |
|------------|--------------|-------------|
| Safe Home Button | $150.00 | 1,500 |
| Assistive Tap | $120.00 | 1,200 |
| AI Rescue Ring | $90.00 | 900 |

---

## 🚀 Hosting Options

### Easiest (Serverless):

**Firebase Cloud Functions** (Free tier)
```bash
firebase init functions
firebase deploy --only functions
```
URL: `https://us-central1-PROJECT.cloudfunctions.net/admobCallback`

**Vercel** (Free tier)
```bash
vercel deploy
```
URL: `https://your-project.vercel.app/api/admob`

### Testing Locally:

**ngrok** (Free for testing)
```bash
# Terminal 1
node server.js

# Terminal 2
ngrok http 3000
```
URL: `https://abc123.ngrok.io/api/admob/verify-reward`

---

## 🔐 Security Checklist

- [ ] **Verify signatures** - Ensure requests are from Google
- [ ] **Check duplicates** - Prevent replay attacks
- [ ] **Validate amounts** - Catch configuration errors
- [ ] **Use HTTPS** - Required by AdMob
- [ ] **Rate limiting** - Prevent abuse

---

## 📊 Example Queries

### Total earnings per cause:
```sql
SELECT
    cause_name,
    SUM(reward_amount) as total,
    COUNT(*) as ads
FROM admob_transactions
GROUP BY cause_name
ORDER BY total DESC;
```

### Daily revenue:
```sql
SELECT
    DATE(FROM_UNIXTIME(timestamp/1000)) as date,
    SUM(reward_amount) as revenue,
    COUNT(*) as ads
FROM admob_transactions
GROUP BY date
ORDER BY date DESC;
```

---

## 🐛 Troubleshooting

### Issue: "URL not accessible"
```bash
# Test your endpoint
curl -I https://your-domain.com/api/admob/verify-reward
```

### Issue: "Invalid signature"
- Verify public key is complete (including BEGIN/END lines)
- Check signature verification logic
- Ensure URL excludes signature parameter when verifying

### Issue: "Timeout"
- Respond within 5 seconds
- Do async processing after responding
- Use database indexing

---

## 📖 Full Documentation

For detailed implementation:
- **Console setup:** [admob-console-steps.md](./admob-console-steps.md)
- **Server implementation:** [admob-ssv-setup.md](./admob-ssv-setup.md)
- **Code example:** [server-example/admob-ssv-server.js](../server-example/admob-ssv-server.js)

---

## 🎓 Learning Resources

- [AdMob SSV Official Docs](https://developers.google.com/admob/android/rewarded-ads-ssv)
- [AdMob Help Center](https://support.google.com/admob)

---

## ✅ Verification Steps

1. [ ] Configure callback URL in AdMob console
2. [ ] Copy and store public key
3. [ ] Deploy your server endpoint
4. [ ] Test with ngrok or live URL
5. [ ] Watch a test ad in your app
6. [ ] Verify callback received in logs
7. [ ] Check cause data is parsed correctly
8. [ ] Confirm reward credited to database

---

**Status of Your App:**
- ✅ App sends cause data: `cause_id:X,cause_name:Y`
- ✅ AdMob configured with your ad units
- ⏳ SSV endpoint needed (follow this guide)
- ⏳ Database schema needed (see setup guide)

**Next Step:** Deploy a server endpoint and configure it in AdMob console!
