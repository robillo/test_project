package com.robillo.test_project.view.main.list_fragment

import android.view.View

/**
 * Created by robinkamboj on 17/02/18.
 */

interface ListFragmentMvpView {

    fun setUp(v: View)

    fun retry()

    fun callForWorldDetails()

    fun showLoading()

    fun showRetry()

    fun showRecycler()

}
