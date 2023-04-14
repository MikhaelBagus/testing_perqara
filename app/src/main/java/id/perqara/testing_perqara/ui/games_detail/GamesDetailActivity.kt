package id.perqara.testing_perqara.ui.games_detail

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import id.perqara.testing_perqara.R
import id.perqara.testing_perqara.data.model.GamesModel
import id.perqara.testing_perqara.databinding.ActivityGamesDetailBinding
import id.perqara.testing_perqara.other.base.BaseActivity
import id.perqara.testing_perqara.room.Games
import id.perqara.testing_perqara.room.GamesDatabase
import org.koin.androidx.viewmodel.ext.android.viewModel

class GamesDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityGamesDetailBinding
    private val gamesDetailViewModel: GamesDetailViewModel by viewModel()
    private var appDb: GamesDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_games_detail)
        initView()
    }

    private fun initView(){
        gamesDetailViewModel.gamesId = intent.getIntExtra("games_id",0)
        if (gamesDetailViewModel.gamesId != null) {
            gamesDetailViewModel.getGamesDetail(gamesDetailViewModel.gamesId!!)
            vmHandle()
        }

        binding.toolbar.layoutBackArrow.setOnClickListener {
            finish()
        }
        binding.toolbar.toolbarTitle.text = "Detail"
        binding.txtDescription.settings.javaScriptEnabled = true

        binding.pullToRefresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            gamesDetailViewModel.getGamesDetail(gamesDetailViewModel.gamesId!!)
            binding.pullToRefresh.isRefreshing = false
        })
    }

    private fun vmHandle() {
        gamesDetailViewModel.dataGamesModel.observe(this) { datas ->
            loadGamesDetail(datas)
        }
    }

    private fun loadGamesDetail(item: GamesModel) {
        binding.txtPublisher.text = item.publishers?.get(0)?.name ?: ""
        binding.txtName.text = item.name
        binding.txtReleased.text = item.released
        binding.txtRating.text = item.rating.toString()
        binding.txtPlaytime.text = item.playtime.toString()
        binding.txtDescription.loadDataWithBaseURL(
            "",
            item.description.toString(),
            "text/html",
            "UTF-8",
            ""
        )

        if (item.background_image != null && item.background_image != "") {
            Glide.with(applicationContext).load(item.background_image).into(binding.imgBackground)
        }

        appDb = GamesDatabase.getInstance(applicationContext)
        if(appDb?.gamesDao()?.getGamesDetail(item.id) == null){
            binding.toolbar.imgFavorite.setImageResource(R.drawable.ic_favorite_black_24dp)
        }
        else{
            binding.toolbar.imgFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
        }

        binding.toolbar.layoutFavorite.setOnClickListener {
            if(appDb?.gamesDao()?.getGamesDetail(item.id) == null){
                var gamesData: Games? = null
                gamesData = Games(
                    item.id,
                    item.id,
                    item.name,
                    item.description,
                    item.released,
                    item.background_image,
                    item.rating,
                    item.publishers!![0].name,
                    item.playtime,
                )
                appDb?.gamesDao()?.insertGames(gamesData)
                binding.toolbar.imgFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
            }
            else{
                var gamesData: Games? = null
                gamesData = Games(
                    item.id,
                    item.id,
                    item.name,
                    item.description,
                    item.released,
                    item.background_image,
                    item.rating,
                    item.publishers!![0].name,
                    item.playtime,
                )
                appDb?.gamesDao()?.deleteGames(gamesData)
                binding.toolbar.imgFavorite.setImageResource(R.drawable.ic_favorite_black_24dp)
            }
        }
    }
}