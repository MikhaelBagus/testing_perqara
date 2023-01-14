package id.perqara.testing_perqara.other.base

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import id.perqara.testing_perqara.R
import id.perqara.testing_perqara.other.custom_view.LoadingDialog
import id.perqara.testing_perqara.other.custom_view.NetworkView
import id.perqara.testing_perqara.other.navigational_interface.IOnBackPressed
import id.perqara.testing_perqara.other.wrapper.EventWrapper

abstract class BaseFragment<VB : ViewBinding> : Fragment(), IOnBackPressed {
    private var _binding: ViewBinding? = null
    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB
    var TAG = ""

    @Suppress("UNCHECKED_CAST")
    private val loadingDialog by lazy { LoadingDialog(requireContext()) }
    val networkView by lazy { NetworkView(requireContext()) }
    protected val binding: VB get() = requireNotNull(_binding) as VB
    private var backstackCount = -1
    private var persistLoadingMultiple = false

    init {
        TAG = this.javaClass.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { getDataFromArgument(it) }
        backstackCount = requireActivity().supportFragmentManager.backStackEntryCount
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater, container, false)
        return requireNotNull(_binding).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (networkView.parent != null) {
            (networkView.parent as ViewGroup).removeView(networkView) // <- fix
        } else {
            networkView.removeView()
        }

        (view as ViewGroup).addView(networkView)
        setupView(binding)
        setupChildFragmentPopListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStop() {
        super.onStop()
        closeLoadingDialog()
    }

    override fun onDestroy() {
        super.onDestroy()
        closeLoadingDialog()
        networkView.removeView()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun handleOnBackPressedOnImpl(): Boolean {
        return false
    }

    private fun setupChildFragmentPopListener() {
        requireActivity().supportFragmentManager.addOnBackStackChangedListener {
            try {
                if (requireActivity().supportFragmentManager.backStackEntryCount == backstackCount) {
                    onFragmentReappear()
                }
            } catch (e: Exception) {

            }
        }
    }

    open fun setupView(binding: VB) {

    }

    open fun hideBottomNavigation() {
        val navBar: BottomNavigationView? = requireActivity().findViewById(R.id.navigation)
        navBar?.visibility = View.GONE
    }

    open fun showBottomNavigation() {
        val navBar: BottomNavigationView? = requireActivity().findViewById(R.id.navigation)
        navBar?.visibility = View.VISIBLE
    }

    open fun getDataFromArgument(argument: Bundle) {}

    open fun onFragmentReappear() {}

    open fun showAlertDialog(
        message: String,
        onPositiveButtonClicked: () -> Unit
    ) {
        if(message.isBlank()) return
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setMessage(message)
        alertDialogBuilder.setPositiveButton(
            "OK"
        ) { dialogInterface: DialogInterface, i: Int ->
            onPositiveButtonClicked()
            dialogInterface.cancel()
        }

        alertDialogBuilder.show()
    }

    fun observeEvent(eventLiveData: MutableLiveData<EventWrapper>) {
        eventLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is EventWrapper.OnToast -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
                is EventWrapper.OnMultipleCallLoadingShow -> {
                    persistLoadingMultiple = true
                    showLoadingDialog()
                }
                is EventWrapper.OnMultipleCallLoadingDissapear -> {
                    persistLoadingMultiple = false
                    closeLoadingDialog()
                }
                is EventWrapper.OnLoadingShow -> {
                    if (persistLoadingMultiple) return@observe
                    showLoadingDialog()
                }
                is EventWrapper.OnLoadingDissapear -> {
                    if (persistLoadingMultiple) return@observe
                    closeLoadingDialog()
                }
                is EventWrapper.OnPageFinished -> {

                }
                is EventWrapper.OnNetworkError -> {
                    showNetworkPrompt()
                }
            }
        }
    }

    open fun showLoadingDialog() {
        loadingDialog.showDialog()
    }

    open fun closeLoadingDialog() {
        loadingDialog.dismissDialog()
    }

    open fun showNetworkPrompt() {
        networkView.visibleView()
    }

    open fun closeNetworkPrompt() {
        networkView.goneView()
    }
}