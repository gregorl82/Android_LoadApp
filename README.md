# Load App

An application designed to download a .zip file of a Github repo and notify the user when the download is complete.

The app consists of 2 screens:

1. A main screen where the user selects which repo to download then clicks the button
2. A details screen that displays the name of the repo that was downloaded and a download status

## Technical features
- main screen uses a custom button that is extended from the `View` class
- button displays an animated progress bar and loading indicator circle
- button makes use of custom attributes to vary the colors of background and progress indicators
- progress animations repeat while the download is in progress
- a notification appears once the download has completed
- clicking the notification takes user to details screen as a new activity
- details screen is animated on start using `MotionLayout`
