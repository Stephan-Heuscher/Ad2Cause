# AdMob Server-Side Verification Setup Guide

## Overview
Server-Side Verification (SSV) allows you to verify ad rewards on your backend before crediting users, preventing fraud and enabling detailed analytics.

---

## Part 1: AdMob Console Configuration

### Step 1: Add Your Callback URL

1. **Go to AdMob Console:**
   - Navigate to https://apps.admob.com/
   - Select your app → App settings → Rewarded ad server-side verification

2. **Enter your callback URL:**
   ```
   https://your-domain.com/api/admob/verify-reward
   ```

   **Important URL Requirements:**
   - Must use HTTPS (not HTTP)
   - Must be publicly accessible (not localhost)
   - Must respond within 5 seconds
   - Must return HTTP 200 for success

3. **Copy the Public Key:**
   - AdMob will display a **public key**
   - Copy this entire key (including BEGIN/END lines)
   - You'll need this to verify signatures on your server

   Example format:
   ```
   -----BEGIN PUBLIC KEY-----
   MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAE...
   -----END PUBLIC KEY-----
   ```

4. **Save the settings**

---

## Part 2: Understanding the Callback Format

When a user completes a rewarded ad, AdMob will send a GET request to your callback URL:

### Example Callback URL:
```
https://your-domain.com/api/admob/verify-reward?
  ad_network=5450213213286189855
  &ad_unit=ca-app-pub-5567609971256551/1555083848
  &custom_data=cause_id:3,cause_name:Safe%20Home%20Button
  &reward_amount=10
  &reward_item=coins
  &timestamp=1234567890123
  &transaction_id=abc123def456
  &user_id=user123
  &signature=MEUCIQCy...
  &key_id=3335741209
```

### Parameter Descriptions:

| Parameter | Description | Example |
|-----------|-------------|---------|
| `ad_network` | AdMob's network ID (always the same) | `5450213213286189855` |
| `ad_unit` | Your ad unit ID | `ca-app-pub-xxx/yyy` |
| `custom_data` | **YOUR CAUSE DATA** | `cause_id:3,cause_name:Safe Home Button` |
| `reward_amount` | Reward value (from AdMob config) | `10` |
| `reward_item` | Reward type (from AdMob config) | `coins` |
| `timestamp` | Unix timestamp in milliseconds | `1234567890123` |
| `transaction_id` | Unique transaction ID | `abc123def456` |
| `user_id` | User ID (if you set it in app) | `user123` |
| `signature` | Cryptographic signature | `MEUCIQCy...` |
| `key_id` | Key ID for signature verification | `3335741209` |

---

## Part 3: Hosting Options

### Option A: Cloud Functions (Serverless)

#### **Firebase Cloud Functions** (Free tier available)
```bash
# Install Firebase CLI
npm install -g firebase-tools

# Initialize Firebase
firebase init functions

# Deploy
firebase deploy --only functions
```

#### **Google Cloud Run** (Pay per request)
- Deploy Docker container
- Auto-scaling
- HTTPS included

#### **AWS Lambda + API Gateway**
- Free tier: 1M requests/month
- Use API Gateway for HTTPS endpoint

#### **Vercel/Netlify Functions**
- Free tier available
- Easy deployment
- HTTPS included

### Option B: Traditional Server

#### **Your Own Server:**
- VPS (DigitalOcean, Linode, AWS EC2)
- Must have HTTPS (use Let's Encrypt for free SSL)
- Must be always available

#### **Shared Hosting:**
- Many support Node.js or Python
- Check if they support long-running processes

---

## Part 4: Testing Your Setup

### Test with ngrok (Local Development)

1. **Install ngrok:**
   ```bash
   # Download from https://ngrok.com/
   npm install -g ngrok
   ```

2. **Start your local server:**
   ```bash
   node server.js
   # Server running on port 3000
   ```

3. **Create public tunnel:**
   ```bash
   ngrok http 3000
   ```

   This gives you a URL like: `https://abc123.ngrok.io`

4. **Configure in AdMob:**
   ```
   https://abc123.ngrok.io/api/admob/verify-reward
   ```

5. **Test an ad in your app:**
   - Watch a test ad
   - Check your server logs for the callback
   - Verify the signature works

### Test URL Format
AdMob provides a test button in the console to verify your endpoint is accessible.

---

## Part 5: Security Best Practices

### 1. **Always Verify Signatures**
```javascript
// CRITICAL: Verify the signature to ensure request is from Google
if (!verifySignature(requestUrl, signature)) {
    return res.status(400).send('Invalid signature');
}
```

### 2. **Check for Duplicates**
```javascript
// Prevent replay attacks
const isDuplicate = await checkDuplicateTransaction(transaction_id);
if (isDuplicate) {
    return res.status(200).send('Already processed');
}
```

### 3. **Validate Reward Amounts**
```javascript
// Prevent fraud by checking expected reward range
if (reward_amount > 100 || reward_amount < 1) {
    return res.status(400).send('Invalid reward amount');
}
```

### 4. **Use HTTPS Only**
- Never use HTTP for production
- AdMob requires HTTPS

### 5. **Rate Limiting**
```javascript
// Prevent abuse
const rateLimit = require('express-rate-limit');
const limiter = rateLimit({
    windowMs: 15 * 60 * 1000, // 15 minutes
    max: 100 // limit each IP to 100 requests per windowMs
});
app.use('/api/admob/', limiter);
```

---

## Part 6: Database Schema

### Recommended Tables:

#### **admob_transactions**
```sql
CREATE TABLE admob_transactions (
    id SERIAL PRIMARY KEY,
    transaction_id VARCHAR(255) UNIQUE NOT NULL,
    user_id VARCHAR(255),
    cause_id VARCHAR(50),
    cause_name VARCHAR(255),
    reward_amount DECIMAL(10,2),
    ad_unit VARCHAR(255),
    timestamp BIGINT,
    processed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_transaction_id (transaction_id),
    INDEX idx_user_id (user_id),
    INDEX idx_cause_id (cause_id)
);
```

#### **causes**
```sql
CREATE TABLE causes (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    total_earned DECIMAL(10,2) DEFAULT 0,
    total_transactions INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_total_earned (total_earned)
);
```

#### **users**
```sql
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    user_id VARCHAR(255) UNIQUE NOT NULL,
    balance DECIMAL(10,2) DEFAULT 0,
    total_earned DECIMAL(10,2) DEFAULT 0,
    ads_watched INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id)
);
```

---

## Part 7: Monitoring & Analytics

### What to Track:

1. **Revenue per Cause:**
   ```sql
   SELECT
       cause_name,
       SUM(reward_amount) as total_earned,
       COUNT(*) as total_ads,
       AVG(reward_amount) as avg_reward
   FROM admob_transactions
   GROUP BY cause_name
   ORDER BY total_earned DESC;
   ```

2. **Top Users:**
   ```sql
   SELECT
       user_id,
       COUNT(*) as ads_watched,
       SUM(reward_amount) as total_earned
   FROM admob_transactions
   GROUP BY user_id
   ORDER BY total_earned DESC
   LIMIT 10;
   ```

3. **Daily Stats:**
   ```sql
   SELECT
       DATE(FROM_UNIXTIME(timestamp/1000)) as date,
       COUNT(*) as total_ads,
       SUM(reward_amount) as total_revenue
   FROM admob_transactions
   GROUP BY date
   ORDER BY date DESC;
   ```

---

## Part 8: Response Codes

Your server should respond appropriately:

| Status Code | Meaning | When to Use |
|-------------|---------|-------------|
| **200 OK** | Success | Reward verified and credited |
| **400 Bad Request** | Invalid request | Bad signature, invalid amount |
| **401 Unauthorized** | Authentication failed | Invalid signature |
| **409 Conflict** | Duplicate transaction | Transaction already processed |
| **500 Internal Server Error** | Server error | Database error, etc. |

**Important:** AdMob will retry failed requests (non-200 responses), so handle duplicates!

---

## Part 9: Common Issues & Solutions

### Issue 1: "Callback URL not accessible"
**Solution:**
- Ensure HTTPS is enabled
- Check firewall rules
- Test with curl: `curl -I https://your-domain.com/api/admob/verify-reward`

### Issue 2: "Invalid signature"
**Solution:**
- Verify you copied the entire public key (including BEGIN/END lines)
- Make sure you're verifying the URL *without* the signature parameter
- Check the signature decoding (base64url format)

### Issue 3: "Duplicate transactions"
**Solution:**
- Always check `transaction_id` before processing
- Store processed transaction IDs in database
- Return 200 for duplicates (not an error)

### Issue 4: "Timeout errors"
**Solution:**
- Respond within 5 seconds
- Do heavy processing async after responding
- Use database indexing for fast lookups

---

## Part 10: Example: Quick Start with Firebase

### 1. Setup Firebase Project:
```bash
npm install -g firebase-tools
firebase login
firebase init functions
cd functions
npm install
```

### 2. Create function (functions/index.js):
```javascript
const functions = require('firebase-functions');
const admin = require('firebase-admin');
const crypto = require('crypto');

admin.initializeApp();
const db = admin.firestore();

// Your AdMob public key
const ADMOB_PUBLIC_KEY = `
-----BEGIN PUBLIC KEY-----
YOUR_KEY_HERE
-----END PUBLIC KEY-----
`;

exports.admobCallback = functions.https.onRequest(async (req, res) => {
    try {
        const { custom_data, reward_amount, transaction_id } = req.query;

        // Verify signature
        if (!verifySignature(req.originalUrl, req.query.signature)) {
            return res.status(401).send('Unauthorized');
        }

        // Parse cause data
        const causeInfo = parseCustomData(custom_data);

        // Check for duplicate
        const transactionRef = db.collection('transactions').doc(transaction_id);
        const doc = await transactionRef.get();

        if (doc.exists) {
            return res.status(200).send('Already processed');
        }

        // Save transaction
        await transactionRef.set({
            causeId: causeInfo.causeId,
            causeName: causeInfo.causeName,
            amount: parseFloat(reward_amount),
            timestamp: admin.firestore.FieldValue.serverTimestamp()
        });

        // Update cause total
        const causeRef = db.collection('causes').doc(causeInfo.causeId);
        await causeRef.update({
            totalEarned: admin.firestore.FieldValue.increment(parseFloat(reward_amount))
        });

        res.status(200).send('OK');

    } catch (error) {
        console.error(error);
        res.status(500).send('Error');
    }
});

function verifySignature(url, signature) {
    // Implementation here
}

function parseCustomData(customData) {
    // Implementation here
}
```

### 3. Deploy:
```bash
firebase deploy --only functions
```

### 4. Get your URL:
```
https://us-central1-YOUR_PROJECT.cloudfunctions.net/admobCallback
```

### 5. Add to AdMob console

---

## Need Help?

- **AdMob Documentation:** https://developers.google.com/admob/android/rewarded-ads-ssv
- **Test your endpoint:** Use Postman or curl to simulate callbacks
- **Check logs:** Monitor your server logs for incoming requests

---

## Summary Checklist

- [ ] Deploy your server endpoint
- [ ] Ensure HTTPS is enabled
- [ ] Copy AdMob public key
- [ ] Configure callback URL in AdMob console
- [ ] Implement signature verification
- [ ] Add duplicate transaction checking
- [ ] Create database tables
- [ ] Test with ngrok or test ad
- [ ] Monitor logs for successful callbacks
- [ ] Add error handling and logging
