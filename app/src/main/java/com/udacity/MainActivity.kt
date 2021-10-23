package com.udacity

import android.app.DownloadManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    private var selectedDownloadUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setLoadingButtonState(ButtonState.Completed)
        custom_button.setOnClickListener {
            download()
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            val action = intent.action
        }
    }

    private fun download() {
        custom_button.setLoadingButtonState(ButtonState.Clicked)

        if (selectedDownloadUrl != null) {

            custom_button.setLoadingButtonState(ButtonState.Loading)

//            val request =
//                DownloadManager.Request(Uri.parse(selectedDownloadUrl))
//                    .setTitle(getString(R.string.app_name))
//                    .setDescription(getString(R.string.app_description))
//                    .setRequiresCharging(false)
//                    .setAllowedOverMetered(true)
//                    .setAllowedOverRoaming(true)
//
//            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
//            downloadID =
//                downloadManager.enqueue(request)// enqueue puts the download request in the queue.
        } else {
            showToast(this)
            custom_button.setLoadingButtonState(ButtonState.Completed)
        }


    }

    companion object {
        private const val GLIDE_URL =
            "https://github.com/bumptech/glide/archive/master.zip"
        private const val URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val RETROFIT_URL =
            "https://github.com/square/retrofit/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.id) {
                R.id.glideRadioButton -> if (checked) {
                    selectedDownloadUrl = GLIDE_URL
                }
                R.id.loadappRadioButton -> if (checked) {
                    selectedDownloadUrl = URL
                }
                R.id.retrofitRadioButton -> if (checked) {
                    selectedDownloadUrl = RETROFIT_URL
                }
            }
        }
    }

    private fun showToast(context: Context) {
        val toast =
            Toast.makeText(context, "Please select the file to download", Toast.LENGTH_SHORT)
        toast.show()
    }
}
