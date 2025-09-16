package com.kcbiswash.course1.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.kcbiswash.course1.databinding.ItemEntityBinding

class EntityAdapter(
    private val items: MutableList<JsonObject>,
    private val onClick: (JsonObject) -> Unit
) : RecyclerView.Adapter<EntityAdapter.VH>() {

    inner class VH(val b: ItemEntityBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemEntityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.b.tvSummary.text = DashboardViewModel.summaryFor(item)
        holder.itemView.setOnClickListener { onClick(item) }
    }

    override fun getItemCount() = items.size

    fun submit(list: List<JsonObject>) {
        items.clear(); items.addAll(list); notifyDataSetChanged()
    }
}
