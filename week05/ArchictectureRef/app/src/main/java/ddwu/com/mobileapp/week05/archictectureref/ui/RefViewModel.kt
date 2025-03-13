package ddwu.com.mobileapp.week05.archictectureref.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import ddwu.com.mobileapp.week05.archictectureref.data.RefRepository
import ddwu.com.mobileapp.week05.archictectureref.data.database.RefEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RefViewModel (val refRepository: RefRepository) : ViewModel() {
    // Flow 를 사용하여 지속 관찰
    val allRefs : LiveData<List<RefEntity>> = refRepository.allRefs.asLiveData()

    // one-shot 결과를 확인하고자 할 때 사용
    private var _name = MutableLiveData<String>()
    val nameData: LiveData<String> = _name // LiveData는 한 번 생성하면 값이 안 바뀌고 고정됨
    // nameData는 변경 가능한 _name을 참조함(가리킴)
    // 뷰 모델에서 보관하고 싶은 값은 멤버변수로 선언하면 됨

    // viewModelScope 는 Dispatcher.Main 이므로 긴시간이 걸리는 IO 작업은 Dispatchers.IO 에서 작업
    fun findName(id: Int) = viewModelScope.launch { // CoroutineScope(Dispatchers.Main).launch과 비슷
        var result : String
        withContext(Dispatchers.IO) { // 이 부분만 Dispatchers.IO로 실행
            result = refRepository.getNameById(id)
        }
        _name.value = result
    }

    fun addRef(ref: RefEntity) = viewModelScope.launch {
        //refRepository.insertRef(ref) // 이렇게 쓰면 앱이 죽는다
        withContext(Dispatchers.IO) {
            refRepository.insertRef(ref)
        }
    }

}