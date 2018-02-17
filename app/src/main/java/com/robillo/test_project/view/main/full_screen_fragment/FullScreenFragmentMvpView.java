package com.robillo.test_project.view.main.full_screen_fragment;

import android.view.View;
import android.widget.ImageView;

/**
 * Created by robinkamboj on 17/02/18.
 */

public interface FullScreenFragmentMvpView {

    void setUp(View v);

    void loadImage(ImageView imageView, String url);
}
