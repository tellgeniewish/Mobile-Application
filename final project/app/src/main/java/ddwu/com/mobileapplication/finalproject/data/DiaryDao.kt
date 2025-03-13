package ddwu.com.mobileapplication.finalproject.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DiaryDao {
    // 새로운 일기 삽입
    @Insert
    suspend fun insertDiary(diary: Diary)

    // 기존 일기 수정
    @Update
    suspend fun updateDiary(diary: Diary)

    // 일기 삭제
    @Delete
    suspend fun deleteDiary(diary: Diary)

    // 모든 일기 조회
    @Query("SELECT * FROM diary_table")
    fun getAllDiarys(): Flow<List<Diary>>

    // 특정 일기 조회 (id로 조회)
//    @Query("SELECT * FROM diary_table WHERE _id = :id")
//    suspend fun getDiaryById(id: Int): Diary?
//
//    // 특정 이름의 일기 조회 (이름으로 조회)
//    @Query("SELECT * FROM diary_table WHERE diary = :name")
//    suspend fun getDiaryByName(name: String): Diary?
}