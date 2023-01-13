package id.perqara.testing_perqara.other.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import id.perqara.testing_perqara.R
import id.perqara.testing_perqara.other.navigational_interface.IOnBackPressed

abstract class BaseFragment<VB : ViewBinding> : Fragment(), IOnBackPressed {
    private var _binding: ViewBinding? = null
    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB
    var TAG = ""

    @Suppress("UNCHECKED_CAST")
    protected val binding: VB get() = requireNotNull(_binding) as VB
    private var backstackCount = -1

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
        setupView(binding)
        setupChildFragmentPopListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
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
}