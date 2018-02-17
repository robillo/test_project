package com.robillo.test_project.view.main

/**
 * Created by robinkamboj on 17/02/18.
 */

interface MainActivityMvpView {

    fun setUp()

    fun showListFragment()

    fun addFullScreenFragment(url: String)

    fun removeFullScreenFragment()
}
