package dev.decagon.godday.threadsandcoroutines


import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_main.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.TimeUnit

/**
 * Main Screen
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Switch to AppTheme for displaying the activity
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val alarmManager = getSystemService(ALARM_SERVICE)  as AlarmManager
        val currentTime = System.currentTimeMillis()

        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            },
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            currentTime + TimeUnit.MILLISECONDS.toSeconds(10_000),
            pendingIntent
        )

        // Get a reference to the MainLooper
        val mainLooper = mainLooper  // or Looper.getMainLooper()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        /**
         * Android forbids making network calls or connections on MainThread in
         * other to avoid blocking it. So to download our image, we have to switch the
         * operation to another thread (Background Thread or Worker Thread)
         */
        Thread {
            // Create and open a URL connection and cast it as HttpURLConnection
            val imageUrl = URL("https://www.pngkit.com/png/detail/17-176284_eagle-owl-png-transparent-image-owl-png.png")
            val connection = imageUrl.openConnection() as HttpURLConnection
            connection.doInput = true // This connection is for input only
            connection.connect()

            // Create an inputStream to read the image from the server to your app
            val inputStream = connection.inputStream
            // Decode the inputStream into a bitmap using BitmapFactory
            val bitmap = BitmapFactory.decodeStream(inputStream)

            /**
             * Only the MainThread (UI Thread) can update the UI in Android
             * so we switch to the UI thread to display our image i.e
             * we post the data to the UI thread. There are several methods used
             * in doing this.
             */

            // 1. This method is only available inside an activity
            //    runOnUiThread { image.setImageBitmap(bitmap) }

            // 2. Using Handler with MainLooper
            /**
             * Loopers are pieces of the Android ecosystem which loops through all
             * messages or signals any thread receives and process them.
             * The MainThread Looper or simply MainLooper handles messages for the
             * MainThread.
             *
             * Handlers on the other hand are used to send messages to the loopers.
             *
             * The combination of handlers and loopers forms a relationship or connection
             * between signals and threads that should process them
             */
            Handler(mainLooper).post { image.setImageBitmap(bitmap) }

        }.start()  // Start the thread
    }
}
