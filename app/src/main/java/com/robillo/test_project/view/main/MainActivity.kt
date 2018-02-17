package com.robillo.test_project.view.main

import android.os.Build
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager

import com.robillo.test_project.R
import com.robillo.test_project.view.main.full_screen_fragment.FullScreenFragment
import com.robillo.test_project.view.main.list_fragment.ListFragment

class MainActivity : AppCompatActivity(), MainActivityMvpView {

    private var mFragmentManager: FragmentManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUp()
    }

    override fun setUp() {
        mFragmentManager = supportFragmentManager
        showListFragment()
    }

    override fun showListFragment() {
        mFragmentManager!!.beginTransaction()
                .add(R.id.fragment_container, ListFragment(), getString(R.string.list_fragment_tag)).commit()
    }

    override fun addFullScreenFragment(url: String) {
        val fragment = FullScreenFragment()
        val args = Bundle()
        args.putString(getString(R.string.flag_url), url)
        fragment.arguments = args

        mFragmentManager!!.beginTransaction()
                .add(R.id.fragment_container, fragment, getString(R.string.full_screen_fragment_tag)).commit()
    }

    override fun removeFullScreenFragment() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
        }
        mFragmentManager!!.beginTransaction()
                .remove(mFragmentManager!!.findFragmentByTag(getString(R.string.full_screen_fragment_tag))).commit()
    }

    override fun onBackPressed() {
        if (mFragmentManager!!.findFragmentByTag(getString(R.string.full_screen_fragment_tag)) != null)
            removeFullScreenFragment()
        else
            super.onBackPressed()
    }
}
