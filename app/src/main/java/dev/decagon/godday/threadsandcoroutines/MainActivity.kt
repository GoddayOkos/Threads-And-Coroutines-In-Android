package dev.decagon.godday.threadsandcoroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

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
    }
}
