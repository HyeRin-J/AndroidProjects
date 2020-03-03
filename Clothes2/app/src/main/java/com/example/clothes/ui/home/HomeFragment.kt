package com.example.clothes.ui.home

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.ContentValues
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.clothes.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import android.os.Build
import android.graphics.Bitmap
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker.*
import androidx.lifecycle.MutableLiveData
import com.example.clothes.MainActivity.Companion.clothes
import java.lang.Boolean.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random


class HomeFragment : Fragment() {
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var imageButtons: Array<View>
    private lateinit var menuButtons: Array<View>
    private var indexArray = arrayOf(0, 0, 0, 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(
                    activity!!.applicationContext,
                    Manifest.permission.CAMERA
                ) == PERMISSION_DENIED && checkSelfPermission(
                    activity!!.applicationContext,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PERMISSION_DENIED
            ) {
                ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    1
                )
            }
        }
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        indexArray = homeViewModel.randomImageSelect()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        imageButtons = arrayOf(root.top_btn, root.pants_btn, root.outer_btn, root.shoes_btn)
        menuButtons = arrayOf(root.switch_btn, root.save_btn, root.add_btn)
        root.text_date.text = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(Date())
        homeViewModel.images.observe(this, Observer<Array<ByteArray?>> {
            var i = 0
            it.forEach { byte ->
                if (byte != null && byte.isNotEmpty()) {
                    (imageButtons[i] as ImageButton).setImageBitmap(
                        BitmapFactory.decodeByteArray(
                            byte,
                            0,
                            byte.size
                        )
                    )
                }
                i++
            }
            updateRecentlyDB()
        })

        imageButtons.forEach {
            it.setOnClickListener { btn -> onClickHomeBtn(btn) }
        }
        menuButtons.forEach {
            it.setOnClickListener { btn -> onClickHomeBtn(btn) }
        }
        return root
    }

    private fun onClickHomeBtn(v: View) {
        when (v.id) {
            R.id.add_btn -> {
                val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, 1)
            }
            R.id.save_btn -> {
                updateFavoriteFromDB()
            }
            R.id.switch_btn -> {
                updateRecentlyDB()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            1 -> if (resultCode == RESULT_OK && data!!.hasExtra("data")) {
                val bitmap = data.extras!!.get("data") as Bitmap
                val intent = Intent(activity?.applicationContext, AddClothesActivity::class.java)
                if (bitmap.byteCount != 0) {
                    intent.putExtra("image", bitmap)
                    startActivity(intent)
                }
            }
        }
    }

    private fun updateRecentlyDB() {
        val helper = RecentDBHelper(activity!!.applicationContext)
        val db = helper.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("id_top", indexArray[0])
        contentValues.put("id_pants", indexArray[1])
        contentValues.put("id_outers", indexArray[2])
        contentValues.put("id_shoes", indexArray[3])
        val sql =
            "insert or replace into tb_recently (date, id_top, id_pants, id_outer, id_shoes) values(CURRENT_DATE, ${indexArray[0]}, ${indexArray[1]}, ${indexArray[2]}, ${indexArray[3]})"
        db.execSQL(sql)
        db.close()
    }

    private fun updateFavoriteFromDB() {
        val helper = FavoriteDBHelper(activity!!.applicationContext)
        val db = helper.writableDatabase
        //중복 검사를 위해 데이터베이스에서 현재 인덱스로 된 값이 있는지 검사함
        val cursor = db.rawQuery(
            "select id_top, id_pants, id_outer, id_shoes from tb_favorite where id_top = ${indexArray[0]} and id_pants = ${indexArray[1]} and id_outer = ${indexArray[2]} and id_shoes = ${indexArray[3]}",
            null
        )

        //중복 메시지 출력
        Toast.makeText(
            activity!!.applicationContext,
            if (!cursor.moveToFirst()) {
                val contentValues = ContentValues()
                contentValues.put("id_top", indexArray[0])
                contentValues.put("id_pants", indexArray[1])
                contentValues.put("id_outer", indexArray[2])
                contentValues.put("id_shoes", indexArray[3])

                db.insert("tb_favorite", null, contentValues)
                "정상적으로 추가되었습니다."
            } else {
                "중복된 즐겨찾기가 존재합니다."
            }, Toast.LENGTH_LONG
        ).show()
        cursor.close()
        db.close()
    }

}