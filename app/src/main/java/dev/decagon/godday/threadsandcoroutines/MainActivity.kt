package dev.decagon.godday.threadsandcoroutines

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.URL

const val JOB_ID = 10

/**
 * Main Screen
 */
@DelicateCoroutinesApi
class MainActivity : AppCompatActivity() {

    private val receiver by lazy { ImageDownloadReceiver { displayImage(it) } }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        // Switch to AppTheme for displaying the activity
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        registerReceiver(receiver, IntentFilter().apply {
            addAction(ACTION_IMAGE_DOWNLOAD)
        })

        val jobScheduler = getSystemService(JobScheduler::class.java) ?: return

        jobScheduler.schedule(
            JobInfo.Builder(
                JOB_ID, ComponentName(this, DownloadImageJobService::class.java)
            )
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setExtras(PersistableBundle().apply {
                    putString("image_path", "https://www.wallpaperup.com/uploads/wallpapers/2013/03/21/55924/3b61c716155c6fa88f321da6d4655767.jpg")
                })
                .setOverrideDeadline(1500)
                .build()
        )
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }

    private fun displayImage(imagePath: String) {
        GlobalScope.launch(Dispatchers.Main) {
            val bitmap = loadImageFromFile(imagePath)

            image.setImageBitmap(bitmap)
        }
    }

    private suspend fun loadImageFromFile(imagePath: String): Bitmap =
        withContext(Dispatchers.IO) { BitmapFactory.decodeFile(imagePath) }
}
