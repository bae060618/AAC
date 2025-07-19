package com.example.aac

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.widget.Toolbar
import com.example.aac.favorite.FavoritesActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)

        val themeColor = ThemeManager.getThemeColor(this)

        toolbar.setBackgroundColor(themeColor)

        val headerView = navigationView.getHeaderView(0)
        val headerContainer = headerView.findViewById<View>(R.id.header_container)
        headerContainer.setBackgroundColor(themeColor)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.open, R.string.close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener(this)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.content_frame, CategoryCardFragment())
                .commit()
        }
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_item1 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.content_frame, ProfileFragment())
                    .commit()
            }
            R.id.nav_item2 -> {
                val intent = Intent(this, FavoritesActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_item3 -> {
                FirebaseAuth.getInstance().signOut()

                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                val googleSignInClient = GoogleSignIn.getClient(this, gso)
                googleSignInClient.signOut()

                Toast.makeText(this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            R.id.nav_item4 -> {
                AlertDialog.Builder(this)
                    .setTitle("앱 종료")
                    .setMessage("앱을 종료하시겠습니까?")
                    .setPositiveButton("예") { _, _ -> finishAffinity() }
                    .setNegativeButton("아니오", null)
                    .show()
            }
            R.id.nav_item5 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.content_frame, CategoryCardFragment())
                    .commit()
            }
            R.id.nav_item6 -> {
                val intent = Intent(this, RecommendedBookActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_item7 -> {
                val intent = Intent(this, SavedBookActivity::class.java)
                startActivity(intent)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}

