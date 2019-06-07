package com.thirdegg.mediapicker.media

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity

class MediaPicker(builder: Builder) {

    private val config: MediaConfig

    private val dataReceiver = object:BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val files = intent.extras?.getStringArrayList("files")?:return
            builder.getMediaConfig().callbackImagesList?.invoke(files)
            builder.getContext()?.unregisterReceiver(this)
        }
    }

    init {
        config = builder.getMediaConfig()
        builder.getContext()?.let {context->
            val fragment = MediaFragment()
            fragment.arguments = Bundle().apply {
                putSerializable("config", config)
            }
            context.registerReceiver(dataReceiver, IntentFilter(MediaTags.Action.SERVICE_ACTION))
            context.supportFragmentManager.beginTransaction().add(fragment,"chooser").commit()
        }
    }




    class Builder(private var context: AppCompatActivity?) {

        private val mediaConfig: MediaConfig = MediaConfig()

        fun mode(mode: Mode): Builder {
            this.mediaConfig.mode = mode
            return this
        }

        fun directory(directory: String): Builder {
            this.mediaConfig.directory = directory
            return this
        }

        fun directory(directory: Directory): Builder {
            if (directory == Directory.DEFAULT)
                this.mediaConfig.directory = Environment.getExternalStorageDirectory().toString() + MediaTags.Tags.IMAGE_PICKER_DIR
            return this
        }

        fun imageExtension(imageExtension: ImageExtension): Builder {
            this.mediaConfig.imageExtension = imageExtension
            return this
        }

        fun videoExtension(videoExtension: VideoExtension): Builder {
            this.mediaConfig.videoExtension = videoExtension
            return this
        }

        fun allowMultipleImages(allowMultiple: Boolean): Builder {
            this.mediaConfig.allowMultiple = allowMultiple
            return this
        }

        fun setCallback(callback:(ArrayList<String>)->Unit):Builder {
            this.mediaConfig.callbackImagesList = callback
            return this
        }

        fun allowOnlineImages(allowOnlineImages: Boolean): Builder {
            this.mediaConfig.allowOnlineImages = allowOnlineImages
            return this
        }


        fun build(): MediaPicker {
            return MediaPicker(this)
        }

        fun getContext(): AppCompatActivity? {
            return context
        }

        fun getMediaConfig(): MediaConfig {
            return mediaConfig
        }

    }

    enum class ImageExtension(val value: String) {
        PNG(".png"),
        JPG(".jpg")
    }

    enum class VideoExtension(val value: String) {
        MP4(".mp4")
    }

    enum class Mode(val value: Int) {
        CAMERA(0),
        GALLERY(1),
        CAMERA_AND_GALLERY(2)
    }

    enum class Directory(val value: Int) {
        DEFAULT(0)
    }

}
