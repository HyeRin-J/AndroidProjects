package com.example.clothes.ui.recently

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.clothes.R
import com.example.clothes.*
import android.graphics.BitmapFactory
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clothes.MainActivity.Companion.clothes
import java.util.*
import kotlin.collections.ArrayList


class RecentlyFragment : Fragment() {
    private val adapter = MyAdapter(clothes)
    private lateinit var recyclerView: RecyclerView
    private var recentlyMap = mutableMapOf<String, Array<Int>>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_recently, container, false)
        selectFromRecentlyDB()
        recyclerView = root.findViewById(R.id.recent_list)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(activity!!.applicationContext)
        recyclerView.adapter = adapter
        return root
    }

    class ClothesDataViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateView = view.findViewById<TextView?>(R.id.list_text_date)
        val topView = view.findViewById<ImageView?>(R.id.list_top_img)
        val pantsView = view.findViewById<ImageView?>(R.id.list_pants_img)
        val outerView = view.findViewById<ImageView?>(R.id.list_outer_img)
        val shoesView = view.findViewById<ImageView?>(R.id.list_shoes_img)
    }

    inner class MyAdapter(clothesList: ArrayList<MutableList<Clothes>>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private var tops = mutableListOf<Clothes>()
        private var pants = mutableListOf<Clothes>()
        private var outers = mutableListOf<Clothes>()
        private var shoes = mutableListOf<Clothes>()

        init {
            tops.addAll(clothesList[0])
            pants.addAll(clothesList[1])
            outers.addAll(clothesList[2])
            shoes.addAll(clothesList[3])
        }

        override fun getItemCount(): Int {
            return recentlyMap.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val mLayoutInflater = LayoutInflater.from(parent.context)

            return ClothesDataViewHolder(
                mLayoutInflater.inflate(
                    R.layout.list_view_item,
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val clothesDataViewHolder: ClothesDataViewHolder =
                (holder as ClothesDataViewHolder)
            val date = recentlyMap.keys.elementAt(position)

            clothesDataViewHolder.dateView?.text = date

            clothesDataViewHolder.topView?.setImageBitmap(
                BitmapFactory.decodeByteArray(
                    tops[recentlyMap[date]?.get(0)!!].image,
                    0,
                    tops[recentlyMap[date]?.get(0)!!].image?.size!!
                )
            )

            clothesDataViewHolder.pantsView?.setImageBitmap(
                BitmapFactory.decodeByteArray(
                    pants[recentlyMap[date]?.get(1)!!].image,
                    0,
                    pants[recentlyMap[date]?.get(1)!!].image?.size!!
                )
            )

            clothesDataViewHolder.outerView?.setImageBitmap(
                BitmapFactory.decodeByteArray(
                    outers[recentlyMap[date]?.get(2)!!].image,
                    0,
                    outers[recentlyMap[date]?.get(2)!!].image?.size!!
                )
            )

            clothesDataViewHolder.shoesView?.setImageBitmap(
                BitmapFactory.decodeByteArray(
                    shoes[recentlyMap[date]?.get(3)!!].image,
                    0,
                    shoes[recentlyMap[date]?.get(3)!!].image?.size!!
                )
            )
        }
    }

    private fun selectFromRecentlyDB() {
        val helper = RecentDBHelper(activity!!.applicationContext)
        val db = helper.readableDatabase
        val cursor = db.rawQuery("select * from tb_recently order by date desc", null)

        while (cursor.moveToNext()) {
            val date = cursor.getString(0)
            val idArray =
                arrayOf(cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4))
            recentlyMap[date] = idArray
        }

        cursor.close()
        db.close()
    }
}

