package com.dicoding.todoapp.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreference
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.dicoding.todoapp.R
import com.dicoding.todoapp.data.Task
import com.dicoding.todoapp.data.TaskRepository
import com.dicoding.todoapp.ui.detail.DetailTaskActivity
import com.dicoding.todoapp.utils.DateConverter
import com.dicoding.todoapp.utils.NOTIFICATION_CHANNEL_ID
import com.dicoding.todoapp.utils.TASK_ID

class NotificationWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    private val channelName = inputData.getString(NOTIFICATION_CHANNEL_ID)

    private fun getPendingIntent(task: Task): PendingIntent? {
        val intent = Intent(applicationContext, DetailTaskActivity::class.java).apply {
            putExtra(TASK_ID, task.id)
        }
        return TaskStackBuilder.create(applicationContext).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }

    override fun doWork(): Result {
        //TODO 14 : If notification preference on, get nearest active task from repository and show notification with pending intent
        val preference = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val flag = preference.getBoolean(applicationContext.getString(R.string.pref_key_notify), false)

        if (flag) {
            val taskRepository = TaskRepository.getInstance(applicationContext)
            val nearestTask = taskRepository.getNearestActiveTask()
            val pendingIntent = getPendingIntent(nearestTask)

            val mNotificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val mBuilder = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle(nearestTask.title)
                .setContentText(String.format(applicationContext.getString(R.string.notify_content), DateConverter.convertMillisToString(nearestTask.dueDateMillis)))
                .setAutoCancel(true)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_DEFAULT)

                mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID)
                mNotificationManager.createNotificationChannel(channel)
            }

            val notification = mBuilder.build()
            mNotificationManager.notify(1, notification)
        }

        return Result.success()
    }

}
