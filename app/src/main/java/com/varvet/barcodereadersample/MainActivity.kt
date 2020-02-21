package com.varvet.barcodereadersample

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.vision.barcode.Barcode
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.varvet.barcodereadersample.barcode.BarcodeCaptureActivity
import java.io.File
import java.io.FileOutputStream

val Firebase.storage : FirebaseStorage get() = FirebaseStorage.getInstance("gs://xpiree-a5ac6.appspot.com")
val storageRef= Firebase.storage.reference
val mountainsRef = storageRef.child("groceries.txt")

class MainActivity : AppCompatActivity() {


    private lateinit var mResultTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mResultTextView = findViewById(R.id.result_textview)

        findViewById<Button>(R.id.scan_barcode_button).setOnClickListener {
            val intent = Intent(applicationContext, BarcodeCaptureActivity::class.java)
            startActivityForResult(intent, BARCODE_READER_REQUEST_CODE)
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    val barcode = data.getParcelableExtra<Barcode>(BarcodeCaptureActivity.BarcodeObject)
                    val path = getFilesDir()
                    val groceriesDirectory = File(path, "GROCERIES")
                    groceriesDirectory.mkdirs()
                    val file = File(groceriesDirectory, "groceries.txt")
                    val fileurl = Uri.fromFile(File(groceriesDirectory, "groceries.txt"))
                    val content:String= barcode.displayValue
                    FileOutputStream(file).use {
                        it.write(content.toByteArray())
                        it.close()
                    }
                    val uploadTask = mountainsRef.putFile(fileurl)
                    val p = barcode.cornerPoints
                    mResultTextView.text = barcode.displayValue
                } else
                    mResultTextView.setText(R.string.no_barcode_captured)
            } else
                Log.e(LOG_TAG, String.format(getString(R.string.barcode_error_format),
                        CommonStatusCodes.getStatusCodeString(resultCode)))
        } else
            super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        private val LOG_TAG = MainActivity::class.java.simpleName
        private val BARCODE_READER_REQUEST_CODE = 1
    }
}
