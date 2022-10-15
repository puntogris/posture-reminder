package com.puntogris.posture.ui.settings.credits

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.puntogris.posture.databinding.CreditItemVhBinding
import com.puntogris.posture.utils.CreditItem

class CreditViewHolder(private val binding: CreditItemVhBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(creditItem: CreditItem, clickListener: (CreditItem) -> Unit, position: Int) {
        binding.creditTextView.setText(creditItem.credit)
        binding.root.setOnClickListener { clickListener(creditItem) }
    }

    companion object {
        fun from(parent: ViewGroup): CreditViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = CreditItemVhBinding.inflate(layoutInflater, parent, false)
            return CreditViewHolder(binding)
        }
    }
}