package dev.decagon.godday.threadsandcoroutines

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

class DownloadWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    override fun doWork(): Result {
        val imageUrl = URL("https://www.pngkit.com/png/detail/17-176284_eagle-owl-png-transparent-image-owl-png.png")
        val connection = imageUrl.openConnection() as HttpURLConnection
        connection.doInput = true
        connection.connect()

        val imagePath = "owl_image.png"
        val inputStream = connection.inputStream
        val file = File(applicationContext.externalMediaDirs.first(), imagePath)

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

        return Result.success()
    }
}