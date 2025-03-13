//package ddwu.com.mobileapplication.finalproject.ui
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import ddwu.com.mobileapplication.finalproject.data.TMapRepository
//
//class TMapViewModelFactory (private val tmRepository: TMapRepository) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(TMapViewModel::class.java)) {
//            return TMapViewModel(tmRepository) as T
//        }
//        return IllegalArgumentException("Unknown ViewModel class") as T
//    }
//}