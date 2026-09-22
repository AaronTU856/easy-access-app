package com.example.easy_access_app.ui.printer

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.easy_access_app.R
import kotlin.concurrent.thread

class PrinterFragment : Fragment() {

    // Notification channel ID for printer notifications
    private val CHANNEL_ID = "printer_notifications"
    private val POST_NOTIFICATION_PERMISSION_REQUEST_CODE = 1

    private var printerStatusTextView: TextView? = null
    private var printerWebsiteButton: Button? = null
    private lateinit var notificationButton: Button
    private lateinit var calculateButton: Button
    private lateinit var costResultTextView: TextView
    private lateinit var printTypeSpinner: Spinner
    private lateinit var pageCountInput: EditText
    private lateinit var copyCountInput: EditText

    // Handler for updating printer status
    private val printerHandler = Handler(Looper.getMainLooper()) { msg ->
        printerStatusTextView?.text = msg.obj as String
        true
    }

    // Volatile flag for controlling the periodic printer status updates
    @Volatile
    private var isRunning = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_printer, container, false)
        printerStatusTextView = view.findViewById(R.id.printerStatusTextView)
        printerWebsiteButton = view.findViewById(R.id.printerWebsiteButton)
        printTypeSpinner = view.findViewById(R.id.printTypeSpinner)
        pageCountInput = view.findViewById(R.id.pageCountInput)
        copyCountInput = view.findViewById(R.id.copyCountInput)
        calculateButton = view.findViewById(R.id.calculateButton)
        costResultTextView = view.findViewById(R.id.costResultTextView)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Create notification channel
        createNotificationChannel()

        // Populate spinner with print types
        val rates = mapOf(
            "Grayscale" to 0.10,
            "Color" to 0.50,
            "Double-Sided Grayscale" to 0.15,
            "Double-Sided Color" to 0.60
        )

        val printTypes = rates.keys.toList()
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, printTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        printTypeSpinner.adapter = adapter

        // Set up calculate button
        calculateButton.setOnClickListener {
            val selectedType = printTypeSpinner.selectedItem.toString()
            val pages = pageCountInput.text.toString().toIntOrNull() ?: 0
            val copies = copyCountInput.text.toString().toIntOrNull() ?: 0
            val rate = rates[selectedType] ?: 0.0

            // Calculate total cost
            val totalCost = pages * copies * rate
            costResultTextView.text = String.format("Estimated Cost: €%.2f", totalCost)

            // Trigger notification
            sendNotification(
                "Print Cost Estimate",
                "Estimated cost for your print job is €%.2f".format(totalCost)
            )
        }


        // Set up website button
        printerWebsiteButton?.setOnClickListener {
            val printerUrl = "https://www.tudublincityprint.ie/"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(printerUrl))
            startActivity(intent)
        }

        // Start periodic printer status updates
        fetchPrinterStatusPeriodically()
    }

    // Periodically update printer status
    private fun fetchPrinterStatusPeriodically() {
        thread {
            while (isRunning) {
                Thread.sleep(5000)
                val status = "Printer Status: Online"
                printerHandler.obtainMessage(0, status).sendToTarget()
            }
        }
    }

    // Create a notification channel for printer notifications
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Printer Notifications"
            val descriptionText = "Notifications for printer-related actions"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 1000)
            }
            val notificationManager: NotificationManager =
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    // Send a notification
    private fun sendNotification(title: String, content: String) {
        val notificationManager =
            requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.ic_printer)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        notificationManager.notify(1, builder.build())
    }

    override fun onStop() {
        super.onStop()
        isRunning = false // Stop periodic updates when the fragment is stopped
    }
}
