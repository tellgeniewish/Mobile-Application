package ddwu.com.mobileapp.week04.wordexam.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ddwu.com.mobileapp.week04.wordexam.data.WordRepository

class WordViewModelFactory (private val wordRepository: WordRepository): ViewModelProvider.Factory { // ViewModelProvider.Factory를 상속받아야 함
    // ViewModel 객체를 생성하는 함수를 재정의
    override fun <T : ViewModel> create(modelClass: Class<T>): T { // ViewModelProvider.Factory을 상속받으면 오류 사라짐
        // 생성하려는 클래스가 FoodViewModel 일 경우 객체 생성
        if (modelClass.isAssignableFrom(WordViewModel::class.java)) {
            return WordViewModel(wordRepository) as T
        }
        return IllegalArgumentException("Unknown ViewModel class") as T
    }
}