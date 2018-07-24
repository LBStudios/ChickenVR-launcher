# [DEPRECATED] Chicken VR Launcher

[![Build Status](https://travis-ci.org/LBStudios/ChickenVR-launcher.svg?branch=master)](https://travis-ci.org/LBStudios/ChickenVR-launcher)

JavaFX launcher for different Chicken VR versions on the same operating system.

**How to Use**

This installer is what gets downloaded with Steam and ensures that the correct version of the game gets downloaded and launched.

**Download**

~~The compiled JAR is available [here](https://github.com).~~

To run the program you need Java JDK ≥ 8 installed. Everything required for the program is included by default. [Download Java JDK SE](http://www.oracle.com/technetwork/java/javase/downloads/index.html).

**How it Works**

When initialized, the installer checks if both applications are installed, and then checks if they are updated to the
latest versions. JavaFX scene has three buttons: Chicken VR, Chicken non VR, and Quit. Quit simply uses
`Platform.exit()`. The launch buttons first check if the application is installed. If not, it will install the latest
version. If it is installed, it will check if it is updated. It should already be because of the auto-update on start,
but if it isn't, it will just run it.

**Contribute**

If there is an issue with the program or you would like to request a new feature, please make a new issue in the issue tracker.

To compile the program yourself, you only need the JDK.
