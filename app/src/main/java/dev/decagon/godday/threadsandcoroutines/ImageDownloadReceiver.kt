package dev.decagon.godday.threadsandcoroutines

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

const val ACTION_IMAGE_DOWNLOAD = "image_downloaded"

class ImageDownloadReceiver(
    private val onImageDownloaded: (String) -> Unit
) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action != ACTION_IMAGE_DOWNLOAD) {
            return
        }
        val imagePath = intent.getStringExtra("image_path")

        if (imagePath != null) {
            onImageDownloaded(imagePath)
        }
    }
}