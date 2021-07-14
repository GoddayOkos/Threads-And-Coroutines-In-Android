package dev.decagon.godday.threadsandcoroutines

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import java.net.HttpURLConnection
import java.net.URL

/**
 * Main Screen
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Switch to AppTheme for displaying the activity
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Your code

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

            // Display the image
            image.setImageBitmap(bitmap)
        }

    }
}
