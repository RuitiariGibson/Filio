package com.gibsoncodes.filio.commons

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridItemDecoration(private  val includeEdge:Boolean,
private val spanCount:Int, private val margin:Int):RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position =parent.getChildAdapterPosition(view)
        val column = position%spanCount
        if (includeEdge){
            outRect.left = margin-column*margin/spanCount
            outRect.right = (column+1)*margin/spanCount
            if (position>=spanCount){
                outRect.top=position
            }
            outRect.bottom=position
        }else{
            outRect.left = column*margin/spanCount
            outRect.right = margin-(column+1)*margin/spanCount
            if (position>spanCount){
                outRect.top = position
            }
        }
    }
}