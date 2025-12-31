const express = require('express');
const bodyParser = require('body-parser');

const app = express();
const PORT = process.env.PORT || 3000;

// Middleware
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

// Log all incoming requests
app.use((req, res, next) => {
    console.log(`${new Date().toISOString()} - ${req.method} ${req.path}`);
    next();
});

// Health check endpoint
app.get('/health', (req, res) => {
    res.json({ status: 'healthy', timestamp: new Date().toISOString() });
});

// Main webhook endpoint for Azure Call Automation events
app.post('/api/callbacks', (req, res) => {
    const events = req.body;
    
    console.log('\n=== Received Azure Call Event ===');
    console.log(JSON.stringify(events, null, 2));
    
    // Process different event types
    if (Array.isArray(events)) {
        events.forEach(event => {
            handleCallEvent(event);
        });
    } else {
        handleCallEvent(events);
    }
    
    // Acknowledge receipt
    res.status(200).json({ received: true });
});

// Handle individual call events
function handleCallEvent(event) {
    const eventType = event.type || event.eventType;
    
    console.log(`\nEvent Type: ${eventType}`);
    
    switch (eventType) {
        case 'Microsoft.Communication.CallConnected':
            console.log('✓ Call connected successfully');
            console.log(`Call ID: ${event.data?.callConnectionId}`);
            break;
            
        case 'Microsoft.Communication.CallDisconnected':
            console.log('✓ Call disconnected');
            break;
            
        case 'Microsoft.Communication.RecognizeCompleted':
            console.log('✓ Recognition completed');
            console.log(`Recognition result: ${event.data?.recognitionType}`);
            break;
            
        case 'Microsoft.Communication.PlayCompleted':
            console.log('✓ Audio playback completed');
            break;
            
        case 'Microsoft.Communication.PlayFailed':
            console.log('✗ Audio playback failed');
            console.error(`Error: ${event.data?.resultInformation?.message}`);
            break;
            
        default:
            console.log(`Unhandled event type: ${eventType}`);
    }
}

// Endpoint for call status updates
app.post('/api/call-status', (req, res) => {
    console.log('\n=== Call Status Update ===');
    console.log(JSON.stringify(req.body, null, 2));
    res.status(200).send('OK');
});

// Error handling middleware
app.use((err, req, res, next) => {
    console.error('Error:', err);
    res.status(500).json({ error: 'Internal server error' });
});

// Start server
app.listen(PORT, () => {
    console.log(`\n╔═══════════════════════════════════════════════════════════╗`);
    console.log(`║  Azure Call Automation Webhook Server                    ║`);
    console.log(`║  Server running on http://localhost:${PORT}                ║`);
    console.log(`║                                                           ║`);
    console.log(`║  Endpoints:                                               ║`);
    console.log(`║  • POST /api/callbacks - Main webhook for Azure events   ║`);
    console.log(`║  • GET  /health - Health check                            ║`);
    console.log(`║                                                           ║`);
    console.log(`║  To expose publicly, use ngrok:                           ║`);
    console.log(`║  $ ngrok http ${PORT}                                       ║`);
    console.log(`╚═══════════════════════════════════════════════════════════╝\n`);
});
