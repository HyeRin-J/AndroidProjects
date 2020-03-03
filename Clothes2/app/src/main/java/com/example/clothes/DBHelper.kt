package com.example.clothes

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

//옷 DB
class ClothesDBHelper(context: Context) : SQLiteOpenHelper(context, "tbClothesDB", null, 2){
    override fun onCreate(db: SQLiteDatabase?) {
        val clothesSQL = "create table tb_clothes (_id integer primary key autoincrement, category, name, image)"
        db?.execSQL(clothesSQL)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop table tb_clothes")
        onCreate(db)
    }
}
//최근목록DB
class RecentDBHelper(context: Context): SQLiteOpenHelper(context, "tbRecentlyDB", null, 2){
    override fun onCreate(db: SQLiteDatabase?) {
        val sql = "create table tb_recently (date primary key, id_top, id_pants, id_outer, id_shoes)"
        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop table tb_recently")
        onCreate(db)
    }
}
//선호목록DB
class FavoriteDBHelper(context: Context): SQLiteOpenHelper(context, "tbFavoriteDB", null, 2){
    override fun onCreate(db: SQLiteDatabase?) {
        val sql = "create table tb_favorite (num integer primary key autoincrement, id_top, id_pants, id_outer, id_shoes)"
        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop table tb_favorite")
        onCreate(db)
    }
}

class Clothes(id: Int, category: Int, name: String, image: ByteArray) {
    var id = 0
    var category = 0
    var name = ""
    var image: ByteArray? = null

    init {
        this.id = id
        this.category = category
        this.name = name
        this.image = image
    }
}

