package ddwu.com.mobileapp.week04.wordexam.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import ddwu.com.mobileapp.week04.wordexam.data.Word
import ddwu.com.mobileapp.week04.wordexam.data.WordRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class WordViewModel (val wordRepo: WordRepository): ViewModel() { // ViewModel()을 상속받음
    var allWords: LiveData<List<Word>> = wordRepo.allWords.asLiveData() // flow는 생명주기와 상관없이 살아있음
    // 라이브 데이터로 변화 -> 화면에 보일 때만 살아있음 -> 생명주기를 인식함

    // viewModel안에서 코루틴 관련 코드를 사용할 때 viewModelScope 사용
    fun addWord(word: Word) = viewModelScope.launch { // viewModelScope = viewModel전용 코루틴Scope
        wordRepo.addWord(word)
    }
    fun removeWord(word: Word) = viewModelScope.launch {
        wordRepo.removeWord(word)
    }

    fun modifyWord(word: Word) = viewModelScope.launch {
        wordRepo.modifyWord(word)
    }
    fun showWordMeaning(word: String): Deferred<String> {
        val defferedWords = viewModelScope.async {
            wordRepo.showWordMeaning(word)
        }
        return defferedWords
    }
}