// AdMob Server-Side Verification (SSV) Endpoint
// This is an example Node.js/Express server for handling AdMob reward verification

const express = require('express');
const crypto = require('crypto');
const app = express();

app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// Your AdMob account's public key for signature verification
// Get this from: AdMob Console → Settings → Server-side verification
const ADMOB_PUBLIC_KEY = `
-----BEGIN PUBLIC KEY-----
YOUR_ADMOB_PUBLIC_KEY_HERE
-----END PUBLIC KEY-----
`;

/**
 * AdMob SSV Callback Endpoint
 *
 * AdMob will POST to this endpoint when a user completes a rewarded ad
 * Documentation: https://developers.google.com/admob/android/rewarded-ads-ssv
 */
app.post('/api/admob/verify-reward', async (req, res) => {
    try {
        console.log('Received AdMob SSV callback:', req.query);
        console.log('Headers:', req.headers);

        // 1. Extract parameters from query string
        const {
            ad_network,           // "5450213213286189855" for AdMob
            ad_unit,              // Your ad unit ID
            reward_amount,        // Reward amount (e.g., 10)
            reward_item,          // Reward type (e.g., "coins")
            timestamp,            // Unix timestamp in milliseconds
            transaction_id,       // Unique transaction ID
            user_id,              // User ID you set (optional)
            custom_data,          // YOUR CAUSE DATA: "cause_id:3,cause_name:Safe Home Button"
            signature,            // Cryptographic signature
            key_id                // Key ID for the signature
        } = req.query;

        // 2. Verify the signature to ensure this request is from Google
        if (!verifySignature(req.originalUrl, signature)) {
            console.error('Invalid signature - possible fraud attempt');
            return res.status(400).send('Invalid signature');
        }

        // 3. Parse custom data to extract cause information
        const causeInfo = parseCustomData(custom_data);
        console.log('Cause info:', causeInfo);

        // 4. Check for duplicate transactions
        const isDuplicate = await checkDuplicateTransaction(transaction_id);
        if (isDuplicate) {
            console.log('Duplicate transaction detected:', transaction_id);
            return res.status(200).send('Already processed');
        }

        // 5. Verify the reward is within acceptable limits
        if (!isValidRewardAmount(reward_amount)) {
            console.error('Invalid reward amount:', reward_amount);
            return res.status(400).send('Invalid reward amount');
        }

        // 6. Credit the reward to the user and cause in your database
        await creditReward({
            userId: user_id,
            causeId: causeInfo.causeId,
            causeName: causeInfo.causeName,
            amount: parseFloat(reward_amount),
            transactionId: transaction_id,
            timestamp: parseInt(timestamp),
            adUnit: ad_unit
        });

        // 7. Send success response to AdMob
        res.status(200).send('OK');

        console.log(`✅ Reward verified and credited: ${reward_amount} to ${causeInfo.causeName}`);

    } catch (error) {
        console.error('Error processing SSV callback:', error);
        res.status(500).send('Internal server error');
    }
});

/**
 * Verify the cryptographic signature from AdMob
 * This ensures the request actually came from Google
 */
function verifySignature(url, signature) {
    try {
        // Extract the URL without the signature parameter
        const urlWithoutSignature = url.split('&signature=')[0];

        // Decode the signature from base64url
        const signatureBuffer = Buffer.from(
            signature.replace(/-/g, '+').replace(/_/g, '/'),
            'base64'
        );

        // Verify using the public key
        const verifier = crypto.createVerify('SHA256');
        verifier.update(urlWithoutSignature);

        return verifier.verify(ADMOB_PUBLIC_KEY, signatureBuffer);
    } catch (error) {
        console.error('Signature verification error:', error);
        return false;
    }
}

/**
 * Parse the custom_data field to extract cause information
 * Format: "cause_id:3,cause_name:Safe Home Button"
 */
function parseCustomData(customData) {
    if (!customData) {
        return { causeId: null, causeName: null };
    }

    try {
        const parts = customData.split(',');
        const causeId = parts[0]?.split(':')[1] || null;
        const causeName = parts[1]?.split(':')[1] || null;

        return { causeId, causeName };
    } catch (error) {
        console.error('Error parsing custom_data:', error);
        return { causeId: null, causeName: null };
    }
}

/**
 * Check if this transaction has already been processed
 * Prevents duplicate rewards
 */
async function checkDuplicateTransaction(transactionId) {
    // TODO: Implement database check
    // Example with PostgreSQL:
    // const result = await db.query(
    //     'SELECT id FROM admob_transactions WHERE transaction_id = $1',
    //     [transactionId]
    // );
    // return result.rows.length > 0;

    return false; // Placeholder
}

/**
 * Validate the reward amount is within acceptable limits
 * Helps detect fraud or configuration errors
 */
function isValidRewardAmount(amount) {
    const rewardAmount = parseFloat(amount);
    // Your ad units should give 1-100 points per ad
    return rewardAmount > 0 && rewardAmount <= 100;
}

/**
 * Credit the reward to the user's account and the cause
 * This is where you update your database
 */
async function creditReward(data) {
    console.log('Crediting reward:', data);

    // TODO: Implement database update
    // Example with PostgreSQL:

    // 1. Record the transaction
    // await db.query(`
    //     INSERT INTO admob_transactions
    //     (transaction_id, user_id, cause_id, amount, timestamp, ad_unit)
    //     VALUES ($1, $2, $3, $4, $5, $6)
    // `, [
    //     data.transactionId,
    //     data.userId,
    //     data.causeId,
    //     data.amount,
    //     new Date(data.timestamp),
    //     data.adUnit
    // ]);

    // 2. Update user's balance
    // await db.query(`
    //     UPDATE users
    //     SET balance = balance + $1
    //     WHERE id = $2
    // `, [data.amount, data.userId]);

    // 3. Update cause's total earnings
    // await db.query(`
    //     UPDATE causes
    //     SET total_earned = total_earned + $1
    //     WHERE id = $2
    // `, [data.amount, data.causeId]);

    // 4. Create earning history record
    // await db.query(`
    //     INSERT INTO earning_history
    //     (user_id, cause_id, amount, source, timestamp)
    //     VALUES ($1, $2, $3, 'admob', $4)
    // `, [data.userId, data.causeId, data.amount, new Date(data.timestamp)]);
}

// Health check endpoint
app.get('/health', (req, res) => {
    res.status(200).json({ status: 'OK', service: 'AdMob SSV' });
});

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
    console.log(`AdMob SSV server running on port ${PORT}`);
    console.log(`Callback URL: http://your-domain.com/api/admob/verify-reward`);
});

module.exports = app;
