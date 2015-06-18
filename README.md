# ftc_app
## 3491 FTC app
A test app to experiment on the ZTE Speed phones while we wait for the rest of the new hardware.

I've configured Git for this repository in a fashion similar to: http://blog.powma.com/git-you-some-mean-js-setup-advanced-git/

Hopefully I can mess around here, yet still retain the ability to merge new updates from ftctechnh and even submit pull requests if needed.

Forked from https://github.com/ftctechnh/ftc_app.
My copy of the fork at https://github.com/acharraggi/ftc_app

Change History:
- v0.0.1 - change Google APIs to 19 from 21, and minsdk from 16 to 19.
- v0.0.2 - rename the app to 3491 Robot Controller
- v0.1 - create a HelloOp, comment out the registering of all Op Modes that require the new hardware.
- v0.2 - create OrientOp that uses the phone's hardware sensors for device orientation.
- v0.3 - create LightOp that uses the light sensor for ambient light levels.
- v0.4 - create AccelerometerOp to return raw acceleration values, fix comments in my other OpModes.
- v0.5 - create MagneticOp that returns raw magnetic field values as telemetry.
- v0.6 - create ProximityOp that returns raw proximity sensor values as telemetry.
- v0.7 - create LinearAccelerometerOp to return raw linear acceleration values, fix comments & cut/paste errors in my other OpModes.
- v0.8 - create RotationOp to return raw rotation vector sensor values.
- v0.9 - create GravityOp to return raw gravity_type sensor values, update comments and telemetry in OrientOp.
- v0.9.1 - fix formatting problem in README changes by converting changes to a Markdown unordered list.
- v0.10 - create MockTeleOp which is a copy of K9TeleOp that uses USE_MOCK_HARDWARE_FACTORY = true in FtcRobotControllerActivity.
- v0.10.1 - replace FtcConfig.context with hardwareMap.appContext in all OpModes.


---

FTC Android Studio project to create FTC Robot Controller app.

This is the FTC SDK that can be used to create an FTC Robot Controller app, with custom op modes.
The FTC Robot Controller app is designed to work in conjunction with the FTC Driver Station app.
The FTC Driver Station app is available through Google Play.

To use this SDK, download/clone the entire project to your local computer.
Use Android Studio to open the folder as an "existing Android Studio project".

We are working on providing documentation (both javadoc reference documentation and a PDF user manual)
for this SDK soon.

For technical questions regarding the SDK, please visit the FTC Technology forum:

  http://ftcforum.usfirst.org/forumdisplay.php?156-FTC-Technology
  
T. Eng
May 28, 2015

