package com.thirdegg.mediapicker.media

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.thirdegg.mediapicker.R
import java.io.File
import java.util.*

class MediaFragment: Fragment() {

    lateinit var config: MediaConfig
    lateinit var destination:File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        config = (arguments?.getSerializable("config") as MediaConfig?)?: throw IllegalAccessException()

        activity?.let { context ->
            context.findViewById<ViewGroup>(android.R.id.content).let { view ->

                val packageManager = context.packageManager

                val dialog = BottomSheetDialog(context)
                val bottomSheetDialog = LayoutInflater.from(context).inflate(
                        R.layout.bottom_dialog_chose_view,
                        view,
                        false
                )
                val rootLayout = bottomSheetDialog.findViewById<LinearLayout>(R.id.root_bottom_dialog)
                rootLayout.findViewById<View>(R.id.take_image_button).setOnClickListener {
                    destination = File(config.directory, getRandomString() + ".jpg")
                    startActivityForTakeImage(context)
                    dialog.dismiss()
                }

                rootLayout.findViewById<View>(R.id.take_video_button).setOnClickListener {
                    destination = File(config.directory, getRandomString() + ".mp4")
                    startActivityForTakeVideo(context)
                    dialog.dismiss()
                }

                rootLayout.findViewById<View>(R.id.chose_image_button).setOnClickListener {
                    var galleryIntents = getGalleryIntents(packageManager, Intent.ACTION_GET_CONTENT, "image/*", false)
                    if (galleryIntents.isEmpty()) {
                        // if no intents found for get-content try pick intent action (Huawei P9).
                        galleryIntents = getGalleryIntents(packageManager, Intent.ACTION_PICK, "image/*", false)
                    }
                    pickIntent(galleryIntents)
                    dialog.dismiss()
                }

                rootLayout.findViewById<View>(R.id.chose_video_button).setOnClickListener {
                    var galleryIntents = getGalleryIntents(packageManager, Intent.ACTION_GET_CONTENT, "video/*", false)
                    if (galleryIntents.isEmpty()) {
                        // if no intents found for get-content try pick intent action (Huawei P9).
                        galleryIntents = getGalleryIntents(packageManager, Intent.ACTION_PICK, "video/*", false)
                    }
                    pickIntent(galleryIntents)
                    dialog.dismiss()
                }

                dialog.setContentView(bottomSheetDialog)
                dialog.show()
            }
        }
    }

    private fun pickIntent(intents: ArrayList<Intent>) {
        val target: Intent
        if (intents.isEmpty()) {
            target = Intent()
        } else {
            target = intents[intents.size - 1]
            intents.removeAt(intents.size - 1)
        }

        // Create a chooser from the main  intent
        val chooserIntent = Intent.createChooser(target, "Chose test")

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents.toTypedArray())
        startActivityForResult(chooserIntent, MEDIA_PICKER_REQUEST_CODE)
    }

    private fun startActivityForTakeImage(context: Activity) {
        val intentPhoto = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val fileUri = FileProvider.getUriForFile(context, context.applicationContext.packageName + ".provider", destination)
        intentPhoto.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
//        val chooserIntent = Intent.createChooser(intentPhoto, "Select Picture")
//        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentPhoto)
        startActivityForResult(intentPhoto, CAMERA_REQUEST_CODE)
    }

    private fun startActivityForTakeVideo(context: Activity) {
        val intentVideo = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        val fileUri = FileProvider.getUriForFile(context, context.applicationContext.packageName + ".provider", destination)
        intentVideo.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
//        val chooserIntent = Intent.createChooser(intentVideo, "Select Picture")
//        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentVideo)
        startActivityForResult(intentVideo, CAMERA_REQUEST_CODE)
    }


    private fun getGalleryIntents(packageManager: PackageManager, action: String, mime:String, includeDocuments: Boolean): ArrayList<Intent> {

        val intents = ArrayList<Intent>()
        val galleryIntent = if (action == Intent.ACTION_GET_CONTENT) {
            Intent(action)
        } else {
            Intent(action, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        }
        galleryIntent.type = mime
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, config.allowMultiple)
        val listGallery = packageManager.queryIntentActivities(galleryIntent, 0)
        for (res in listGallery) {
            val intent = Intent(galleryIntent)
            intent.component = ComponentName(res.activityInfo.packageName, res.activityInfo.name)
            intent.setPackage(res.activityInfo.packageName)
            intents.add(intent)
        }

        // remove documents intent
        if (!includeDocuments) {
            for (intent in intents) {
                if (intent.component?.className == "com.android.documentsui.DocumentsActivity") {
                    intents.remove(intent)
                    break
                }
            }
        }
        return intents
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val intent = Intent()
        val files = ArrayList<String>()
        if (requestCode == MEDIA_PICKER_REQUEST_CODE) {
            context?.let { context->
                data?.let {data->
                    files.addAll(MediaProcessing.processMultiMedia(context,data))
                }
            }
        }
        if (requestCode == CAMERA_REQUEST_CODE) {
            context?.let { context->
                data?.let {data->
                    files.add(destination.absolutePath)
                }
            }
        }

        intent.action = SERVICE_ACTION
        intent.putExtras(Bundle().apply {
            putStringArrayList("files",files)
        })
        context?.sendBroadcast(intent)

    }

    fun getRandomString():String {
        return UUID.randomUUID().toString()
    }

    companion object {
        const val MEDIA_PICKER_REQUEST_CODE = 42141
        const val CAMERA_REQUEST_CODE = 1888
        const val SERVICE_ACTION = "com.thirdegg.mediapicker.mediapicker.media.service"
    }

}