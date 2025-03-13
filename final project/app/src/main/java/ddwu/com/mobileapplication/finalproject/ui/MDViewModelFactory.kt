package ddwu.com.mobileapplication.finalproject.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ddwu.com.mobileapplication.finalproject.data.MDRepository

class MDViewModelFactory (private val mdRepository: MDRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MDViewModel::class.java)) {
            return MDViewModel(mdRepository) as T
        }
        return IllegalArgumentException("Unknown ViewModel class") as T
    }
}