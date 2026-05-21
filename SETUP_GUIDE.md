# VSCode Setup for OpenGL ES Development (Android)

This guide explains the VSCode configuration for developing OpenGL ES 2.0 applications on Android.

## Prerequisites

Before using this setup, ensure you have:

1. **Java Development Kit (JDK)** - JDK 8 or higher
   - Set `JAVA_HOME` environment variable

2. **Android SDK** 
   - Set `ANDROID_HOME` environment variable
   - Install Android SDK Platform 21+ (or update build.gradle for newer versions)
   - Install Android Build Tools 21.1.0+ (or update build.gradle)

3. **Android NDK** (optional, for native OpenGL development)

## VSCode Extensions

The project includes recommended extensions in `.vscode/extensions.json`. Install them by:

1. Opening the command palette (`Ctrl+Shift+P`)
2. Running "Extensions: Show Recommended Extensions"
3. Clicking "Install All"

### Key Extensions:

- **Extension Pack for Java** - Full Java development support
- **Debugger for Java** - Debug Java applications
- **Gradle Task Explorer** - Run Gradle tasks easily
- **GLSL Canvas** - Preview GLSL shaders
- **Shader** - GLSL syntax highlighting

## Project Structure

```
Render3D/
├── .vscode/
│   ├── settings.json      # VSCode configuration
│   ├── extensions.json    # Recommended extensions
│   ├── launch.json        # Debug configurations
│   └── tasks.json         # Build tasks
├── app/
│   ├── src/main/
│   │   ├── java/com/mycompany/render3d/
│   │   │   ├── MainActivity.java    # Main activity
│   │   │   ├── Renderer.java        # OpenGL ES renderer
│   │   │   ├── Object3D.java        # 3D object class
│   │   │   ├── mMatrix.java         # Matrix utilities
│   │   │   ├── MeshCreator.java     # Mesh generation
│   │   │   └── iAnimate.java        # Animation interface
│   │   ├── assets/shaders/          # GLSL shader files
│   │   └── AndroidManifest.xml
│   └── build.gradle
├── build.gradle
└── settings.gradle
```

## Building the Project

### Using VSCode Tasks

Press `Ctrl+Shift+P` and run:
- **Tasks: Run Build Task** → Build Debug APK
- **Tasks: Run Task** → Choose from available tasks

### Using Command Line

```bash
# Build debug APK
gradle assembleDebug

# Build release APK
gradle assembleRelease

# Clean build
gradle clean
```

## Debugging

### Debug Configuration

The `launch.json` includes configurations for:
- Debug Android App
- Run Gradle Build

### Steps to Debug:

1. Set breakpoints in your Java code
2. Press `F5` or go to Run → Start Debugging
3. Choose "Debug Android App" configuration

## OpenGL ES Development

This project uses **OpenGL ES 2.0** via the `android.opengl.GLES20` API.

### Key Classes:

- `GLES20` - Core OpenGL ES 2.0 functions
- `GLES30` - OpenGL ES 3.0 (if targetSdkVersion supports it)
- `Matrix` - Matrix manipulation utilities

### Shader Development

Shaders are stored in `app/src/main/assets/shaders/`:
- `vertex.txt` - Vertex shader
- `fragment.txt` - Fragment shader
- Additional shaders for shadows and debugging

### GLSL Shader Preview

Use the **GLSL Canvas** extension to preview shaders:
1. Open a shader file
2. Click the "Preview" button in the top-right
3. Adjust preview settings as needed

## Common Gradle Commands

```bash
# Build variants
gradle assembleDebug      # Debug build
gradle assembleRelease    # Release build

# Testing
gradle test               # Run unit tests
gradle connectedCheck     # Run instrumented tests

# Dependencies
gradle dependencies       # Show dependency tree
gradle clean              # Clean build outputs
```

## Troubleshooting

### Build Errors

1. **SDK not found**: Ensure `ANDROID_HOME` is set correctly
2. **Java version mismatch**: Check `JAVA_HOME` points to compatible JDK
3. **Gradle sync failed**: Try `File → Invalidate Caches / Restart`

### OpenGL Errors

Check Logcat for OpenGL errors:
```java
Log.e("shaders", GLES20.glGetShaderInfoLog(shaderHandle));
```

### Device Connection

For deploying to device:
```bash
adb devices          # List connected devices
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

## Updating SDK Versions

To use newer Android SDK versions, update `app/build.gradle`:

```gradle
android {
    compileSdkVersion 33  // Update to desired version
    buildToolsVersion "33.0.0"
    
    defaultConfig {
        minSdkVersion 21   // Minimum supported version
        targetSdkVersion 33 // Target version
    }
}
```

## Resources

- [Android OpenGL ES Documentation](https://developer.android.com/guide/topics/graphics/opengl)
- [OpenGL ES 2.0 Reference](https://www.khronos.org/opengles/sdk/docs/man/xhtml/)
- [Gradle User Guide](https://docs.gradle.org/current/userguide/userguide.html)