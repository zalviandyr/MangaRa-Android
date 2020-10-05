package com.zukron.mangara.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.zukron.mangara.R
import com.zukron.mangara.adapter.ViewPagerAdapter
import com.zukron.mangara.ui.fragment.AccountFragment
import com.zukron.mangara.ui.fragment.FavoriteFragment
import com.zukron.mangara.ui.fragment.HistoryFragment
import com.zukron.mangara.ui.fragment.HomeFragment
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // fragment list
        val fragmentList: MutableList<Fragment> = mutableListOf(
            HomeFragment(),
            HistoryFragment(),
            FavoriteFragment(),
            AccountFragment()
        )

        // adapter
        val adapter = ViewPagerAdapter(fragmentList, supportFragmentManager, lifecycle)
        homeAct_viewPager2.adapter = adapter

        // view pager
        homeAct_viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> homeAct_bottomNavigation.selectedItemId = R.id.nav_home
                    1 -> homeAct_bottomNavigation.selectedItemId = R.id.nav_history
                    2 -> homeAct_bottomNavigation.selectedItemId = R.id.nav_favorite
                    3 -> homeAct_bottomNavigation.selectedItemId = R.id.nav_account
                }
            }
        })

        // bottom navigation
        homeAct_bottomNavigation.setOnNavigationItemSelectedListener {
            return@setOnNavigationItemSelectedListener when (it.itemId) {
                R.id.nav_home -> {
                    homeAct_viewPager2.currentItem = 0
                    true
                }
                R.id.nav_history -> {
                    homeAct_viewPager2.currentItem = 1
                    true
                }
                R.id.nav_favorite -> {
                    homeAct_viewPager2.currentItem = 2
                    true
                }
                R.id.nav_account -> {
                    homeAct_viewPager2.currentItem = 3
                    true
                }
                else -> false
            }
        }
    }
}