package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager

    private var selectedRepo: GithubRepo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        createNotificationChannel(CHANNEL_ID, "LoadApp Notification Channel")

        custom_button.setLoadingButtonState(ButtonState.Completed)
        custom_button.setOnClickListener {
            download()
        }
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.id) {
                R.id.glideRadioButton -> if (checked) {
                    selectedRepo = GithubRepo.GLIDE
                }
                R.id.loadappRadioButton -> if (checked) {
                    selectedRepo = GithubRepo.UDACITY
                }
                R.id.retrofitRadioButton -> if (checked) {
                    selectedRepo = GithubRepo.RETROFIT
                }
            }
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            val action = intent.action

            if (downloadID == id) {
                if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {

                    val query = DownloadManager.Query().setFilterById(id)
                    val downloadManager =
                        context!!.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                    val cursor: Cursor = downloadManager.query(query)

                    if (cursor.moveToFirst() && cursor.count > 0) {
                        val downloadStatus =
                            cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))

                        val notificationStatus =
                            if (downloadStatus == DownloadManager.STATUS_SUCCESSFUL) {
                                "Success"
                            } else {
                                "Failed"
                            }

                        custom_button.setLoadingButtonState(ButtonState.Completed)

                        notificationManager.sendNotification(
                            selectedRepo!!.repoName,
                            applicationContext,
                            notificationStatus
                        )

                    }
                }
            }
        }
    }


    private fun download() {
        custom_button.setLoadingButtonState(ButtonState.Clicked)

        if (selectedRepo != null) {
            custom_button.setLoadingButtonState(ButtonState.Loading)
            val request =
                DownloadManager.Request(Uri.parse(selectedRepo!!.url))
                    .setTitle(getString(R.string.app_name))
                    .setDescription(getString(R.string.app_description))
                    .setRequiresCharging(false)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true)

            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            downloadID =
                downloadManager.enqueue(request)// enqueue puts the download request in the queue.
        } else {
            Toast.makeText(this, "Please select the file to download", Toast.LENGTH_SHORT).show()
            custom_button.setLoadingButtonState(ButtonState.Completed)
        }


    }

    private fun createNotificationChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            notificationManager = ContextCompat.getSystemService(
                applicationContext,
                NotificationManager::class.java
            ) as NotificationManager

            val notificationChannel =
                NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    enableLights(true)
                    lightColor = Color.GREEN
                    enableVibration(true)
                    description = "Download complete!"
                }

            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }


    companion object {
        private const val CHANNEL_ID = "channelId"
    }
}
