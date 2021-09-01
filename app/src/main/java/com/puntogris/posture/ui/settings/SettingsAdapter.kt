package com.puntogris.posture.ui.settings

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.puntogris.posture.BuildConfig
import com.puntogris.posture.R
import com.puntogris.posture.model.SettingItem
import com.puntogris.posture.model.SettingUi

class SettingsAdapter(private val context: Context,private val clickListener: (SettingItem) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var list = listOf(
        SettingItem(title = R.string.account_label),
        SettingItem(SettingUi.Name, R.string.name, context.getString(R.string.human)),
        SettingItem(SettingUi.Theme, R.string.theme),
        SettingItem(SettingUi.BatteryOpt,R.string.battery_optimization),
        SettingItem(SettingUi.LogOut, R.string.close, context.getString(R.string.log_out)),
        SettingItem(title = R.string.help),
        SettingItem(SettingUi.Help, R.string.questions, context.getString(R.string.read_the_faq)),
        SettingItem(SettingUi.Ticket, R.string.ticker_feedback,context.getString(R.string.create_ticket)),
        SettingItem(title = R.string.contact),
        SettingItem(SettingUi.Website, R.string.website, context.getString(R.string.got_to_website)),
        SettingItem(SettingUi.Github, R.string.github, context.getString(R.string.see_the_code)),
        SettingItem(title = R.string.about_us),
        SettingItem(SettingUi.Credits, R.string.credits_label, context.getString(R.string.app_credits)),
        SettingItem(SettingUi.RateApp, R.string.rate_us, context.getString(R.string.rate_the_app)),
        SettingItem(SettingUi.Version, R.string.version,context.getString(R.string.version_name, BuildConfig.VERSION_NAME))
        )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) SettingsDividerViewHolder.from(parent)
        else SettingItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        if (item.code == null) (holder as SettingsDividerViewHolder).bind(list[position])
        else (holder as SettingItemViewHolder).bind(list[position], clickListener)
    }

    override fun getItemViewType(position: Int): Int {
        return if (list[position].code == null) 0 else 1
    }

    override fun getItemCount() = list.size

    fun updateUserName(name: String){
        list[1].summary = name
        notifyItemChanged(1)
    }

    fun updateThemeName(name:String){
        list[2].summary = name
        notifyItemChanged(2)
    }
    fun updateBatteryOptimization(isOptimized: Boolean){
        list[3].summary = context.getString(if (isOptimized) R.string.all_in_order else R.string.require_action)
        notifyItemChanged(3)
    }

}