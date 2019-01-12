package com.sunladder.summary.view.xfermode

import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.sunladder.summary.R
import kotlinx.android.synthetic.main.summary_act_view_xfermode.*

class ViewXfermodeAct : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.summary_act_view_xfermode)
        viewXferModeRv.layoutManager = GridLayoutManager(this, 2)
        viewXferModeRv.adapter = ViewXfermodeAdapter()
    }
}

data class ViewXfermodeItemModel(
        val name: String?,
        val xfermode: PorterDuffXfermode?,
        val needOffset: Boolean = false
)

class ViewXfermodeAdapter : RecyclerView.Adapter<ViewXfermodeVH>() {

    var dataList: ArrayList<ViewXfermodeItemModel>

    init {
        dataList = ArrayList<ViewXfermodeItemModel>().apply {
            PorterDuff.Mode.values().forEach {
                this.add(ViewXfermodeItemModel(it.name, PorterDuffXfermode(it)))
                this.add(ViewXfermodeItemModel(it.name, PorterDuffXfermode(it), true))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewXfermodeVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.summary_items_view_xfermode, parent, false)
        return ViewXfermodeVH(view)
    }

    override fun getItemCount() = dataList.size

    override fun onBindViewHolder(holder: ViewXfermodeVH, position: Int) {
        holder.bind(dataList[position])
    }
}

class ViewXfermodeVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(model: ViewXfermodeItemModel) {
        itemView.findViewById<TextView>(R.id.viewXferModeItemName).text = model.name
        itemView.findViewById<XferModeView>(R.id.viewXferModeItemV).bind(
                model.xfermode, model.needOffset
        )
    }
}
