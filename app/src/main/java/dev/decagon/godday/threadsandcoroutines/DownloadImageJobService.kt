package dev.decagon.godday.threadsandcoroutines

import android.app.job.JobParameters
import android.app.job.JobService
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

class DownloadImageJobService : JobService() {

    override fun onStartJob(params: JobParameters?): Boolean {
        val imagePath = params?.extras?.getString("image_path")

        return if (imagePath != null) {
            downloadImage(imagePath)
            true
        } else {
            jobFinished(null, false)
            false
        }
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        TODO("Not yet implemented")
    }

    private fun downloadImage(imagePath: String) {
        Thread {
            val imageUrl = URL(imagePath)
            val connection = imageUrl.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()

            val imageFilePath = "owl_image_${System.currentTimeMillis()}.jpg"
            val inputStream = connection.inputStream
            val file = File(applicationContext.externalMediaDirs.first(), imageFilePath)

            val outputStream = FileOutputStream(file)
            outputStream.use { output ->
                val buffer = ByteArray(4 * 1024)
                var byteCount = inputStream.read(buffer)

                while (byteCount > 0) {
                    output.write(buffer, 0, byteCount)
                    byteCount = inputStream.read(buffer)
                }

                output.flush()
            }
        }.start()
    }
}