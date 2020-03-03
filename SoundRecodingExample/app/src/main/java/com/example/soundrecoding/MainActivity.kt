package com.example.soundrecoding

import android.Manifest
import android.app.DownloadManager
import android.content.pm.PackageManager
import android.icu.text.AlphabeticIndex
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var mediaRecoder: MediaRecorder? = null
    private var mediaPlayer: MediaPlayer? = null
    //lazyinit
    private val sFileName by lazy {
        Environment.getExternalStorageDirectory().absolutePath + "/" + "MyRecoding.3gp"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //퍼미션 허용받기
        btnRecord.setOnClickListener {
            if (!checkMyPermission()) {
                requestMyPermission()
                return@setOnClickListener
            }
            setUpMediaRecoder()
            mediaRecoder!!.prepare()
            mediaRecoder!!.start()
            Toast.makeText(this@MainActivity, "녹음 시작", Toast.LENGTH_LONG).show()
        }
        //녹음 저장하기
        btnRecordSave.setOnClickListener { 
            mediaRecoder!!.stop()
            Toast.makeText(this@MainActivity, "녹음 저장", Toast.LENGTH_LONG).show()
        }
        //녹음 재생하기
        btnPlay.setOnClickListener { 
            mediaPlayer = MediaPlayer()
            mediaPlayer!!.setDataSource(sFileName)
            mediaPlayer!!.prepare()
            mediaPlayer!!.start()
            Toast.makeText(this@MainActivity, "녹음 재생", Toast.LENGTH_LONG).show()
        }
    }
    //퍼미션 요청 및 허락을 받을 상수값
    companion object{
        const val REQUEST_PERMISSION_CODE = 1000
    }
    //퍼미션 요청 결과를 받아오는 핸들러
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
             REQUEST_PERMISSION_CODE -> if(grantResults.isNotEmpty()){
                val storagePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val recordPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED
                if(storagePermission && recordPermission)
                    Toast.makeText(this@MainActivity, "Permission 허락됨", Toast.LENGTH_LONG).show()
                else
                    Toast.makeText(this@MainActivity, "Permission 거부됨", Toast.LENGTH_LONG).show()
            }
        }
    }
    //퍼미션 체크 함수
    private fun checkMyPermission(): Boolean{
        val r = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val r2 = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.RECORD_AUDIO)
        return r == PackageManager.PERMISSION_GRANTED && r2 == PackageManager.PERMISSION_GRANTED
    }
    //퍼미션 요청
    private fun requestMyPermission(){
        ActivityCompat.requestPermissions(
            this@MainActivity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO), REQUEST_PERMISSION_CODE
        )
    }
    //미디어 레코더 초기화
    private fun setUpMediaRecoder(){
        mediaRecoder = MediaRecorder()
        mediaRecoder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecoder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mediaRecoder!!.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB)
        mediaRecoder!!.setOutputFile(sFileName)
    }
}
