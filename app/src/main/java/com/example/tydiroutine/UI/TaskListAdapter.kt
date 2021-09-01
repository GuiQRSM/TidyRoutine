package com.example.tydiroutine.UI

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tydiroutine.R
import com.example.tydiroutine.databinding.ItemTaskBinding
import com.example.tydiroutine.model.Task

class TaskListAdapter: ListAdapter<Task, TaskListAdapter.TaskviewHolder>(DiffCallBack()) {

    var listenerEdit: (Task) -> Unit = {}
    var listenerDelete: (Task) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskviewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskBinding.inflate(inflater, parent, false)
        return TaskviewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskviewHolder, position: Int) {
        holder.bind(getItem(position))
    }

   inner class TaskviewHolder(
        private val binding: ItemTaskBinding
        ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Task) {
            binding.tvTitle.text = item.title
            binding.tvDate.text = "${item.date} ${item.hour}"
            binding.imPopup.setOnClickListener {
                showPopUp(item)
            }
        }

        private fun showPopUp(item: Task) {
            val imPopUp = binding.imPopup
            val popupMenu = PopupMenu(imPopUp.context, imPopUp)
            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_edit -> listenerEdit(item)
                    R.id.action_delete -> listenerDelete(item)
                }
                return@setOnMenuItemClickListener true
            }
            popupMenu.show()
        }

    }
}

class DiffCallBack: DiffUtil.ItemCallback<Task>() {

    override fun areItemsTheSame(oldItem: Task, newItem: Task) = oldItem == newItem

    override fun areContentsTheSame(oldItem: Task, newItem: Task) = oldItem.id == newItem.id

}