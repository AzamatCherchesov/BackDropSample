package ru.a1tt.backdrop

import androidx.recyclerview.widget.DiffUtil

class DiffCalculator (
    private val oldList: List<TargetApplication>,
    private val newList: List<TargetApplication>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.appName.equals(newItem.appName)
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.packageName.equals(newItem.packageName)
    }
}