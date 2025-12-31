# Azure Call Automation Webhook Server

This Node.js server handles webhook callbacks from Azure Communication Services Call Automation.

## Setup

1. **Install Node.js** (if not already installed):
   - Download from https://nodejs.org/
   - Or use: `winget install OpenJS.NodeJS`

2. **Install dependencies**:
   ```powershell
   cd callback-server
   npm install
   ```

3. **Install ngrok** (for public URL):
   ```powershell
   winget install ngrok.ngrok
   # Or download from https://ngrok.com/download
   ```

4. **Start the webhook server**:
   ```powershell
   npm start
   ```

5. **Expose to public internet with ngrok**:
   ```powershell
   ngrok http 3000
   ```

6. **Copy the ngrok URL** (e.g., `https://abc123.ngrok.io`) and update:
   - `azure-config.properties` → `azure.callbackUri=https://abc123.ngrok.io/api/callbacks`

## How It Works

1. Your Java app makes a call via Azure
2. Azure sends webhook events to your ngrok URL
3. ngrok forwards to localhost:3000
4. This server receives and logs all call events

## Endpoints

- `POST /api/callbacks` - Main webhook endpoint for Azure events
- `GET /health` - Health check endpoint

## Events Handled

- `CallConnected` - Call successfully connected
- `CallDisconnected` - Call ended
- `PlayCompleted` - Audio/TTS playback finished
- `PlayFailed` - Audio playback error
- `RecognizeCompleted` - DTMF or speech recognition completed
