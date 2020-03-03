package com.example.clothes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    //static 변수
    companion object {
        var clothes = arrayListOf<MutableList<Clothes>>(
            mutableListOf(),
            mutableListOf(),
            mutableListOf(),
            mutableListOf()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            //다시 한 번 초기화
            clothes =
                arrayListOf(mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf())
            val navView: BottomNavigationView = findViewById(R.id.nav_view)
            //navigation Controller
            val navController = findNavController(R.id.nav_host_fragment)
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            val appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.navigation_home, R.id.navigation_recently, R.id.navigation_favorite
                )
            )

            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)
            //데이터베이스에서 옷 정보 꺼내옴
            selectFromClothesDB()
        }
    }

    private fun selectFromClothesDB() {
        val helper = ClothesDBHelper(applicationContext)
        val db = helper.readableDatabase
        //전부 가져온다
        val cursor = db.rawQuery("select * from tb_clothes order by category desc", null)
        //커서가 다음으로 넘어갈 때마다 값을 가져온다
        while (cursor.moveToNext()) {
            val id = cursor.getInt(0)
            val category = cursor.getInt(1)
            val name = cursor.getString(2)
            val image = cursor.getBlob(3)
            val temp = Clothes(id, category, name, image)
            //가져와서 옷 정보 배열에 집어넣는다
            clothes[category - 1].add(temp)
        }

        cursor.close()
        db.close()
    }
}
