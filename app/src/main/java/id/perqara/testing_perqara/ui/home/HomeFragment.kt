package id.perqara.testing_perqara.ui.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import id.perqara.testing_perqara.MainActivity
import id.perqara.testing_perqara.R
import id.perqara.testing_perqara.data.model.GamesModel
import id.perqara.testing_perqara.databinding.FragmentHomeBinding
import id.perqara.testing_perqara.other.adapter.GamesAdapter
import id.perqara.testing_perqara.other.base.BaseFragment
import id.perqara.testing_perqara.ui.games_detail.GamesDetailActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment(){
    private lateinit var main: MainActivity
    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModel()
    private lateinit var gamesAdapter: GamesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home, container, false)
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
        homeViewModel.getGamesList(1,"")
        vmHandle()

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
                    homeViewModel.getGamesList(homeViewModel.gamesCurrentPage, homeViewModel.gamesSearch)
                }
            }
        })

        binding.imgSearch.setOnClickListener(View.OnClickListener {
            binding.txtInputSearch.requestFocus()
            if (activity != null) {
                val imm = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
            }
        })

        binding.txtInputSearch.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                homeViewModel.resetGamesPage()
                homeViewModel.getGamesList(1, binding.txtInputSearch.text.toString())
                return@OnKeyListener true
            }
            false
        })

        binding.pullToRefresh.setOnRefreshListener(OnRefreshListener {
            binding.txtInputSearch.text.clear()
            homeViewModel.resetGamesPage()
            homeViewModel.getGamesList(1, "")
            binding.pullToRefresh.isRefreshing = false
        })
    }

    fun setMain(mainActivity: MainActivity) {
        main = mainActivity
    }

    private fun vmHandle() {
        homeViewModel.dataWrapperListModel.observe(requireActivity()) { datas ->
            loadGamesRecyclerData(datas.results!!)
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

    private fun onItemGamesClicked(item: GamesModel) {
        val intent = Intent(activity, GamesDetailActivity::class.java)
        intent.putExtra("games_id", item.id)
        startActivity(intent)
    }
}