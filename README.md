# Media Picker

Media Picker is an Android Library that lets you to select multiple images, video for Android 4.4 (API 19) +.

# NOTE

## Installation

**Gradle**

```gradle
dependencies {
    implementation 'com.github.thirdegg:media-picker:1.0.1-alpha'
}
```

# Usage

## Images

After adding the library, you need to:

### Create an `MediaPicker`
You will need to create a new instance of `MediaPicker`. Once the instance are configured, you can call `build()`.

```kotlin
MediaPicker.Builder(MainActivity.this)
    .mode(MediaPicker.Mode.CAMERA_AND_GALLERY)
    .directory(MediaPicker.Directory.DEFAULT)
    .imageExtension(MediaPicker.Extension.PNG)
    .videoExtension(MediaPicker.Extension.MP4)
    .allowMultipleImages(false)
    .enableDebuggingMode(true)
    .setCallback { files->
        //TODO
    }
    .build()
```

### Additional Options

* `allowMultipleImages` Extra used to select and return multiple images from gallery **CANNOT select single image from gallery if this feature was enabled**

```kotlin
.allowMultipleImages(true)
```

* `allowOnlineImages` an option to allow the user to select any image from online resource ex: Google Drive **(KNOWN ISSUE) if you enable this option then you cannot select multiple images**

```kotlin
.allowOnlineImages(true)
```

---

library based on [https://github.com/alhazmy13/MediaPicker](https://github.com/alhazmy13/MediaPicker)
