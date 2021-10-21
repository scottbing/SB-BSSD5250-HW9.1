package edu.nmhu.cam30

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.LinearLayoutCompat
import java.security.cert.CertPath
import java.util.Arrays.toString
import kotlin.Unit.toString

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

        val buttonLayout = LinearLayoutCompat(this).apply {
            orientation = LinearLayoutCompat.HORIZONTAL
            layoutParams = LinearLayoutCompat.LayoutParams(
                LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                LinearLayoutCompat.LayoutParams.MATCH_PARENT)
            addView(saveButton)
            addView(closeButton)
        }

        val screenLayout = LinearLayoutCompat(this).apply {
            orientation = LinearLayoutCompat.VERTICAL
            layoutParams = LinearLayoutCompat.LayoutParams(
                LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                LinearLayoutCompat.LayoutParams.MATCH_PARENT)
            addView(buttonLayout)
            addView(imagePreview)
        }

        setContentView(screenLayout)
    }

    val saveButton = Button(this).apply {
        text = context.getString(R.string.save_button)
        setOnClickListener {
            MediaScannerConnection.scanFile(applicationContext,
                                            arrayOf(fileUri.toString()))
        }
    }

    val closeButton =Button(this).apply {
        text = context.getString(R.string.discard_button)
        setOnClickListener {
            finish()
        }
    }

    /*val tv = TextView(this).apply{
        text = outputString
        typeface = Typeface.MONOSPACE
    }*/

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun editBitmap(filePath: String?) {
        val orig = BitmapFactory.decodeFile(filePath)
        val bmp = Bitmap.createScaledBitmap(
            orig, orig.width / 128,
            orig.height / 128, true
        )
        val w = bmp.width
        val h = bmp.height
        var outputString = ""
        for (y in 0..h - 1) { //for all the pixels in the bmp
            for (x in 0..w - 1) {
                var currColor: Int = (bmp.getColor(x, y).red() * 255).toInt()
                currColor += (bmp.getColor(x, y).blue() * 255).toInt()
                currColor += (bmp.getColor(x, y).green() * 255).toInt()
                currColor /= 3 //average of r,g,b

                if (currColor < 255 / 4) {
                    outputString += "-"
                } else if (currColor < (255 / 4) * 2) {
                    outputString += "+"
                } else if (currColor < (255 / 4) * 3) {
                    outputString += "!"
                } else {
                    outputString += "@"
                }
            }
            outputString += "\n"
        }
        Log.d("MACT", outputString)
    }
}