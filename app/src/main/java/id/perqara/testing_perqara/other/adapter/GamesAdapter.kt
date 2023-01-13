package id.perqara.testing_perqara.other.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.perqara.testing_perqara.data.model.GamesModel

class GamesAdapter(
    private var itemList: MutableList<GamesModel>,
    val onItemGamesClicked: (item: GamesModel) -> Unit,
) :
    RecyclerView.Adapter<GamesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.list_games, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val item = itemList[position]
        holder.apply {
            txtTitle.text = item.title

            layoutMain.setOnClickListener {
                onItemGamesClicked(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun setItemList(itemList: List<GamesModel>) {
        if (this.itemList == itemList) return
        this.itemList = itemList.toMutableList()
        notifyDataSetChanged()
    }

    fun addItemList(itemList: List<GamesModel>) {
        this.itemList.addAll(itemList)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTitle: TextView = itemView.findViewById(R.id.txt_title)
        val layoutMain: FrameLayout = itemView.findViewById(R.id.layout_main)
    }

}