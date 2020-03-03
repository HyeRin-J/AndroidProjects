package hyerin.example.using_webview_blogbook

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*

const val URL_HOME = "https://m.blog.naver.com/PostList.nhn?blogId=jamduino"
const val URL_NEWS = "https://m.blog.naver.com/PostList.nhn?blogId=jamduino&categoryNo=59&listStyle=style2"
const val URL_DAILY = "https://m.blog.naver.com/PostList.nhn?blogId=jamduino&categoryNo=61&listStyle=style2"
const val URL_INFO = "https://m.blog.naver.com/PostList.nhn?blogId=jamduino&categoryNo=45&listStyle=style2"

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = drawer_layout
        val navView: NavigationView = nav_view
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        setUpUI()
        navView.setNavigationItemSelectedListener(this)
    }
    private inner class WebClient: WebViewClient(){
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            return super.shouldOverrideUrlLoading(view, request)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
        }
    }
    private fun setUpUI(){
        //웹페이지 진행 상황을 관리
        wbMain.webViewClient = WebClient()
        //세팅 가져오기
        val set = wbMain.settings
        set.javaScriptEnabled = true
        set.builtInZoomControls = false
        //이동
        wbMain.loadUrl("https://m.blog.naver.com/PostList.nhn?blogId=jamduino")
        toolbar.title = "잼코딩 블로그"
    }
    //백 버튼을 눌렀을 때
    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = drawer_layout
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            //구현부
            if(wbMain.canGoBack()){
                wbMain.goBack()
            }else {
                super.onBackPressed()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
    //메뉴가 선택되었을 때
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.navHome -> {
                // Handle the camera action
                wbMain.loadUrl(URL_HOME)
            }
            R.id.navNews -> {
                wbMain.loadUrl(URL_NEWS)
            }
            R.id.navDaily -> {
                wbMain.loadUrl(URL_DAILY)
            }
            R.id.navInfo -> {
                wbMain.loadUrl(URL_INFO)
            }

        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
