package id.perqara.testing_perqara.ui.favorite

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import id.perqara.testing_perqara.DBHandler
import id.perqara.testing_perqara.MainActivity
import id.perqara.testing_perqara.R
import id.perqara.testing_perqara.data.model.GamesModel
import id.perqara.testing_perqara.databinding.FragmentFavoriteBinding
import id.perqara.testing_perqara.other.adapter.GamesAdapter
import id.perqara.testing_perqara.other.base.BaseFragment
import id.perqara.testing_perqara.ui.games_detail.GamesDetailActivity

class FavoriteFragment : BaseFragment(){
    private lateinit var main: MainActivity
    private lateinit var binding: FragmentFavoriteBinding
    private var dbHandler: DBHandler? = null
    private lateinit var gamesAdapter: GamesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_favorite, container, false)
        initView()
        return binding.root
    }

    @SuppressLint("ResourceAsColor", "RestrictedApi")
    private fun initView(){
        gamesAdapter = GamesAdapter(
            mutableListOf(),
            requireContext(),
            ::onItemGamesClicked,
        )
        dbHandler = DBHandler(context)
        loadGamesRecyclerData(dbHandler!!.readFavorite())

        binding.toolbar.toolbarTitle.text = "Favorite Games"

        binding.pullToRefresh.setOnRefreshListener {
            reloadPageData()
            binding.pullToRefresh.isRefreshing = false
        }

        binding.recyclerViewGames.adapter = gamesAdapter
        binding.recyclerViewGames.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.pullToRefresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            reloadPageData()
            binding.pullToRefresh.isRefreshing = false
        })
    }

    private fun loadGamesRecyclerData(itemList: List<GamesModel>){
        gamesAdapter.setItemList(itemList)
        binding.recyclerViewGames.scrollToPosition(0)
    }

    private fun reloadPageData() {
        dbHandler = DBHandler(context)
        loadGamesRecyclerData(dbHandler!!.readFavorite())
    }

    private fun onItemGamesClicked(item: GamesModel) {
        val intent = Intent(activity, GamesDetailActivity::class.java)
        intent.putExtra("games_id", item.id)
        startActivity(intent)
    }
}