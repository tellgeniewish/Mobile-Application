package ddwu.com.mobileapplication.finalproject.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ddwu.com.mobileapplication.finalproject.data.MDRepository
import ddwu.com.mobileapplication.finalproject.data.Medical
import kotlinx.coroutines.launch

class MDViewModel(private val mdRepository: MDRepository) : ViewModel() {
    private val _medical = MutableLiveData<List<Medical>>()
    val medicals : LiveData<List<Medical>> = _medical

    fun showMedical(instit_nm: String) = viewModelScope.launch {
        val TAG = "MDViewModel"
        var result : List<Medical>
        //withContext(Dispatchers.IO) {
            result = mdRepository.showMedical(instit_nm)
        //}
        Log.d(TAG, "MDViewModel result --> ${result}")
        _medical.value = result
    }
}