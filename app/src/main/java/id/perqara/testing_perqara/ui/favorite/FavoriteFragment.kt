package id.perqara.testing_perqara.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dagger.hilt.android.AndroidEntryPoint
import id.perqara.testing_perqara.DBHandler
import id.perqara.testing_perqara.R
import id.perqara.testing_perqara.data.model.GamesModel
import id.perqara.testing_perqara.databinding.FragmentFavoriteBinding
import id.perqara.testing_perqara.other.adapter.GamesAdapter
import id.perqara.testing_perqara.other.base.BaseFragment
import id.perqara.testing_perqara.ui.games_detail.GamesDetailFragment

@AndroidEntryPoint
class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>(){
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFavoriteBinding =
        FragmentFavoriteBinding::inflate
    private val favoriteViewModel by viewModels<FavoriteViewModel>()
    private var backstackOldCount = 0
    private var dbHandler: DBHandler? = null
    private lateinit var gamesAdapter: GamesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gamesAdapter = GamesAdapter(
            mutableListOf(),
            ::onItemGamesClicked,
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbHandler = DBHandler(context)
        loadGamesRecyclerData(dbHandler!!.readFavorite())
        observeState(favoriteViewModel.stateLiveData)
        observeEvent(favoriteViewModel.eventLiveData)
        setupChildFragmentPopListener()
        showBottomNavigation()
    }

    override fun setupView(binding: FragmentFavoriteBinding) {
        super.setupView(binding)

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

    private fun observeState(liveData: MutableLiveData<FavoriteState>) {
        liveData.observe(viewLifecycleOwner) {
            when (it) {
                is FavoriteState.LoadGames -> {
                    loadGamesRecyclerData(it.data)
                }
                is FavoriteState.MinorError -> {
                    showAlertDialog(it.message) {

                    }
                }
                is FavoriteState.NetworkError -> {
                    networkView.setOnRetryListener { _ ->
                        networkView.goneView()
                        reloadPageData()
                    }
                }
            }
        }
    }

    private fun loadGamesRecyclerData(itemList: List<GamesModel>){
        if (favoriteViewModel.gamesCurrentPage <= 1) {
            gamesAdapter.setItemList(itemList)
            binding.recyclerViewGames.scrollToPosition(0)
        } else {
            gamesAdapter.addItemList(itemList)
        }
    }

    private fun setupChildFragmentPopListener() {
        requireActivity().supportFragmentManager.addOnBackStackChangedListener {
            try {
                if (requireActivity().supportFragmentManager.backStackEntryCount < backstackOldCount) {
                    reloadPageData()
                }
                backstackOldCount = requireActivity().supportFragmentManager.backStackEntryCount
            } catch (e: Exception) {

            }
        }
    }

    private fun reloadPageData() {
        dbHandler = DBHandler(context)
        loadGamesRecyclerData(dbHandler!!.readFavorite())
    }

    override fun onFragmentReappear() {
        super.onFragmentReappear()
        reloadPageData()
        showBottomNavigation()
    }

    private fun onItemGamesClicked(item: GamesModel) {
        val fragment = GamesDetailFragment()
        fragment.arguments = bundleOf(
            Pair("games_id", item.id)
        )
        val fragmentTransaction = parentFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragment_container, fragment)
        fragmentTransaction.addToBackStack("GamesDetailFragment")
        fragmentTransaction.commit()
    }
}