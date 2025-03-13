package ddwu.com.mobileapp.week04.wordexam.data

import kotlinx.coroutines.flow.Flow

class WordRepository(private val wordDao: WordDao) {
    val allWords: Flow<List<Word>> = wordDao.showAllWords()

    suspend fun addWord(word: Word) {
        wordDao.insertWord(word)
    }
    suspend fun removeWord(word: Word) {
        wordDao.deleteWord(word)
    }
    suspend fun modifyWord(word: Word) {
        wordDao.updateWord(word)
    }

    // 단어(word)를 입력하여 의미(meaning) 반환
    suspend fun showWordMeaning(word: String) : String {
        val mean = wordDao.getWordMeaning(word)
        return mean
    }
}