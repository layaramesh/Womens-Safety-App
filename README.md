Women's Safety & Mental Health - Java Swing Demo

This is a small Java Swing demo application with a home page that displays motivational quotes which rotate every 10 minutes.

Build & run (Windows):

1. Compile:

```bash
javac -d out src/Main.java
```

2. Run:

```bash
java -cp out Main
```

Requirements:
- Java 8 or later.

Notes:
- The `SOS` button is a demo placeholder and will show a confirmation dialog.
- The `Resources` button opens a WHO mental health page in the default browser.
- The quote rotation interval is set to 10 minutes (600000 ms) in `src/Main.java`.
