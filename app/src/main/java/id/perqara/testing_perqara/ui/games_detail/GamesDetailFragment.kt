package id.perqara.testing_perqara.ui.games_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import id.perqara.testing_perqara.data.model.GamesModel
import id.perqara.testing_perqara.databinding.FragmentGamesDetailBinding
import id.perqara.testing_perqara.other.base.BaseFragment
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GamesDetailFragment : BaseFragment<FragmentGamesDetailBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentGamesDetailBinding =
        FragmentGamesDetailBinding::inflate

    private val gamesDetailViewModel by viewModels<GamesDetailViewModel>()

    private var backstackOldCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (gamesDetailViewModel.gamesId != null) {
            lifecycleScope.launch {
                gamesDetailViewModel.getGamesDetail(gamesDetailViewModel.gamesId!!)
            }
        } else {
            showAlertDialog("Mohon maaf, telah terjadi kesalahan. Silahkan coba lagi nanti") {
                this.parentFragmentManager.popBackStack()
            }
        }
        observeState(gamesDetailViewModel.stateLiveData)
        observeEvent(gamesDetailViewModel.eventLiveData)
        setupChildFragmentPopListener()
        hideBottomNavigation()
    }

    override fun setupView(binding: FragmentGamesDetailBinding) {
        super.setupView(binding)
        binding.toolbar.layoutBackArrow.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.toolbar.toolbarTitle.text = "Detail"
        binding.txtDescription.settings.javaScriptEnabled = true

        binding.pullToRefresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            lifecycleScope.launch {
                gamesDetailViewModel.getGamesDetail(gamesDetailViewModel.gamesId!!)
            }
            binding.pullToRefresh.isRefreshing = false
        })

        binding.toolbar.layoutFavorite.setOnClickListener {

        }
    }

    override fun getDataFromArgument(argument: Bundle) {
        gamesDetailViewModel.gamesId = argument.getInt("games_id", 0)
    }

    private fun observeState(liveData: MutableLiveData<GamesDetailState>) {
        liveData.observe(viewLifecycleOwner) {
            when (it) {
                is GamesDetailState.LoadGamesDetail -> {
                    loadGamesDetail(it.data)
                }
                is GamesDetailState.MinorError -> {
                    showAlertDialog(it.message) {

                    }
                }
                is GamesDetailState.NetworkError -> {
                    networkView.setOnRetryListener { _ ->
                        networkView.goneView()
                        loadPageData(gamesDetailViewModel.gamesId)
                    }
                }
            }
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

        binding.progressBar.visibility = View.VISIBLE
        if (item.background_image != null && item.background_image != "") {
            Picasso.get().load(item.background_image).into(binding.imgBackground, object : Callback {
                override fun onSuccess() {
                    binding.progressBar.visibility = View.GONE
                }

                override fun onError(e: Exception) {
                    binding.progressBar.visibility = View.GONE
                }
            })
        }
        else{
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setupChildFragmentPopListener() {
        requireActivity().supportFragmentManager.addOnBackStackChangedListener {
            try {
                if (requireActivity().supportFragmentManager.backStackEntryCount < backstackOldCount) {
                    if (gamesDetailViewModel.gamesId != null) {
                        lifecycleScope.launch {
                            gamesDetailViewModel.getGamesDetail(gamesDetailViewModel.gamesId!!)
                        }
                    }
                }
                backstackOldCount = requireActivity().supportFragmentManager.backStackEntryCount
            } catch (e: Exception) {

            }
        }
    }

    override fun onFragmentReappear() {
        super.onFragmentReappear()
        loadPageData(gamesDetailViewModel.gamesId)
        hideBottomNavigation()
    }

    private fun loadPageData(gamesId: Int?){
        if (gamesId != null) {
            lifecycleScope.launch {
                gamesDetailViewModel.getGamesDetail(gamesId)
            }
        } else {
            showAlertDialog("Mohon maaf, telah terjadi kesalahan. Silahkan coba lagi nanti") {
                this.parentFragmentManager.popBackStack()
            }
        }
    }
}