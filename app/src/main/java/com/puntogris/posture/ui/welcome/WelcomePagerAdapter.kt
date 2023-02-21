package com.puntogris.posture.ui.welcome

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.puntogris.posture.R
import com.puntogris.posture.utils.WelcomeItem

class WelcomePagerAdapter(isDarkModeOn: Boolean) : RecyclerView.Adapter<WelcomeItemViewHolder>() {

    private var items = listOf(
        WelcomeItem(
            R.string.welcome_pager_title1,
            if (isDarkModeOn) R.drawable.ic_girl_swing_dark else R.drawable.ic_girl_swing
        ),
        WelcomeItem(
            R.string.welcome_pager_title2,
            if (isDarkModeOn) R.drawable.ic_men_standing_dark else R.drawable.ic_men_standing
        ),
        WelcomeItem(
            R.string.welcome_pager_title3,
            if (isDarkModeOn) R.drawable.ic_girl_sitting_dark else R.drawable.ic_girl_sitting
        )
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WelcomeItemViewHolder {
        return WelcomeItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: WelcomeItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}
