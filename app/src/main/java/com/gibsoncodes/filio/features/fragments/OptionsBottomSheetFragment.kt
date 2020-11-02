package com.gibsoncodes.filio.features.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gibsoncodes.filio.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_options.*
import kotlinx.android.synthetic.main.recent_files_item.*

class OptionsBottomSheetFragment: BottomSheetDialogFragment() {
    override fun getTheme(): Int {
        return R.style.BottomSheetDialogBase
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.bottom_sheet_options, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
      if (context is ItemClickListener){
          listener = context as ItemClickListener
      }else{
          throw RuntimeException("$context must implement this interface")
      }
    }

    private fun setUpViews(){
        delete.setOnClickListener {
            dismissAllowingStateLoss()
            listener?.onItemClick("delete")
        }
        viewBtn.setOnClickListener {
            dismissAllowingStateLoss()
            listener?.onItemClick("view")
        }

    }
    private var listener:ItemClickListener? = null
interface  ItemClickListener{
    fun onItemClick(item:String)
}
    override fun onDestroy() {
        super.onDestroy()
        listener = null

    }
    companion object{
        @JvmStatic
        fun newInstance(bundle:Bundle):OptionsBottomSheetFragment{
            val fragment = OptionsBottomSheetFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
    }

}