# Setting Up Environment Variables on Windows

This guide explains how to set up the required environment variables for Android/OpenGL development on Windows.

## Required Environment Variables

1. **JAVA_HOME** - Path to your JDK installation
2. **ANDROID_HOME** - Path to your Android SDK

## Method 1: Using System Properties (GUI)

### Step 1: Open Environment Variables Settings

1. Press `Win + R` to open the Run dialog
2. Type `sysdm.cpl` and press Enter
3. Go to the **Advanced** tab
4. Click **Environment Variables...**

### Step 2: Set JAVA_HOME

1. Under **System variables**, click **New...**
2. Enter:
   - **Variable name:** `JAVA_HOME`
   - **Variable value:** `C:\Program Files\Java\jdk-17` (adjust to your JDK path)
3. Click **OK**

### Step 3: Set ANDROID_HOME

1. Under **System variables**, click **New...**
2. Enter:
   - **Variable name:** `ANDROID_HOME`
   - **Variable value:** `C:\Users\YourUsername\AppData\Local\Android\Sdk` (adjust to your SDK path)
3. Click **OK**

### Step 4: Update PATH (Optional but Recommended)

1. Under **System variables**, find and select **Path**, then click **Edit...**
2. Click **New** and add:
   - `%JAVA_HOME%\bin`
   - `%ANDROID_HOME%\platform-tools`
   - `%ANDROID_HOME%\tools`
   - `%ANDROID_HOME%\tools\bin`
3. Click **OK** on all dialogs

## Method 2: Using Command Prompt (Admin)

Open Command Prompt as Administrator and run:

```cmd
:: Set JAVA_HOME (adjust path as needed)
setx JAVA_HOME "C:\Program Files\Java\jdk-17" /M

:: Set ANDROID_HOME (adjust path as needed)
setx ANDROID_HOME "C:\Users\YourUsername\AppData\Local\Android\Sdk" /M

:: Add to PATH
setx PATH "%PATH%;%JAVA_HOME%\bin;%ANDROID_HOME%\platform-tools" /M
```

**Note:** `/M` flag sets system-wide variables. You may need administrator privileges.

## Method 3: Using PowerShell (Admin)

Open PowerShell as Administrator and run:

```powershell
# Set JAVA_HOME
[System.Environment]::SetEnvironmentVariable("JAVA_HOME", "C:\Program Files\Java\jdk-17", "Machine")

# Set ANDROID_HOME
[System.Environment]::SetEnvironmentVariable("ANDROID_HOME", "C:\Users\YourUsername\AppData\Local\Android\Sdk", "Machine")

# Update PATH
$oldPath = [System.Environment]::GetEnvironmentVariable("Path", "Machine")
$newPath = "$oldPath;$env:JAVA_HOME\bin;$env:ANDROID_HOME\platform-tools"
[System.Environment]::SetEnvironmentVariable("Path", $newPath, "Machine")
```

## Verifying Installation

After setting environment variables, **restart VSCode** (or log out and back in) for changes to take effect.

### Check JAVA_HOME

Open Command Prompt and run:
```cmd
echo %JAVA_HOME%
java -version
javac -version
```

### Check ANDROID_HOME

```cmd
echo %ANDROID_HOME%
adb --version
```

## Common JDK Installation Paths on Windows

- **Oracle JDK:** `C:\Program Files\Java\jdk-17` or `C:\Program Files\Java\jdk-11`
- **OpenJDK:** `C:\Program Files\OpenJDK\jdk-17`
- **Adoptium (Eclipse Temurin):** `C:\Program Files\Eclipse Adoptium\jdk-17.0.1-ms-hotspot`
- **Amazon Corretto:** `C:\Program Files\Amazon Corretto\jdk17.0.1`

## Common Android SDK Paths on Windows

- **Default Android Studio location:** `C:\Users\YourUsername\AppData\Local\Android\Sdk`
- **Custom location:** Check Android Studio → Tools → SDK Manager → Android SDK Location

## Finding Your JDK Path

If you're not sure where Java is installed:

```cmd
where java
```

This will show paths like `C:\Program Files\Java\jdk-17\bin\java.exe`

The JDK home is the parent directory: `C:\Program Files\Java\jdk-17`

## Finding Your Android SDK Path

If you have Android Studio installed:

1. Open Android Studio
2. Go to **File** → **Settings** (or **Android Studio** → **Preferences** on Mac)
3. Navigate to **Appearance & Behavior** → **System Settings** → **Android SDK**
4. The **Android SDK Location** field shows your SDK path

## Troubleshooting

### Variables Not Recognized

1. **Restart VSCode** - Environment variables are loaded when VSCode starts
2. **Restart Command Prompt** - Close and reopen any open terminals
3. **Check for typos** - Ensure variable names are exactly `JAVA_HOME` and `ANDROID_HOME`

### "java is not recognized" Error

Make sure `%JAVA_HOME%\bin` is in your PATH variable.

### Gradle Build Fails

Ensure all required SDK components are installed via Android Studio's SDK Manager:
- Android SDK Platform (API 21 or higher)
- Android SDK Build-Tools
- Android SDK Platform-Tools

### ADB Not Found

Ensure `%ANDROID_HOME%\platform-tools` is in your PATH.

## Quick Check Script

Save this as `check_env.bat` and run it to verify your setup:

```batch
@echo off
echo Checking environment variables...
echo.
echo JAVA_HOME: %JAVA_HOME%
echo ANDROID_HOME: %ANDROID_HOME%
echo.
echo Java version:
java -version 2>&1
echo.
echo Javac version:
javac -version 2>&1
echo.
echo ADB version:
adb --version 2>&1
echo.
echo Gradle version:
gradle --version 2>&1
pause
```

## VSCode-Specific Notes

The VSCode configuration uses `${env:JAVA_HOME}` and `${env:ANDROID_HOME}` syntax to reference these environment variables. Once set correctly, VSCode will automatically use them for building and debugging your Android OpenGL project.