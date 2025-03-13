package ddwu.com.mobileapplication.finalproject.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ddwu.com.mobileapplication.finalproject.data.network.NVLoc
import ddwu.com.mobileapplication.finalproject.data.NVRepository
import kotlinx.coroutines.launch

class NVViewModel (val nvRepository: NVRepository) : ViewModel() {

    private val _nvLocs = MutableLiveData<List<NVLoc>>()
    val nvLocs : LiveData<List<NVLoc>> = _nvLocs

    fun getBooks(query: String, id: String, secret: String) = viewModelScope.launch {
        _nvLocs.value = nvRepository.getNVLocs(query, id, secret)     // INaverBookSearch에 coroutine 적용할 것
    }
}