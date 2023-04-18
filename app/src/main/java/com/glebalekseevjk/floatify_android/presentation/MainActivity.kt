package com.glebalekseevjk.floatify_android.presentation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.glebalekseevjk.floatify_android.R
import com.glebalekseevjk.floatify_android.data.paddle.common.OCRPredictor
import java.io.IOException
import java.io.InputStream


class MainActivity : AppCompatActivity() {
    private lateinit var imageViewOld: ImageView
    private lateinit var imageViewNew: ImageView
    private lateinit var startButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageViewNew = findViewById(R.id.newImage)
        imageViewOld = findViewById(R.id.oldImage)
        startButton = findViewById(R.id.start)
        val image = readImageFromAssets(this, "image.png")!!
        imageViewNew.setImageBitmap(image)
        imageViewOld.setImageBitmap(image)
        val predicator = OCRPredictor(this)
        println("-------------------- predicator initialized")
        startButton.setOnClickListener {
            predicator.setInputImage(image)
            val result = predicator.runModel(
                runDetection = true,
                runClassification = true,
                runRecognition = true,
            )
            imageViewNew.setImageBitmap(result)
            println("-------------------- new image generated")
        }
    }
}

fun readImageFromAssets(context: Context, fileName: String): Bitmap? {
    return try {
        val inputStream: InputStream = context.assets.open(fileName)
        BitmapFactory.decodeStream(inputStream)
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}
