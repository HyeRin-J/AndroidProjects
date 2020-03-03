package com.example.clothes

import android.app.Activity
import android.content.ContentValues
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_clothes.*
import java.io.ByteArrayOutputStream

class AddClothesActivity : AppCompatActivity() {
    private lateinit var bitmap: Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_clothes)
        bitmap = intent.getParcelableExtra("image")
        image_view.setImageBitmap(bitmap)
        add_to_db_btn.setOnClickListener{
            updateClothesDB()
        }
    }

    private fun updateClothesDB() {
        if (tops_radio_btn.isChecked || pants_radio_btn.isChecked || outer_radio_btn.isChecked || shoes_radio_btn.isChecked || clothes_name_text.text.toString() != "") {
            val helper = ClothesDBHelper(this)
            val db = helper.writableDatabase

            val contentValues = ContentValues()
            contentValues.put("category", when {
                tops_radio_btn.isChecked -> 1
                pants_radio_btn.isChecked -> 2
                outer_radio_btn.isChecked -> 3
                shoes_radio_btn.isChecked -> 4
                else -> 0
            })
            contentValues.put("name", clothes_name_text.text.toString())

            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            contentValues.put("image", byteArrayOutputStream.toByteArray())

            db.insert("tb_clothes", null, contentValues)
            db.close()

            setResult(Activity.RESULT_OK)
            finish()
        } else {
            Toast.makeText(this, "모든 데이터가 입력되지 않았습니다.", Toast.LENGTH_LONG).show()
        }
    }
}
