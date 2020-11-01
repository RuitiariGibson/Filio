package com.gibsoncodes.filio.binding

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import com.gibsoncodes.filio.R
import com.google.android.material.appbar.MaterialToolbar

fun AppCompatActivity.setUpToolBarWithBackButton(toolbar: MaterialToolbar) {
    setSupportActionBar(toolbar)
    supportActionBar?.run {
        setHomeAsUpIndicator(R.drawable.back_button)
        setDisplayHomeAsUpEnabled(true)
    }
}

@BindingAdapter("simpleToolbarWithUpButton")
fun bindToolBarWithTitle(toolbar: MaterialToolbar, activity: AppCompatActivity) {
    activity.setUpToolBarWithBackButton(toolbar)
}