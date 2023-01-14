package id.perqara.testing_perqara.ui.home

import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import dagger.hilt.android.AndroidEntryPoint
import id.perqara.testing_perqara.R
import id.perqara.testing_perqara.data.model.GamesModel
import id.perqara.testing_perqara.databinding.FragmentHomeBinding
import id.perqara.testing_perqara.other.adapter.GamesAdapter
import id.perqara.testing_perqara.other.base.BaseFragment
import id.perqara.testing_perqara.ui.games_detail.GamesDetailFragment
import kotlinx.coroutines.launch

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
            context!!,
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
        showBottomNavigation()
    }

    override fun setupView(binding: FragmentHomeBinding) {
        super.setupView(binding)

        binding.toolbar.toolbarTitle.text = "Games For You"

        binding.recyclerViewGames.adapter = gamesAdapter
        binding.recyclerViewGames.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.recyclerViewGames.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisible = layoutManager.findLastVisibleItemPosition() + 1
                if (totalItemCount - lastVisible <= 3 && homeViewModel.gamesNext != "") {
                    homeViewModel.gamesCurrentPage += 1
                    lifecycleScope.launch {
                        homeViewModel.getGamesList(homeViewModel.gamesCurrentPage, homeViewModel.gamesSearch)
                    }
                }
            }
        })

        binding.imgSearch.setOnClickListener(View.OnClickListener {
            binding.txtInputSearch.requestFocus()
            if (activity != null) {
                val imm = activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
            }
        })

        binding.txtInputSearch.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                lifecycleScope.launch {
                    homeViewModel.resetGamesPage()
                    homeViewModel.getGamesList(1, binding.txtInputSearch.text.toString())
                }
                return@OnKeyListener true
            }
            false
        })

        binding.pullToRefresh.setOnRefreshListener(OnRefreshListener {
            binding.txtInputSearch.text.clear()
            lifecycleScope.launch {
                homeViewModel.resetGamesPage()
                homeViewModel.getGamesList(1, "")
            }
            binding.pullToRefresh.isRefreshing = false
        })
    }

    private fun observeState(liveData: MutableLiveData<HomeState>) {
        liveData.observe(viewLifecycleOwner) {
            when (it) {
                is HomeState.LoadGames -> {
                    loadGamesRecyclerData(it.data)
                }
                is HomeState.MinorError -> {
                    showAlertDialog(it.message) {

                    }
                }
                is HomeState.NetworkError -> {
                    networkView.setOnRetryListener { _ ->
                        networkView.goneView()
                        reloadPageData()
                    }
                }
            }
        }
    }

    private fun loadGamesRecyclerData(itemList: List<GamesModel>){
        if (homeViewModel.gamesCurrentPage <= 1) {
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
//                    reloadPageData()
                }
                backstackOldCount = requireActivity().supportFragmentManager.backStackEntryCount
            } catch (e: Exception) {

            }
        }
    }

    private fun reloadPageData() {
        lifecycleScope.launch {
            homeViewModel.resetGamesPage()
            homeViewModel.getGamesList(homeViewModel.gamesCurrentPage, homeViewModel.gamesSearch)
        }
    }

    override fun onFragmentReappear() {
        super.onFragmentReappear()
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