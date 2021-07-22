package dev.decagon.godday.threadsandcoroutines

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class DownloadWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    override fun doWork(): Result {

    }
}