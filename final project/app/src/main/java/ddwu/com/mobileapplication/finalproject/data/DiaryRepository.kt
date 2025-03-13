package ddwu.com.mobileapplication.finalproject.data

import kotlinx.coroutines.flow.Flow

class DiaryRepository(private val diaryDao: DiaryDao) {
    val allDiarys : Flow<List<Diary>> = diaryDao.getAllDiarys() // flow: 지속적으로 데이터 관찰해서 변화 시 바로 반영

//    suspend fun getFoodByCountry(country: String) : List<Food> {
//        val foods = foodDao.getFoodsByCountry(country)
//        return foods
//    }

    suspend fun addDiary(diary: Diary) {
        diaryDao.insertDiary(diary)
    }

    suspend fun modifyDiary(diary: Diary) {
        diaryDao.updateDiary(diary)
    }

    suspend fun removeDiary(diary: Diary) {
        diaryDao.deleteDiary(diary)
    }
}