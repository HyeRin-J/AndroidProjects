package com.example.clothes.ui.favorite

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clothes.Clothes
import com.example.clothes.ClothesDBHelper
import com.example.clothes.FavoriteDBHelper
import com.example.clothes.MainActivity.Companion.clothes
import com.example.clothes.R
import com.example.clothes.ui.recently.RecentlyFragment

class FavoritesFragment : Fragment() {
    private val adapter = MyAdapter(clothes)
    private lateinit var recyclerView: RecyclerView
    private val favoriteMap = mutableMapOf<Int, Array<Int>>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_favorite, container, false)
        recyclerView = root.findViewById(R.id.favorite_list)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(activity!!.applicationContext)
        recyclerView.adapter = adapter
        selectFromFavoriteDB()
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
            return favoriteMap.size
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

            clothesDataViewHolder.dateView?.text = position.toString()

            clothesDataViewHolder.topView?.setImageBitmap(
                BitmapFactory.decodeByteArray(
                    tops[favoriteMap[position]?.get(0)!!].image,
                    0,
                    tops[favoriteMap[position]?.get(0)!!].image?.size!!
                )
            )

            clothesDataViewHolder.pantsView?.setImageBitmap(
                BitmapFactory.decodeByteArray(
                    pants[favoriteMap[position]?.get(1)!!].image,
                    0,
                    pants[favoriteMap[position]?.get(1)!!].image?.size!!
                )
            )

            clothesDataViewHolder.outerView?.setImageBitmap(
                BitmapFactory.decodeByteArray(
                    outers[favoriteMap[position]?.get(2)!!].image,
                    0,
                    outers[favoriteMap[position]?.get(2)!!].image?.size!!
                )
            )

            clothesDataViewHolder.shoesView?.setImageBitmap(
                BitmapFactory.decodeByteArray(
                    shoes[favoriteMap[position]?.get(3)!!].image,
                    0,
                    shoes[favoriteMap[position]?.get(3)!!].image?.size!!
                )
            )
        }
    }


    private fun selectFromFavoriteDB() {
        val helper = FavoriteDBHelper(activity!!.applicationContext)
        val db = helper.readableDatabase
        val cursor = db.rawQuery(
            "select id_top, id_pants, id_outer, id_shoes from tb_favorite group by id_top, id_pants, id_outer, id_shoes order by num desc",
            null
        )

        var num = 0
        while (cursor.moveToNext()) {
            val idArray =
                arrayOf(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3))
            favoriteMap[num] = idArray
            num++
        }

        cursor.close()
        db.close()
    }
}