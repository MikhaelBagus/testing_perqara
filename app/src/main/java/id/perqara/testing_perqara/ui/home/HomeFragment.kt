package id.perqara.testing_perqara.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.perqara.testing_perqara.data.model.GamesModel
import id.perqara.testing_perqara.databinding.FragmentHomeBinding
import id.perqara.testing_perqara.other.adapter.GamesAdapter

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(){
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeBinding =
        FragmentHomeBinding::inflate
    private val homeViewModel by viewModels<HomeViewModel>()
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
            homeViewModel.getGamesList(1,"")
        }
        observeState(homeViewModel.stateLiveData)
        observeEvent(homeViewModel.eventLiveData)
        setupChildFragmentPopListener()
    }

    override fun setupView(binding: FragmentHomeBinding) {
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
                if (totalItemCount - lastVisible <= 3 && homeViewModel.gamesCurrentPage < homeViewModel.gamesTotalPage) {
                    homeViewModel.gamesCurrentPage += 1
                    lifecycleScope.launch {
                        homeViewModel.getGamesList(homeViewModel.gamesCurrentPage)
                    }
                }
            }
        })
    }

    private fun observeState(liveData: MutableLiveData<HomeState>) {
        liveData.observe(viewLifecycleOwner) {
            when (it) {
                is HomeState.LoadGames -> {
                    loadGamesRecyclerData(it.currentPage, it.maxPage, it.data)
                }
                is HomeState.MinorError -> {
                    showAlertDialog(it.message) {

                    }
                }
            }
        }
    }

    private fun loadGamesRecyclerData(currentPage: Int, totalPage: Int, itemList: List<GamesModel>){
        homeViewModel.gamesTotalPage = totalPage
        homeViewModel.gamesCurrentPage = currentPage
        if (homeViewModel.gamesCurrentPage <= 1) {
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
            homeViewModel.resetGamesPage()
            homeViewModel.getGamesList(homeViewModel.gamesCurrentPage)
        }
    }

    override fun onFragmentReappear() {
        super.onFragmentReappear()
        reloadPageData()
    }
}