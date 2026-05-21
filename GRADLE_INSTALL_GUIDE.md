# How to Install Gradle on Windows

## Method 1: Manual Installation (Recommended)

### Step 1: Download Gradle

1. Go to [Gradle Releases](https://gradle.org/releases/)
2. Download the **bin** distribution (e.g., `gradle-8.9-bin.zip`)
   - Or use direct link: https://services.gradle.org/distributions/gradle-8.9-bin.zip

### Step 2: Extract Gradle

1. Create a directory: `C:\Gradle`
2. Extract the downloaded ZIP file to `C:\Gradle`
3. You should have: `C:\Gradle\gradle-8.9\` with `bin`, `lib`, etc. inside

### Step 3: Add Gradle to PATH

#### Option A: Using System Properties (GUI)

1. Press `Win + R`, type `sysdm.cpl`, press Enter
2. Go to **Advanced** tab
3. Click **Environment Variables...**
4. Under **System variables**, find and select **Path**, click **Edit...**
5. Click **New** and add: `C:\Gradle\gradle-8.9\bin`
6. Click **OK** on all dialogs

#### Option B: Using PowerShell (Admin)

Open PowerShell as Administrator and run:

```powershell
[Environment]::SetEnvironmentVariable("Path", $env:Path + ";C:\Gradle\gradle-8.9\bin", "Machine")
```

### Step 4: Verify Installation

1. **Close and reopen** any open Command Prompt/PowerShell windows
2. Run: `gradle --version`
3. You should see Gradle version information

## Method 2: Using Chocolatey (if installed)

If you have Chocolatey package manager:

```cmd
choco install gradle
```

## Method 3: Using Scoop (if installed)

If you have Scoop package manager:

```cmd
scoop install gradle
```

## After Installation

1. **Restart VSCode** completely (close all windows and reopen)
2. Press `Ctrl+Shift+B` to build the debug APK

## Troubleshooting

### "gradle is not recognized"

1. Make sure you added the correct path to PATH variable
2. Restart your terminal/VSCode after modifying PATH
3. Open a new Command Prompt and run: `echo %PATH%` to verify Gradle path is included

### Check if Gradle is in PATH

Open Command Prompt and run:

```cmd
where gradle
```

This should show the path to `gradle.bat` or `gradle.exe`.

### Alternative: Update VSCode Tasks

If you installed Gradle in a custom location, update `.vscode/tasks.json`:

```json
{
    "label": "Build Debug APK",
    "type": "shell",
    "command": "C:\\Gradle\\gradle-8.9\\bin\\gradle.bat",
    ...
}
```

## Quick Verification Script

Save as `check_gradle.bat` and run:

```batch
@echo off
echo Checking Gradle installation...
echo.
where gradle
echo.
gradle --version
echo.
pause
```
