package com.arzaapps.android.yamobile.views

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageSwitcher
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arzaapps.android.yamobile.R
import com.arzaapps.android.yamobile.data.entities.ActionData
import com.squareup.picasso.Picasso
import java.math.BigDecimal
import java.math.RoundingMode

class ListAdapter(
    private val items: List<ActionData>,
    private val listener: ActionViewHolder.OnActionClickListener
) :
    RecyclerView.Adapter<ActionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ActionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ActionViewHolder, position: Int) {
        holder.bind(items[position], listener)
    }

}

class ActionViewHolder(
    itemView: View,
) : RecyclerView.ViewHolder(itemView) {

    private lateinit var actionData: ActionData

    private val tickerCompany: TextView = itemView.findViewById(R.id.ticker_company)
    private val nameCompany: TextView = itemView.findViewById(R.id.name_company)
    private val price: TextView = itemView.findViewById(R.id.price)
    private val changePriceForDay: TextView = itemView.findViewById(R.id.change_price_for_day)
    private val image: ImageView = itemView.findViewById(R.id.img)
    private val imageSwitcher: ImageSwitcher = itemView.findViewById(R.id.image_switcher)

    @SuppressLint("SetTextI18n")
    fun bind(item: ActionData, clickListener: OnActionClickListener) {
        itemView.setOnClickListener { clickListener.onClickedAction(item.companyProfile.ticker) }
        this.actionData = item
        if (item.companyProfile.isFavorite) {
            imageSwitcher.setImageResource(R.drawable.ic_enable)
        } else {
            imageSwitcher.setImageResource(R.drawable.ic_disable)
        }
        tickerCompany.text = item.companyProfile.ticker
        nameCompany.text = item.companyProfile.name
        price.text =
            "${item.actionCosts.c} ${item.companyProfile.currency}"
        val start = item.actionCosts.o.toDouble()
        val current = item.actionCosts.c.toDouble()
        val changeCost = current - start
        var percentChangeCost = 0.0
        if (start != 0.0) {
            percentChangeCost = changeCost / start * 100
        }
        if (changeCost > 0) {
            changePriceForDay.text =
                "+ ${BigDecimal(changeCost).setScale(2, RoundingMode.HALF_EVEN)} (${
                    (BigDecimal(
                        percentChangeCost
                    ).setScale(2, RoundingMode.HALF_EVEN))
                }%)"
            changePriceForDay.setTextColor(Color.GREEN)
        } else {
            changePriceForDay.text =
                "${BigDecimal(changeCost).setScale(2, RoundingMode.HALF_EVEN)} (${
                    (BigDecimal(
                        percentChangeCost
                    ).setScale(2, RoundingMode.HALF_EVEN))
                }%)"
            changePriceForDay.setTextColor(Color.RED)
        }
        Picasso.get()
            .load(
                when {
                    item.companyProfile.logo.isEmpty() -> "xxxxx"
                    else -> item.companyProfile.logo
                }
            )
            .error(R.drawable.ic_enable)
            .into(image);
    }

    interface OnActionClickListener {
        fun onClickedAction(ticker: String)
    }
}

