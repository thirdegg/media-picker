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

}
