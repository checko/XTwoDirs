# XTwoDirs

**XTwoDirs** is a demonstration Android app for AOSP that shows how to access internal and external storage simultaneously. It allows users to input text, save it to internal storage, view the file, copy it to app-specific external storage, and export it to the Documents folder using the Storage Access Framework (SAF).

## Features
- Input text and append to an internal file (`/data/data/<package>/files/inputtext.txt`)
- Show the content of the internal file
- Copy the file to app-specific external storage (`/storage/emulated/0/Android/data/<package>/files/inputtext.txt`)
- Export the file to the Documents folder using SAF (Android 10+ compatible, required for Android 11+)
- No storage permissions required (Android 14 compatible)

## Build Instructions (AOSP)
1. Place this folder under `packages/apps/` in your AOSP source tree.
2. Run `source build/envsetup.sh` and `lunch` for your target device.
3. Build the app with:
   ```
   m XTwoDirs
   ```
4. The APK will be available in the output directory and can be flashed or installed on your device.

## Notes
- This project is compatible with Android 14 and uses only app-specific storage and SAF for file export.
- No binary launcher icon is included; please add your own PNG icon in `res/mipmap-*/` if needed.

---

*This project was written by cursor.* 