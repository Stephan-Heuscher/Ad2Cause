# AdMob SSV Server Example

This is a Node.js/Express server example for handling AdMob Server-Side Verification callbacks with cause tracking.

## Quick Start

### 1. Install Dependencies
```bash
npm install
```

### 2. Configure Public Key
Edit `admob-ssv-server.js` and replace `YOUR_ADMOB_PUBLIC_KEY_HERE` with your actual public key from AdMob console.

### 3. Run Locally
```bash
npm start
```

Server will run on http://localhost:3000

### 4. Test with ngrok
```bash
# In another terminal
ngrok http 3000
```

Use the ngrok URL in AdMob console:
```
https://abc123.ngrok.io/api/admob/verify-reward
```

## API Endpoint

### POST /api/admob/verify-reward

Receives AdMob SSV callbacks when users complete rewarded ads.

**Example Request:**
```
GET /api/admob/verify-reward?
  ad_network=5450213213286189855
  &ad_unit=ca-app-pub-xxx/yyy
  &custom_data=cause_id:3,cause_name:Safe%20Home%20Button
  &reward_amount=10
  &reward_item=coins
  &timestamp=1234567890123
  &transaction_id=abc123def456
  &signature=MEUCIQCy...
  &key_id=3335741209
```

**Response:**
- `200 OK` - Reward verified and credited
- `400 Bad Request` - Invalid signature or data
- `500 Internal Server Error` - Server error

## Database Setup

This example includes placeholder functions. Implement your database logic:

### Required Tables:
- `admob_transactions` - Store all transactions
- `users` - User balances
- `causes` - Cause earnings

See `../docs/admob-ssv-setup.md` for SQL schemas.

## Deployment

### Firebase Functions:
```bash
firebase init functions
# Copy admob-ssv-server.js to functions/index.js
firebase deploy --only functions
```

### Vercel:
```bash
vercel deploy
```

### Docker:
```bash
docker build -t admob-ssv .
docker run -p 3000:3000 admob-ssv
```

## Testing

### Test with curl:
```bash
curl -I http://localhost:3000/api/admob/verify-reward
```

### Health check:
```bash
curl http://localhost:3000/health
```

## Security Notes

⚠️ **Important:**
- Always verify signatures in production
- Implement duplicate transaction checking
- Use environment variables for sensitive data
- Enable rate limiting
- Log all requests for auditing

## Environment Variables

Create a `.env` file:
```env
PORT=3000
ADMOB_PUBLIC_KEY=-----BEGIN PUBLIC KEY-----...
DATABASE_URL=postgresql://...
NODE_ENV=production
```

## License

MIT
