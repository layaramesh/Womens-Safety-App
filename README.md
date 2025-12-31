# 🛡️ Women's Safety & Mental Health App

> **Empowering safety through technology** — A Java-based application designed to help women feel safer with real-time location sharing, emergency SOS features, and mental health resources.

---

## ✨ Features

### 📍 **Live Location Sharing**
Share your real-time location with trusted contacts for a specified duration. Perfect for:
- Walking home alone at night
- First dates or meeting new people
- Solo travel adventures
- Any situation where you want someone to know where you are

**How it works:**
- Enter your name and trusted contacts (phone numbers)
- Set a timeout duration (supports fractional minutes like 0.5 for quick tests!)
- Start sharing — contacts receive an Phone Call
- Location updates are logged every 10 seconds
- Auto-stops after your specified timeout

### 🚨 **Emergency SOS**
One-tap emergency alert system that can:
- Trigger instant notifications to your emergency contacts
- Send your current location (in production version)
- Provide quick access to help when you need it most

### 💚 **Mental Health Resources**
Quick access to:
- WHO Mental Health resources
- Crisis support information
- Wellness tips and guidance

### 💪 **Motivational Support**
Rotating inspirational quotes to keep you motivated and positive throughout your day.

---

## 🚀 Getting Started

### Prerequisites
- **Java JDK 8 or later** (tested with JDK 25)
- Windows, macOS, or Linux

### Installation & Running

#### Option 1: Quick Start (Windows with JDK 25)
```powershell
# Navigate to project directory
cd Womens-Safety-App

# Compile and run in one command
& "C:\Program Files\Java\jdk-25\bin\javac.exe" -d . src\Main.java; & "C:\Program Files\Java\jdk-25\bin\java.exe" Main
```

#### Option 2: Standard Java Compilation
```bash
# Compile
javac src/Main.java -d .

# Run
java Main
```

---

## 🎯 How to Use

1. **Launch the app** — The main window opens with motivational quotes
2. **Navigate tabs:**
   - **Location** — Set up and manage location sharing
   - **SOS** — Emergency alert button
   - **Resources** — Mental health and safety resources
3. **Share your location:**
   - Enter your name
   - Add contacts in format: `+11234567890` (comma-separated for multiple)
   - Set timeout duration in minutes (e.g., `10` or `0.5`)
   - Click "Start Sharing"
4. **Stop anytime** — Use the "Stop Sharing" button or wait for auto-timeout

---

## ⚠️ Important Notes

### Demo Version
This is a **demonstration application**. In this version:
- Phone calls are logged to `calls_made.log` instead of actually being sent
- Location updates are logged to `shared_locations.log`
- No actual data is transmitted

---

## 🛠️ Technical Details

- **Language:** Java
- **GUI Framework:** Swing
- **Supported Platforms:** Windows, macOS, Linux
- **Location Update Interval:** 10 seconds (configurable)
- **Quote Rotation:** 5 seconds (demo) — change to 10 minutes in production

---

## 🌟 Future Enhancements

- [ ] Real SMS or WhatsApp integration
- [ ] Actual GPS location tracking
- [ ] Emergency contact management
- [ ] Geofencing alerts
- [ ] Integration with local emergency services
- [ ] Mobile app version (iOS/Android)
- [ ] Dark mode support
- [ ] Multi-language support

---

## 💡 Contributing

This is a safety-focused project. If you have ideas to make it better, feel free to:
1. Fork the repository
2. Create a feature branch
3. Submit a pull request

All contributions that enhance safety and user experience are welcome!

---

## 📄 License

This project is created for educational and demonstration purposes.

---

## 🤝 Acknowledgments

Built with the mission to make the world a safer place, one line of code at a time.

**Stay safe. Stay connected. Stay empowered.** 💜
