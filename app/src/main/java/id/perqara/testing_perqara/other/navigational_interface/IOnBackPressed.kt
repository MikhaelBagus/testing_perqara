package id.perqara.testing_perqara.other.navigational_interface

interface IOnBackPressed {
    /**
     * If back pressed is already handled on implementation, return true.
     * Otherwise, return false and handle back pressed on caller.*/
    fun handleOnBackPressedOnImpl(): Boolean
}