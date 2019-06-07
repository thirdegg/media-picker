# Media Picker

------
Media Picker is an Android Libary that lets you to select multiple images, video for Android 4.4 (API 19) +.

# NOTE
----
## Installation
------

**Gradle**

```gradle
dependencies {
	implementation 'net.alhazmy13.MediaPicker:libary:2.4.4'
}
```

# Usage
------
## Images
After adding the library, you need to:

### Create an `MediaPicker`
You will need to create a new instance of `MediaPicker`. Once the instance are configured, you can call `build()`.

```kotlin
        new MediaPicker.Builder(MainActivity.this)
                    .mode(MediaPicker.Mode.CAMERA_AND_GALLERY)
                    .directory(MediaPicker.Directory.DEFAULT)
                    .imageExtension(MediaPicker.Extension.PNG)
                    .videoExtension(MediaPicker.Extension.MP4)
                    .allowMultipleImages(false)
                    .enableDebuggingMode(true)
                    .setCallback { files->
                        //TODO
                    }
                    .build();
```

### Additional Image Options
* `mode` to select the mode, you can choose one of these `CAMERA`,`GALLERY` or `CAMERA_AND_GALLERY`

```kotlin
.mode(MediaPicker.Mode.CAMERA)
```

* `imageExtension` You can change the imageExtension of image to `PNG` or `JPG`

```kotlin
.imageExtension(MediaPicker.Extension.PNG)
```

* `directory` You can pass the storage path, or select `Directory.DEFAULT_DIR` to keep the default path.

```kotlin
.directory(MediaPicker.Directory.DEFAULT)

//OR

.directory(Environment.getExternalStorageDirectory()+"/myFolder")

```

* `allowMultipleImages` Extra used to select and return multiple images from gallery **CANNOT select single image from gallery if this feature was enabled**

```kotlin
	.allowMultipleImages(true)
```

* `allowOnlineImages` an option to allow the user to select any image from online resource ex: Google Drive **(KNOWN ISSUE) if you enable this option then you cannot select multiple images**

```kotlin
	.allowOnlineImages(true)
```

### Additional Video Options
* `mode` to select the mode, you can choose one of these `CAMERA`,`GALLERY` or `CAMERA_AND_GALLERY`

```kotlin
.mode(MediaPicker.Mode.CAMERA)
```

* `imageExtension` You can change the imageExtension of video to `MP4`

```kotlin
.imageExtension(MediaPicker.Extension.MP4)
```


library based on [https://github.com/alhazmy13/MediaPicker](https://github.com/alhazmy13/MediaPicker)