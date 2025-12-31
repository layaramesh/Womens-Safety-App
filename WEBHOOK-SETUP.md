# Women's Safety App - Webhook Setup Guide

## Quick Start (3 Steps)

### Step 1: Start the Webhook Server
```powershell
cd callback-server
npm start
```
Leave this terminal running. You should see: "Server running on http://localhost:3000"

### Step 2: Expose with ngrok (in a NEW terminal)
```powershell
ngrok http 3000
```

You'll see output like:
```
Forwarding  https://abc123xyz.ngrok.io -> http://localhost:3000
```

**Copy the https URL** (e.g., `https://abc123xyz.ngrok.io`)

### Step 3: Update Configuration
Open `src/main/resources/azure-config.properties` and update:
```properties
azure.callbackUri=https://YOUR-NGROK-URL.ngrok.io/api/callbacks
```

Replace `YOUR-NGROK-URL` with the URL from step 2.

### Step 4: Rebuild and Run
```powershell
# Rebuild the app
mvn package -DskipTests

# Run the app
java -jar target/womens-safety-app-1.0.0.jar
```

## How It Works

1. **Java App** → Makes call via Azure Communication Services
2. **Azure** → Sends call events to your ngrok URL
3. **ngrok** → Forwards to localhost:3000
4. **Webhook Server** → Receives and logs all events

## Monitoring Calls

Watch the webhook server terminal to see real-time events:
- `CallConnected` - Call answered
- `PlayCompleted` - Message delivered
- `CallDisconnected` - Call ended

## Troubleshooting

**Webhook server not starting?**
```powershell
cd callback-server
npm install
npm start
```

**ngrok not found?**
```powershell
winget install ngrok.ngrok
# Refresh PATH
$env:Path = [System.Environment]::GetEnvironmentVariable("Path","Machine") + ";" + [System.Environment]::GetEnvironmentVariable("Path","User")
ngrok http 3000
```

**App shows "Callback URI not configured"?**
- Make sure `azure.callbackUri` is set in azure-config.properties
- Must be a public HTTPS URL from ngrok
- Must end with `/api/callbacks`
