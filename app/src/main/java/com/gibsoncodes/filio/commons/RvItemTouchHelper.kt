package com.gibsoncodes.filio.commons

import android.content.Context
import android.content.ContextWrapper
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RvItemTouchHelper (context:Context, private val listener:ListenerInterface): RecyclerView.SimpleOnItemTouchListener (){
    private var view:View?=null
    private var position:Int?=null
    private  var gestureDetector:GestureDetector?=null
    init{
       gestureDetector = GestureDetector(context, object:GestureDetector.SimpleOnGestureListener(){
            override fun onSingleTapUp(e: MotionEvent?): Boolean {
                view?.let{
                  position?.let{
                      pos->
                      listener.onClick(it, pos)
                  }
                }
                return true
            }

            override fun onLongPress(e: MotionEvent?) {
                view?.let {
                    position?.let{
                        pos->
                        listener.onLongClick(it, pos)
                    }
                }
                super.onLongPress(e)
            }
        })
    }
    interface ListenerInterface{
        fun onClick(view:View, position:Int)
        fun onLongClick(view:View, position:Int)
    }
    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        view = rv.findChildViewUnder(e.x, e.y)
        view?.let{
            position = rv.getChildAdapterPosition(it)
        }

        return view!=null && gestureDetector?.onTouchEvent(e)!!
    }

}