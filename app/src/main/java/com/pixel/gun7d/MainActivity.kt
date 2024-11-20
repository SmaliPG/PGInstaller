package com.pixel.gun7d

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    // Filename of the OBB file to be extracted
    private val obbFilename = "main.1111.com.pixel.gun7d.obb"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set click listener for the Install button
        findViewById<Button>(R.id.InstallButton).setOnClickListener {
            unpackObb() // Extract the OBB file
            installApk() // Install the APK file
        }
    }

    // Extracts the OBB file from assets to the appropriate directory
    private fun unpackObb() {
        // Create a File object for the OBB file destination
        val obbFile = File(obbDir, obbFilename)

        // Open the OBB file from assets
        assets.open("obb/$obbFilename").use { input ->
            // Create parent directories if they don't exist
            obbFile.parentFile?.mkdirs()
            // Open an output stream to write to the OBB file
            FileOutputStream(obbFile).use { output ->
                // Copy the data from the input stream to the output stream
                input.copyTo(output)
            }
        }
    }

    // Installs the APK file from assets
    private fun installApk(apkFileName: String = "app.apk") {
        // Create a File object for the APK file destination
        val apkFile = File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), apkFileName)

        // Create the APK file if it doesn't exist
        if (!apkFile.exists()) apkFile.createNewFile()

        // Open the APK file from assets
        assets.open("apk/$apkFileName").use { input ->
            // Open an output stream to write to the APK file
            FileOutputStream(apkFile).use { output ->
                // Copy the data from the input stream to the output stream
                input.copyTo(output)
            }
        }

        // Create an intent to install the APK
        val intent = Intent(Intent.ACTION_VIEW).apply {
            // Set the data and type for the intent
            setDataAndType(
                FileProvider.getUriForFile(this@MainActivity, "${packageName}.provider", apkFile),
                "application/vnd.android.package-archive"
            )
            // Add flags to grant read URI permission and start in a new task
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        // Start the installation activity
        startActivity(intent)
    }
}