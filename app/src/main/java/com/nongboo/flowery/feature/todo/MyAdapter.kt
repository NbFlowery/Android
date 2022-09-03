package com.nongboo.flowery.feature.todo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nongboo.flowery.R
import com.nongboo.flowery.databinding.ItemLayoutBinding

class MyAdapter(private val viewModel: TodoViewModel) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setContents() { //+버튼이나 수정한걸로 text 내용 변경

            val item = viewModel.getItem(adapterPosition)

            binding.title.text = item.name

            binding.root.setOnLongClickListener { //롱클릭했을때
                viewModel.longClickItem = adapterPosition
                false //계속해서 처리되게
            }

            var todoButtonClickCount = 0

            binding.todoButton.setOnClickListener { //todoButton 클릭했을 때
                todoButtonClickCount = (todoButtonClickCount + 1) % 3

                when (todoButtonClickCount) {
                    1 -> binding.todoButton.setBackgroundResource(R.drawable.shape_for_half_circle_button)
                    2 -> {
                        binding.todoButton.setBackgroundResource(R.drawable.shape_for_full_circle_button)
                        viewModel.changeItemState(adapterPosition)
                    }
                    else -> {
                        binding.todoButton.setBackgroundResource(R.drawable.shape_for_circle_button)
                        viewModel.changeItemState(adapterPosition)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLayoutBinding
            .inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ) // 당장 붙히지 않는다 false

        return ViewHolder(binding)
    }

    //viewHolder에 데이터 쓰기
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setContents()
    }

    override fun getItemCount() = viewModel.itemsData.value?.size ?: 0

}