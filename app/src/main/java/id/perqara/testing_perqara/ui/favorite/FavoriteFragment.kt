package id.perqara.testing_perqara.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.perqara.testing_perqara.data.model.GamesModel
import id.perqara.testing_perqara.databinding.FragmentFavoriteBinding
import id.perqara.testing_perqara.databinding.FragmentHomeBinding
import id.perqara.testing_perqara.other.adapter.GamesAdapter
import id.perqara.testing_perqara.ui.home.HomeState
import id.perqara.testing_perqara.ui.home.HomeViewModel

@AndroidEntryPoint
class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>(){
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFavoriteBinding =
        FragmentFavoriteBinding::inflate
    private val favoriteViewModel by viewModels<FavoriteViewModel>()
    private var backstackOldCount = 0
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
        lifecycleScope.launch {
            favoriteViewModel.getGamesList(1,"")
        }
        observeState(favoriteViewModel.stateLiveData)
        observeEvent(favoriteViewModel.eventLiveData)
        setupChildFragmentPopListener()
    }

    override fun setupView(binding: FragmentFavoriteBinding) {
        super.setupView(binding)

        binding.pullToRefresh.setOnRefreshListener {
            reloadPageData()
            binding.pullToRefresh.isRefreshing = false
        }

        binding.recyclerViewGames.adapter = gamesAdapter
        binding.recyclerViewGames.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.recyclerViewGames.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisible = layoutManager.findLastVisibleItemPosition() + 1
                if (totalItemCount - lastVisible <= 3 && favoriteViewModel.gamesCurrentPage < favoriteViewModel.gamesTotalPage) {
                    favoriteViewModel.gamesCurrentPage += 1
                    lifecycleScope.launch {
                        favoriteViewModel.getGamesList(favoriteViewModel.gamesCurrentPage)
                    }
                }
            }
        })
    }

    private fun observeState(liveData: MutableLiveData<FavoriteState>) {
        liveData.observe(viewLifecycleOwner) {
            when (it) {
                is FavoriteState.LoadGames -> {
                    loadGamesRecyclerData(it.currentPage, it.maxPage, it.data)
                }
                is FavoriteState.MinorError -> {
                    showAlertDialog(it.message) {

                    }
                }
            }
        }
    }

    private fun loadGamesRecyclerData(currentPage: Int, totalPage: Int, itemList: List<GamesModel>){
        favoriteViewModel.gamesTotalPage = totalPage
        favoriteViewModel.gamesCurrentPage = currentPage
        if (favoriteViewModel.gamesCurrentPage <= 1) {
            gamesAdapter.setItemList(itemList)
            binding.recyclerViewGames.scrollToPosition(0)
        } else {
            gamesAdapter.addItemList(itemList)
        }
    }

    private fun onItemGamesClicked(item: GamesModel) {

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
        lifecycleScope.launch {
            favoriteViewModel.resetGamesPage()
            favoriteViewModel.getGamesList(favoriteViewModel.gamesCurrentPage)
        }
    }

    override fun onFragmentReappear() {
        super.onFragmentReappear()
        reloadPageData()
    }
}