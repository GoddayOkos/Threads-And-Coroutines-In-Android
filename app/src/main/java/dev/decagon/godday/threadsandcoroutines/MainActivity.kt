package dev.decagon.godday.threadsandcoroutines

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
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

        val imagePath = "https://www.pngkit.com/png/detail/17-176284_eagle-owl-png-transparent-image-owl-png.png"
        val task = DownloadImageTask {
            findViewById<ImageView>(R.id.image).setImageBitmap(it)
        }

        task.execute(imagePath)
    }
}
