package ddwu.com.mobileapplication.finalproject.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ddwu.com.mobileapplication.finalproject.data.Medical
import ddwu.com.mobileapplication.finalproject.databinding.ListItemBinding

class MedicalAdapter : RecyclerView.Adapter<MedicalAdapter.ItemHolder>() {
    var medicals: List<Medical>? = null

    override fun getItemCount(): Int = medicals?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemBinding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.itemBinding.tvItem.text = (medicals?.get(position) as Medical).toString()
        holder.itemBinding.clItem.setOnClickListener{
            clickListener?.onItemClick(it, position)
        }
    }

    class ItemHolder(val itemBinding: ListItemBinding) : RecyclerView.ViewHolder(itemBinding.root)

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    var clickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.clickListener = listener
    }
}