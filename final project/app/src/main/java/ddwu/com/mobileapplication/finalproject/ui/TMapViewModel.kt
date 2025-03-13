//package ddwu.com.mobileapplication.finalproject.ui
//
//import android.util.Log
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import ddwu.com.mobileapplication.finalproject.data.Poi
//import ddwu.com.mobileapplication.finalproject.data.TMapRepository
//import kotlinx.coroutines.launch
//
//class TMapViewModel(private val tmRepository: TMapRepository) : ViewModel() {
//    private val _tmap = MutableLiveData<List<Poi>>()
//    val tmaps : LiveData<List<Poi>> = _tmap
//
//    fun showTMap(instit_nm: String) = viewModelScope.launch {
//        val TAG = "TMapViewModel"
//        var result : List<Poi>
//        //withContext(Dispatchers.IO) {
//        result = tmRepository.showTMap(instit_nm)
//        //}
//        Log.d(TAG, "TMapViewModel result --> ${result}")
//        _tmap.value = result
//    }
//}