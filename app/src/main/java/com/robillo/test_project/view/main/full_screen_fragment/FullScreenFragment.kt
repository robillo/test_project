package com.robillo.test_project.view.main.full_screen_fragment


import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView

import com.bumptech.glide.Glide
import com.robillo.test_project.R

/**
 * A simple [Fragment] subclass.
 */
class FullScreenFragment : Fragment(), FullScreenFragmentMvpView {

    internal lateinit var mFlag: ImageView


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater!!.inflate(R.layout.fragment_full_screen, container, false)

        setUp(v)

        return v
    }

    override fun setUp(v: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(activity, R.color.black)
        }

        val url = arguments.getString(getString(R.string.flag_url))
        mFlag = v.findViewById(R.id.flag)
        loadImage(mFlag, url)
    }

    override fun loadImage(imageView: ImageView, url: String?) {
        Glide.with(context).load(url).fitCenter().crossFade(0).into(imageView)
    }
}// Required empty public constructor
