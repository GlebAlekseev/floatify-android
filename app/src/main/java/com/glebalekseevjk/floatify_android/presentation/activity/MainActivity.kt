package com.glebalekseevjk.floatify_android.presentation.activity

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.glebalekseevjk.floatify_android.presentation.compose.page.theme.MyApplicationTheme
import com.glebalekseevjk.floatify_android.presentation.service.ViewHolderService
import com.glebalekseevjk.floatify_android.utils.PermissionUtil
import tw.firemaples.onscreenocr.screenshot.ScreenExtractor


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Button(onClick = ::startOverlay) {
                            Text(text = "Старт")
                        }
                    }
                }
            }
        }
    }

    private fun startOverlay() {
        val granted = PermissionUtil.canDrawOverlays(this@MainActivity)
        if (granted) {
//            if (ScreenExtractor.isGranted) {
//                startService()
//            } else {
                requestMediaProjection()
//            }
        } else {
            requestPermissionDrawOverlays()
        }
    }

    private fun requestPermissionDrawOverlays() {
        var intent =
            Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${this@MainActivity.packageName}")
            )
        try {
            resultLauncherForRequestPermissionDrawOverlays.launch(intent)
        } catch (e: ActivityNotFoundException) {
            intent = Intent(Settings.ACTION_APPLICATION_SETTINGS)
            resultLauncherForRequestPermissionDrawOverlays.launch(intent)
        }
    }

    private val resultLauncherForRequestPermissionDrawOverlays =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (PermissionUtil.canDrawOverlays(this@MainActivity)) {
                requestMediaProjection()
            } else {
                Toast.makeText(
                    this@MainActivity,
                    "Права для ACTION_MANAGE_OVERLAY_PERMISSION не получены",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private fun requestMediaProjection() {
        val manager =
            this@MainActivity.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        resultLauncherForRequestMediaProjection.launch(manager.createScreenCaptureIntent())
    }

    private val resultLauncherForRequestMediaProjection =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val intent = it.data
            if (it.resultCode == Activity.RESULT_OK && intent != null) {
                ScreenExtractor.onMediaProjectionGranted(intent)
                startService()
            }
        }

    private fun startService() {
        ViewHolderService.showViews(this@MainActivity)
        this@MainActivity.finishAffinity()
    }

    companion object {
        fun getLaunchIntent(context: Context): Intent =
            Intent(context, MainActivity::class.java).apply {
                flags += Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
    }
}

