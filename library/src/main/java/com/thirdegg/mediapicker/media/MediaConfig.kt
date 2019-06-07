package com.thirdegg.mediapicker.media

import android.os.Environment

import java.io.Serializable

class MediaConfig: Serializable {

    var imageExtension: MediaPicker.ImageExtension = MediaPicker.ImageExtension.PNG
    var videoExtension: MediaPicker.VideoExtension = MediaPicker.VideoExtension.MP4
    var mode: MediaPicker.Mode = MediaPicker.Mode.CAMERA
    var directory: String = Environment.getExternalStorageDirectory().toString() + MediaTags.Tags.IMAGE_PICKER_DIR
    var allowMultiple: Boolean = false
    var allowOnlineImages: Boolean = false
    @Transient
    var callbackImagesList:((ArrayList<String>)->Unit)? = null

}
