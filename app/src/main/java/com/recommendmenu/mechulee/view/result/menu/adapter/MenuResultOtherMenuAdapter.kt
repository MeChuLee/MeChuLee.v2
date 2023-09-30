package com.recommendmenu.mechulee.view.result.menu.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.recommendmenu.mechulee.R
import com.recommendmenu.mechulee.model.data.MenuInfo
import com.recommendmenu.mechulee.utils.Constants.INTENT_NAME_RESULT
import com.recommendmenu.mechulee.view.result.menu.MenuResultActivity

class MenuResultOtherMenuAdapter : RecyclerView.Adapter<MenuResultOtherMenuAdapter.ViewHolder>() {
    var datas = ArrayList<MenuInfo>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val otherMenuImage: AppCompatImageView = itemView.findViewById(R.id.resultImageView)
        val otherMenuText: AppCompatTextView = itemView.findViewById(R.id.resultTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_result_other_menu, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val myExample = R.drawable.example_result
        holder.otherMenuImage.setImageResource(myExample)
        holder.otherMenuText.text = datas[position].name

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, MenuResultActivity::class.java)
            intent.putExtra(INTENT_NAME_RESULT, datas[position])
            ContextCompat.startActivity(holder.itemView.context, intent, null)
        }
    }

    override fun getItemCount(): Int {
        return datas.size
    }


}