package com.puntogris.posture.ui.settings.credits

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.puntogris.posture.R
import com.puntogris.posture.utils.CreditItem

class CreditsAdapter(private val clickListener: (CreditItem) -> Unit) :
    RecyclerView.Adapter<CreditViewHolder>() {

    private val credits = listOf(
        CreditItem(R.string.panda_credits, R.string.panda_credits_url),
        CreditItem(R.string.bottom_nav_icons_credit, R.string.bottom_nav_icons_credit_url),
        CreditItem(R.string.russian_translation_credit, R.string.russian_translation_credit_url)
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditViewHolder {
        return CreditViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: CreditViewHolder, position: Int) {
        holder.bind(credits[position], clickListener)
    }

    override fun getItemCount() = credits.size
}