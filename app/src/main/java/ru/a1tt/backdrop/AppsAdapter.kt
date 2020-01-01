package ru.a1tt.backdrop

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card_appinfo_view.view.*

class AppsAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var list: List<TargetApplication> = emptyList()
    var appsIcons: HashMap<String, Bitmap>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AppViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.card_appinfo_view,
                parent,
                false
            ))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.AppName.text = list[position].appName
        holder.itemView.PackageName.text = list[position].packageName
        holder.itemView.imageView2.setImageBitmap(appsIcons?.get(list[position].packageName))
    }

    data class AppViewHolder(val view: View): RecyclerView.ViewHolder(view)
}