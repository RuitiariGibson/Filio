package com.gibsoncodes.filio.features.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gibsoncodes.filio.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_options.*
import kotlinx.android.synthetic.main.recent_files_item.*
import java.text.SimpleDateFormat

class OptionsBottomSheetFragment: BottomSheetDialogFragment() {
    override fun getTheme(): Int {
        return R.style.BottomSheetDialogBase
    }

    private var dateAddedText:Long?=null
    private var fileNameText:String?=null
    private var fileSize:Int?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fileNameText= arguments?.getString("name")
        fileSize = arguments?.getInt("size")
        dateAddedText = arguments?.getLong("dateAdded")
    }
    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         val view = inflater.inflate(R.layout.bottom_sheet_options,container, false)
        val nameTextView:TextView = view.findViewById(R.id.fileName)
        val dateTextView :TextView = view.findViewById(R.id.dateAdded)
        val sizeTextView:TextView = view.findViewById(R.id.size)
        fileNameText?.let{
            val finalString ="name $it"
            nameTextView.text = finalString
        }
        fileSize?.let{
            val size= "size ${Formatter.formatFileSize(requireContext(), it.toLong())}"
            sizeTextView.text = size
        }
        dateAddedText?.let{
            val timeStamp= it*1000
            SimpleDateFormat("dd.MM.yyyy").format(timeStamp).let {
                stringDate->
                val finalString = "added on $stringDate"
                dateTextView.text = finalString
            }
        }

        return view

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
      if (context is ItemClickListener){
          listener = context
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