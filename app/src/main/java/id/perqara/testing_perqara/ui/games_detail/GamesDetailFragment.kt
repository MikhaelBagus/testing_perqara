package id.perqara.testing_perqara.ui.games_detail

import android.content.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
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
    }

    override fun getDataFromArgument(argument: Bundle) {
        gamesDetailViewModel.gamesId = argument.getString("games_id", null)
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
            }
        }
    }

    private fun loadGamesDetail(item: GamesModel) {

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