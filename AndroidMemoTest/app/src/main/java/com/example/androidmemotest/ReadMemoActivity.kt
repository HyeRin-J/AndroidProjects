package com.example.androidmemotest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_read_memo.*

class ReadMemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_memo)

        val helper = DBHelper(this)
        val db = helper.writableDatabase
        val cursor = db.rawQuery("select title, content from tb_memo order by _id desc limit 1", null)
        while(cursor.moveToNext()){
            titleTxt.text = cursor.getString(0)
            contentTxt.text = cursor.getString(1)
        }
        db.close()
    }
}
