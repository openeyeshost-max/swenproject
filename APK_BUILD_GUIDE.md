# Hotel Manager Android App - APK Build Guide

## Prerequisites
- Android Studio installed
- Android SDK (API 24+)
- JDK 17 or higher
- Gradle 8.1.0+

## Build APK for Local Testing

### Method 1: Using Android Studio (Easiest)

1. **Open Project in Android Studio**
   - File → Open → Select project root
   - Wait for Gradle sync to complete

2. **Build APK**
   - Build → Build Bundles/APKs → Build APK
   - Select "app" when prompted
   - Wait for build to complete

3. **Locate APK**
   - Built APK: `app/build/outputs/apk/debug/app-debug.apk`

4. **Install on Device/Emulator**
   ```bash
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

### Method 2: Using Command Line

1. **Navigate to project root**
   ```bash
   cd path/to/swenproject
   ```

2. **Build Debug APK**
   ```bash
   ./gradlew assembleDebug
   ```
   APK location: `app/build/outputs/apk/debug/app-debug.apk`

3. **Build Release APK (Optimized)**
   ```bash
   ./gradlew assembleRelease
   ```
   APK location: `app/build/outputs/apk/release/app-release-unsigned.apk`

4. **Install APK**
   ```bash
   adb install -r app/build/outputs/apk/debug/app-debug.apk
   ```

### Method 3: Generate Universal APK

```bash
./gradlew bundleRelease
```
Then use bundletool to generate universal APK:
```bash
bundletool build-apks --bundle=app/build/outputs/bundle/release/app-release.aab \
  --output=app.apks --mode=universal
```

## Test on Emulator

1. **Launch Android Emulator**
   - Android Studio → Tools → Device Manager
   - Create/Select device (min API 24)
   - Start emulator

2. **Install APK on Emulator**
   ```bash
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

3. **Open App**
   - Look for "Hotel Manager" in app list
   - Tap to launch

## Test on Physical Device

1. **Enable Developer Mode**
   - Settings → About Phone → Tap Build Number 7 times
   - Settings → Developer Options → USB Debugging ON

2. **Connect Device via USB**
   ```bash
   adb devices
   ```
   Your device should appear in list

3. **Install APK**
   ```bash
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

## Test Credentials

**Staff IDs for Login:**
- Admin: `ADM-001` / PIN: `1234`
- Waiter: `WT-101` / PIN: `1234`
- Kitchen: `KT-201` / PIN: `1234`
- Billing: `BL-301` / PIN: `1234`
- Receptionist: `RC-401` / PIN: `1234`

## Troubleshooting

### Build Fails
```bash
./gradlew clean
./gradlew build --stacktrace
```

### APK Size Optimization
- Release builds are minified (ProGuard)
- Avg size: ~8-12MB

### Firebase Connection
- Download `google-services.json` from Firebase Console
- Place in `app/` directory
- Rebuild

## Next Steps

1. Test all features on emulator/device
2. Verify orders workflow
3. Check room management
4. Test kitchen & billing screens
5. Ready for Play Store submission
