package ddwu.com.mobileapplication.finalproject.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ddwu.com.mobileapplication.finalproject.data.Diary
import ddwu.com.mobileapplication.finalproject.databinding.DiaryItemBinding

class DiaryAdapter(/*val*/ var diarys: List<Diary>) : RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder>() {
    val TAG = "DiaryAdapter"

    override fun getItemCount(): Int {
        return diarys.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryViewHolder {
        val itemBinding = DiaryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DiaryViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: DiaryViewHolder, position: Int) {
        holder.itemBinding.tvDiary.text = diarys[position].toString()
        holder.itemBinding.DiaryItem.setOnLongClickListener{
            itemLongClickListener?.onItemLongClickListener(it, position)
            true
        }
        holder.itemBinding.DiaryItem.setOnClickListener{
            clickListener?.onItemClick(it, position)
        }
    }

    class DiaryViewHolder(val itemBinding: DiaryItemBinding)
        : RecyclerView.ViewHolder(itemBinding.root)

    interface OnItemLongClickListener {
        fun onItemLongClickListener(view: View, pos: Int)
    }

    var itemLongClickListener : OnItemLongClickListener? = null

    fun setOnItemLongClickListener(listener: OnItemLongClickListener?) {
        itemLongClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    var clickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.clickListener = listener
    }
}