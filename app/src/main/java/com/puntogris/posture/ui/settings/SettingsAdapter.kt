package com.puntogris.posture.ui.settings

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.puntogris.posture.BuildConfig
import com.puntogris.posture.model.SettingItem
import com.puntogris.posture.model.SettingUi

class SettingsAdapter(private val clickListener: (SettingItem) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var list = listOf(
        SettingItem(title = "Cuenta"),
        SettingItem(SettingUi.Name, "Nombre", "Terricola"),
        SettingItem(SettingUi.Theme,"Tema",""),
        SettingItem(SettingUi.BatteryOpt,"Optimiz. bateria",""),
        SettingItem(SettingUi.LogOut,"Cerrar",""),
        SettingItem(title = "Ayuda"),
        SettingItem(SettingUi.Help,"Preguntas","Lee las F.A.Q."),
        SettingItem(SettingUi.Ticket,"Ticket /Feedback","Crea un ticket"),
        SettingItem(title = "Contacto"),
        SettingItem(SettingUi.Website,"Website","Ir a la pagina"),
        SettingItem(SettingUi.Github,"Github","Ver el codigo"),
        SettingItem(title = "App"),
        SettingItem(SettingUi.RateApp,"Calificanos","Puntua la app"),
        SettingItem(SettingUi.Version,"Version","Version ${BuildConfig.VERSION_NAME}")
        )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0)
            SettingsDividerViewHolder.from(parent)
        else
            SettingItemViewHolder.from(parent)
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
        list[3].summary = if (isOptimized) "Todo en orden" else "Necesita accion"
        notifyItemChanged(3)
    }

}