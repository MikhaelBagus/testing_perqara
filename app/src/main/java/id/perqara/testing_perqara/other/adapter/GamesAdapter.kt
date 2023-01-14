package id.perqara.testing_perqara.other.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import id.perqara.testing_perqara.R
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
            txtName.text = item.name
            txtReleased.text = item.released
            txtRating.text = item.rating.toString()

            progressBar.visibility = View.VISIBLE
            if (item.background_image != null && item.background_image != "") {
                Picasso.get().load(item.background_image).into(imgBackground, object : Callback {
                    override fun onSuccess() {
                        progressBar.visibility = View.GONE
                    }

                    override fun onError(e: Exception) {
                        progressBar.visibility = View.GONE
                    }
                })
            }
            else{
                progressBar.visibility = View.GONE
            }

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
        val txtName: TextView = itemView.findViewById(R.id.txt_name)
        val txtReleased: TextView = itemView.findViewById(R.id.txt_released)
        val txtRating: TextView = itemView.findViewById(R.id.txt_rating)
        val imgBackground: ImageView = itemView.findViewById(R.id.img_background)
        val progressBar: ProgressBar = itemView.findViewById(R.id.progress_bar)
        val layoutMain: FrameLayout = itemView.findViewById(R.id.layout_main)
    }

}