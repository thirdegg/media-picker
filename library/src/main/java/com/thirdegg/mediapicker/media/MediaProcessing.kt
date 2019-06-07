package com.thirdegg.mediapicker.media

import android.content.Context
import android.content.Intent
import com.thirdegg.mediapicker.FileProcessing


object MediaProcessing {

    fun processMultiMedia(context: Context, data: Intent): ArrayList<String> {
        val listOfImgs = HashSet<String>()
        val singleData = data.data
        singleData?.let {uri->
            val selectedImagePath = FileProcessing.getPath(context, uri)
            listOfImgs.add(selectedImagePath)
        }

        val clipdata = data.clipData
        for (i in 0 until (clipdata?.itemCount ?: 0)) {
            val selectedImage = clipdata!!.getItemAt(i).uri
            val selectedImagePath = FileProcessing.getPath(context, selectedImage)
            listOfImgs.add(selectedImagePath)
        }
        return listOfImgs.mapTo(ArrayList()) {it}
    }

}


