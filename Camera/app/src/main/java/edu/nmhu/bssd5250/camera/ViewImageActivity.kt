package edu.nmhu.bssd5250.camera

import android.media.MediaScannerConnection
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView

class ViewImageActivity : AppCompatActivity() {

    private lateinit var imagePreview:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        imagePreview = ImageView(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            //get extra from intent and turn to URI (make sure it is lowercase)
            setImageURI(Uri.parse(intent.getStringExtra("filePath")))
        }
        setContentView(imagePreview)
    }

    val saveButton = Button(this).apply {
        text = "Save"
        setOnClickListener {
            MediaScannerConnection.scanFile(applicationContext,
                                            arrayOf(fileUri.toString()))
        }
    }

    val closeButton =Button(this).apply {
        text = "Discard"
        setOnClickListener {
            finish()
        }
    }
}