package com.devsai.recipeapp;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public abstract class BaseActivity extends AppCompatActivity {
    public ProgressBar mProgress;
    @Override
    public void setContentView(int layoutResID) {

        ConstraintLayout constraintLayout = (ConstraintLayout)getLayoutInflater().inflate(R.layout.activity_base,null);

        FrameLayout frameLayout = (constraintLayout).findViewById(R.id.contents);

        mProgress = constraintLayout.findViewById(R.id.progress);

        getLayoutInflater().inflate(layoutResID,frameLayout,true);

        super.setContentView(constraintLayout);
    }


    public void showProgressBar(boolean visibility) {

        mProgress.setVisibility(visibility ? View.VISIBLE : View.GONE);

    }
}
