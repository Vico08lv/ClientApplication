package com.example.clientapplication.firebase


import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.clientapplication.R

class FirebaseService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d("FIREBASE", message.data.toString())

        // Récupérer les données du message
        val data = message.data

        val body = data["message"]


        val handler = Handler(Looper.getMainLooper())

        handler.post{
            Toast.makeText(this, "Nouvelle commande en attente!", Toast.LENGTH_LONG).show()
        }        // Créer un ID de notification unique


        val notificationId = 1 // Vous pouvez utiliser un ID unique pour chaque notification

        // Créer le canal de notification pour les versions Android Oreo et supérieures
        val channelId = "your_channel_id"
        val channelName = "Your Channel Name"
        createNotificationChannel(channelId, channelName)

        // Construire la notification
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.logo_api_commercepng) // Icône de notification
            .setContentTitle("Nouvelle commande en attente!") // Titre de la notification
            .setContentText(body) // Texte de la notification
            .setPriority(NotificationCompat.PRIORITY_DEFAULT) // Priorité de la notification
            .setAutoCancel(true)
        // Afficher la notification à l'aide du NotificationManager
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    // Créer le canal de notification pour les versions Android Oreo et supérieures
    private fun createNotificationChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}