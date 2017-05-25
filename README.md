Signal-Catcher catches the SIGTERM, SIGINT and SIGHUP signals and uses them to trigger a clean shutdown.
 
Imagine you're running a dedicated server, and you press ctrl-c. This shuts down the server, but not cleanly: It causes the terminal to send SIGINT to the Java runtime, which will immediately terminate all running code, even in the middle of updating on-disk data. There is a mechanism to handle this, but because not literally every mod does so correctly, this has the potential to corrupt your world.

Terminator overrides this behavior, triggering a clean shutdown as if you had typed stop into the terminal.

The signals it handles are:
- SIGTERM: "Terminate". This is what kill/killall send.
- SIGINT: "Interrupt", ctrl-c as mentioned.
- SIGHUP: "Hangup". This is sent if the controlling terminal is itself killed, e.g. if you run Minecraft directly under SSH and lose your connection.

This mod has little effect client-side, but it's safe to leave it in on both sides.

