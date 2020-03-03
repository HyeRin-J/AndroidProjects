package com.example.androidmemotest

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        saveBtn.setOnClickListener {
            val title = titleEdit.text.toString()
            val content = contentEdit.text.toString()
            val helper = DBHelper(this)
            val db = helper.writableDatabase
            db.execSQL("insert into tb_memo (title, content) values (?, ?)", arrayOf<String>(title, content))
            db.close()
            val intent = Intent(this, ReadMemoActivity::class.java)
            startActivity(intent)
        }
    }
}
