package com.gibsoncodes.filio.commons

import android.view.View
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavigationBehaviour :CoordinatorLayout.Behavior<BottomNavigationView>(){
    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: BottomNavigationView,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: BottomNavigationView,
        dependency: View
    ): Boolean {
        return dependency is FrameLayout
    }
    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: BottomNavigationView,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        if (dy<0){
            showBottomNavigationView(child)
        }else if (dy>0){
            hideBottomNavigationView(child)
        }
    }
    private fun showBottomNavigationView(view:BottomNavigationView){
        view.animate().translationY(view.height.toFloat())
    }
    private fun hideBottomNavigationView(view:BottomNavigationView){
        view.animate().translationY(0f)
    }
}