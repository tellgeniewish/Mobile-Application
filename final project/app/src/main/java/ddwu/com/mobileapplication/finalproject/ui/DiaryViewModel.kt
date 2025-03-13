package ddwu.com.mobileapplication.finalproject.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import ddwu.com.mobileapplication.finalproject.data.Diary
import ddwu.com.mobileapplication.finalproject.data.DiaryRepository
import kotlinx.coroutines.launch

class DiaryViewModel(val diaryRepo: DiaryRepository): ViewModel() {
    // ViewModel()을 상속받음
    var allDiarys: LiveData<List<Diary>> = diaryRepo.allDiarys.asLiveData() // flow는 생명주기와 상관없이 살아있음
    // 라이브 데이터로 변화 -> 화면에 보일 때만 살아있음 -> 생명주기를 인식함

//    fun getFoodByCountry(country: String): Deferred<List<Diary>> {
//        val defferedFoods = viewModelScope.async {
//            diaryRepo.getFoodByCountry(country)
//        }
//        return defferedFoods
//    }

    // viewModel안에서 코루틴 관련 코드를 사용할 때 viewModelScope 사용
    fun addDiary(diary: Diary) = viewModelScope.launch { // viewModelScope = viewModel전용 코루틴Scope
        diaryRepo.addDiary(diary)
    }

    fun modifyFood(diary: Diary) = viewModelScope.launch {
        diaryRepo.modifyDiary(diary)
    }

    fun removeDiary(diary: Diary) = viewModelScope.launch {
        diaryRepo.removeDiary(diary)
    }
}