package com.example.mechulee

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class CustomSpinnerAdapter(context: Context, items: Array<String>) : ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        (view as? TextView)?.apply {
            gravity = Gravity.END // 글자를 오른쪽으로 정렬
        }
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        (view as? TextView)?.apply {
            gravity = Gravity.END // 드롭다운 목록의 글자를 오른쪽으로 정렬
        }
        return view
    }
}