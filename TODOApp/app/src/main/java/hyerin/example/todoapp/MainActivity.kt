package hyerin.example.todoapp

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import hyerin.example.todoapp.ItemV0.Companion.TYPE_DATA
import hyerin.example.todoapp.ItemV0.Companion.TYPE_HEADER

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_header.view.*
import kotlinx.android.synthetic.main.item_main.view.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    var list: MutableList<ItemV0> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        selectDB()

        fab.setOnClickListener {
            val intent = Intent(this, AddToDoActivity::class.java)
            startActivityForResult(intent, 10)
        }
    }

    private fun selectDB(){
        list = mutableListOf()
        val helper = DBHelper(this)
        val db = helper.readableDatabase
        val cursor = db.rawQuery("select * from tb_todo order by date desc", null)

        var preDate: Calendar? = null
        while(cursor.moveToNext()){
            val dbdate = cursor.getString(3)
            val date = SimpleDateFormat("yyyy-MM-dd").parse(dbdate)
            val currentDate = GregorianCalendar()
            currentDate.time = date

            if(!currentDate.equals(preDate)){
                val headerItem = HeaderItem(dbdate)
                list.add(headerItem)
                preDate = currentDate
            }
            val completed = cursor.getInt(4) != 0
            val dataItem = DataItem(cursor.getInt(0), cursor.getString(1), cursor.getString(2), completed)
            list.add(dataItem)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MyAdapter(list)
        recyclerView.addItemDecoration(MyDecoration())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 10 && resultCode == Activity.RESULT_OK){
            selectDB()
        }
    }

    class HeaderViewHolder(view: View): RecyclerView.ViewHolder(view){
        val headerView = view.itemHeaderView
    }

    class DataViewHolder(view: View): RecyclerView.ViewHolder(view){
        val completedIconView = view.completedIconView
        val itemTitleView = view.itemTitleView
        val itemContentView = view.itemContentView
    }

    inner class MyAdapter(private val list: MutableList<ItemV0>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        override fun getItemViewType(position: Int): Int {
            return list[position].type
        }

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
            if(p1 == TYPE_HEADER){
                val layoutInflater = LayoutInflater.from(p0.context)
                return HeaderViewHolder(layoutInflater.inflate(R.layout.item_header, p0, false))
            } else{
                val layoutInflater = LayoutInflater.from(p0.context)
                return DataViewHolder(layoutInflater.inflate(R.layout.item_main, p0, false))
            }
        }
        override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
            val itemV0 = list[p1]

            if(itemV0.type == TYPE_HEADER){
                val viewHolder = p0 as HeaderViewHolder
                val headerItem = itemV0 as HeaderItem
                viewHolder.headerView.text = headerItem.date
            } else {
                val viewHolder = p0 as DataViewHolder
                val dataItem = itemV0 as DataItem
                viewHolder.itemTitleView.text = dataItem.title
                viewHolder.itemContentView.text = dataItem.content
                if(dataItem.completed){
                    viewHolder.completedIconView.setImageResource(R.drawable.icon_completed)
                } else {
                    viewHolder.completedIconView.setImageResource(R.drawable.icon)
                }

                viewHolder.completedIconView.setOnClickListener{
                    val helper = DBHelper(this@MainActivity)
                    val db = helper.writableDatabase

                    if(dataItem.completed){
                        db.execSQL("update tb_todo set completed=? where _id=?", arrayOf(0, dataItem.id))
                        viewHolder.completedIconView.setImageResource(R.drawable.icon)
                    } else {
                        db.execSQL("update tb_todo set completed=? where _id=?", arrayOf(1, dataItem.id))
                        viewHolder.completedIconView.setImageResource(R.drawable.icon_completed)
                    }
                    dataItem.completed = !dataItem.completed
                    db.close()
                }
            }
        }

        override fun getItemCount(): Int {
            return list.size
        }
    }

    inner class MyDecoration(): RecyclerView.ItemDecoration(){
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)
            val index = parent.getChildAdapterPosition(view)
            val itemV0 = list[index]
            if(itemV0.type == TYPE_DATA){
                view.setBackgroundColor(0xFFFFFFFF.toInt())
                ViewCompat.setElevation(view, 10.0f)
            }
            outRect.set(20, 10, 20, 10)
        }
    }
}

abstract class ItemV0{
    abstract val type: Int
    //전역 처리
    companion object{
        const val TYPE_HEADER = 0
        const val TYPE_DATA = 1
    }
}

class HeaderItem(var date: String): ItemV0(){
    override val type: Int
        get() = TYPE_HEADER
}

internal class DataItem(var id: Int, var title: String, var content: String, var completed: Boolean = false): ItemV0(){
    override val type: Int
        get() = TYPE_DATA
}