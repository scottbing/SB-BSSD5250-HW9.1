package edu.nmhu.cam30

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat


class ViewImageActivity : AppCompatActivity() {

    private lateinit var imagePreview:ImageView
    private lateinit var fileUri:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        imagePreview = ImageView(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            //get extra from intent and turn to URI (make sure it is lowercase)
            fileUri = intent.getStringExtra("filePath").toString()
            setImageURI(Uri.parse(intent.getStringExtra("filePath")))
        }

        val asciiButton = Button(this).apply {
            text = context.getString(R.string.ascii_button)
            setOnClickListener {
                Log.d("ASCII Button", "Pressed")
                editBitmap(intent.getStringExtra("filePath"))
            }
        }

        val saveButton = Button(this).apply {
            text = context.getString(R.string.save_button)
            val msg = "File Saved Successfully!"
            setOnClickListener {
                MediaScannerConnection.scanFile(applicationContext,
                    arrayOf(fileUri),
                    null, null  )
                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                Log.d(MainActivity.TAG, msg)
            }
        }

        val closeButton =Button(this).apply {
            text = context.getString(R.string.close_button)
            setOnClickListener {
                finish()
            }
        }

        val buttonLayout = LinearLayoutCompat(this).apply {
            orientation = LinearLayoutCompat.HORIZONTAL
            layoutParams = LinearLayoutCompat.LayoutParams(
                LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                LinearLayoutCompat.LayoutParams.MATCH_PARENT)
            addView(asciiButton)
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

    private fun writeASCII(input:String) {

        AlertDialog.Builder(this).apply {
            setTitle("ASCII Representation")
            setMessage("This is an ASCII representation of the current photograph")
            val text = TextView(applicationContext).apply {
                text = input
                setPadding(10, 10, 10, 10)
                //gravity = Gravity.CENTER
                //textSize = 20f
                typeface = Typeface.MONOSPACE
            }
            val diagLayout = LinearLayout(applicationContext).apply {
                orientation = LinearLayout.VERTICAL
                addView(text)
            }
            setView(diagLayout)
            setPositiveButton(
                "OK"
            ) { dialog, _ ->
                Toast.makeText(
                    applicationContext,
                    "OK Button pressed!", Toast.LENGTH_LONG
                ).show()
                dialog.dismiss()
            }
            setNegativeButton(
                "CANCEL"
            ) { dialog, _ ->
                Toast.makeText(
                    applicationContext,
                    "CANCEL button pressed!!", Toast.LENGTH_LONG
                )
                    .show()
                dialog.dismiss()
            }
            create()
            show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun editBitmap(filePath: String?) {
        val orig = BitmapFactory.decodeFile(filePath)
        val bmp = Bitmap.createScaledBitmap(
            orig, orig.width / 128,
            orig.height / 128, true
        )
        val w = bmp.width
        val h = bmp.height
        Log.d("Bit Map Width", w.toString())
        Log.d("Bit Map Height", h.toString())
        var outputString = ""
        for (y in 0 until h) { //for all the pixels in the bmp
            for (x in 0 until w) {
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
        writeASCII(outputString)
        Log.d("MACT", outputString)
        val msg = "ASCII capture succeeded:"
        Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        Log.d(MainActivity.TAG, msg)
    }

    companion object {
        const val TAG = "CameraXBasic"
    }
}
