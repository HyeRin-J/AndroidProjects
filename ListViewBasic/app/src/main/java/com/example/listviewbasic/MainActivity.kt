package com.example.listviewbasic

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val dataList by lazy{
        mutableListOf<String>()
    }
    var adapter: ArrayAdapter<String>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUp()
    }
    private fun setUp(){
        initListView()
        btnAdd.setOnClickListener {
            dataList.add(edtItem.text.toString())
            adapter?.notifyDataSetChanged()
        }
    }
    private fun initListView(){
        (0..10).map { dataList.add(it.toString()) }
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, dataList)
        lstMain.adapter = adapter
        lstMain.setOnItemClickListener { _, _, position, _ ->
            dataList.removeAt(position)
            adapter?.notifyDataSetChanged()
        }
    }
}
