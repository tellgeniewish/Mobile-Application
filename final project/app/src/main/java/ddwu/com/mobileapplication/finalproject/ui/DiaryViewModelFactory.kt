package ddwu.com.mobileapplication.finalproject.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ddwu.com.mobileapplication.finalproject.data.DiaryRepository

class DiaryViewModelFactory(private val diaryRepository: DiaryRepository): ViewModelProvider.Factory { // ViewModelProvider.Factory를 상속받아야 함
    // ViewModel 객체를 생성하는 함수를 재정의
    override fun <T : ViewModel> create(modelClass: Class<T>): T { // ViewModelProvider.Factory을 상속받으면 오류 사라짐
        // 생성하려는 클래스가 FoodViewModel 일 경우 객체 생성
        if (modelClass.isAssignableFrom(DiaryViewModel::class.java)) {
            return DiaryViewModel(diaryRepository) as T
        }
        return IllegalArgumentException("Unknown ViewModel class") as T
    }
}