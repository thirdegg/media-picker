package com.thirdegg.mediapicker.media

import android.os.Environment

import java.io.Serializable

class MediaConfig: Serializable {

    var directory: String = Environment.getExternalStorageDirectory().toString()
    var allowMultiple: Boolean = false
    var allowOnlineImages: Boolean = false
    @Transient
    var callbackImagesList:((ArrayList<String>)->Unit)? = null

}
