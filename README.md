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
- v0.10.2 - merge FTC SDK (20150803_001) changes into my app: add init() to opModes, change telemetry.


---

FTC Android Studio project to create FTC Robot Controller app.

This is the FTC SDK that can be used to create an FTC Robot Controller app, with custom op modes.
The FTC Robot Controller app is designed to work in conjunction with the FTC Driver Station app.
The FTC Driver Station app is available through Google Play.

To use this SDK, download/clone the entire project to your local computer.
Use Android Studio to import the folder  ("Import project (Eclipse ADT, Gradle, etc.)").

Documentation for the FTC SDK are included with this repository.  There is a subfolder called "doc" which contains several subfolders:

 * The folder "apk" contains the .apk files for the FTC Driver Station and FTC Robot Controller apps.
 * The folder "javadoc" contains the JavaDoc user documentation for the FTC SDK.
 * The folder "tutorial" contains PDF files that help teach the basics of using the FTC SDK.

For technical questions regarding the SDK, please visit the FTC Technology forum:

  http://ftcforum.usfirst.org/forumdisplay.php?156-FTC-Technology

In this latest version of the FTC SDK (20150803_001) the following changes should be noted:

 * New user interfaces for FTC Driver Station and FTC Robot Controller apps.
 * An init() method is added to the OpMode class.
   - For this release, init() is triggered right before the start() method.
   - Eventually, the init() method will be triggered when the user presses an "INIT" button on driver station.
   - The init() and loop() methods are now required (i.e., need to be overridden in the user's op mode).
   - The start() and stop() methods are optional.
 * A new LinearOpMode class is introduced.
   - Teams can use the LinearOpMode mode to create a linear (not event driven) program model.
   - Teams can use blocking statements like Thread.sleep() within a linear op mode.
 * The API for the Legacy Module and Core Device Interface Module have been updated.
   - Support for encoders with the Legacy Module is now working.
 * The hardware loop has been updated for better performance.


T. Eng
August 3, 2015

