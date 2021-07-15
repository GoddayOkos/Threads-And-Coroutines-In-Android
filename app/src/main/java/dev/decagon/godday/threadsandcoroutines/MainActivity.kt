package dev.decagon.godday.threadsandcoroutines

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL

/**
 * Main Screen
 */
class MainActivity : AppCompatActivity() {

    @DelicateCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        // Switch to AppTheme for displaying the activity
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get a reference to the MainLooper
        val mainLooper = mainLooper  // or Looper.getMainLooper()

        Log.d("TaskThread", Thread.currentThread().name)
        GlobalScope.launch(context = Dispatchers.IO) {
            Log.d("TaskThread", Thread.currentThread().name)
            // Create and open a URL connection and cast it as HttpURLConnection
            val imageUrl = URL("https://www.pngkit.com/png/detail/17-176284_eagle-owl-png-transparent-image-owl-png.png")
            val connection = imageUrl.openConnection() as HttpURLConnection
            connection.doInput = true // This connection is for input only
            connection.connect()

            // Create an inputStream to read the image from the server to your app
            val inputStream = connection.inputStream
            // Decode the inputStream into a bitmap using BitmapFactory
            val bitmap = BitmapFactory.decodeStream(inputStream)

            launch(Dispatchers.Main) {
                Log.d("TaskThread", Thread.currentThread().name)
                image.setImageBitmap(bitmap)
            }

//            runOnUiThread {
//                Log.d("TaskThread", Thread.currentThread().name)
//                image.setImageBitmap(bitmap)
//            }

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
          //  Handler(mainLooper).post { image.setImageBitmap(bitmap) }
        }

        /**
         * Android forbids making network calls or connections on MainThread in
         * other to avoid blocking it. So to download our image, we have to switch the
         * operation to another thread (Background Thread or Worker Thread)
         */
//        Thread {
//            // Create and open a URL connection and cast it as HttpURLConnection
//            val imageUrl = URL("https://www.pngkit.com/png/detail/17-176284_eagle-owl-png-transparent-image-owl-png.png")
//            val connection = imageUrl.openConnection() as HttpURLConnection
//            connection.doInput = true // This connection is for input only
//            connection.connect()
//
//            // Create an inputStream to read the image from the server to your app
//            val inputStream = connection.inputStream
//            // Decode the inputStream into a bitmap using BitmapFactory
//            val bitmap = BitmapFactory.decodeStream(inputStream)
//
//            /**
//             * Only the MainThread (UI Thread) can update the UI in Android
//             * so we switch to the UI thread to display our image i.e
//             * we post the data to the UI thread. There are several methods used
//             * in doing this.
//             */
//
//            // 1. This method is only available inside an activity
//        //    runOnUiThread { image.setImageBitmap(bitmap) }
//
//            // 2. Using Handler with MainLooper
//            /**
//             * Loopers are pieces of the Android ecosystem which loops through all
//             * messages or signals any thread receives and process them.
//             * The MainThread Looper or simply MainLooper handles messages for the
//             * MainThread.
//             *
//             * Handlers on the other hand are used to send messages to the loopers.
//             *
//             * The combination of handlers and loopers forms a relationship or connection
//             * between signals and threads that should process them
//             */
//            Handler(mainLooper).post { image.setImageBitmap(bitmap) }
//
//        }.start()  // Start the thread

    }

    /**
     *                               COROUTINES
     *  Working threads is a bit tedious and tiring as one can easily forget to goto
     *  the background thread for some operations and return back to the main thread to use the
     *  the result obtained from the task from the background processing. This is where Kotlin
     *  Coroutines come to the rescue.
     *
     *  Coroutines allows us to do both background processing and posting to the main thread at the
     *  same time. Coroutines are functions which can run in parallel and can also be cancelled
     *  once started unlike regular functions. They are Asynchronous by default and can also be
     *  paused and resumed at any time i.e They can be suspended.
     *
     *  Coroutines required two things to work:
     *  1. CoroutineScope -> Determines how long the coroutine lives. It is the way of attaching
     *     the coroutine to a lifecycle. If coroutines are not attached to lifecycle, we would run
     *     into memory leaks, and have coroutines which never finish.
     *
     *  2. Coroutine Builder -> Determines the way the coroutine is launched. It is used to start
     *     coroutines and wraps code in a coroutine similar to using Threads & Runnables.
     *
     *  A Dispatcher is a used to switch threads in coroutines similar to Handler + Looper combination.
     *  A GlobalScope holds no Dispatchers and launch() defines a Default dispatcher.
     *  There are four different dispatchers but only two are commonly used. These are:
     *  1. Dispatchers.IO -> Used for input/output work
     *  2. Dispatchers.Main -> Used to post to the main thread.
     *  3. Dispatchers.Default
     *  4. Dispatchers.Unconfined
     *
     *  The Dispatchers.IO has Threads up to the number of processor cores or 64 (whichever is larger).
     *  The Dispatchers.IO also shared the same threads with Dispatchers.Default
     */
}
