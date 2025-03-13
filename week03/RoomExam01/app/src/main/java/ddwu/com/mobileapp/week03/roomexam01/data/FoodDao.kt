package ddwu.com.mobile.roomexam01.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao { // 함수들을 사용하는 클래스
    @Insert
    suspend fun insertFood(food: Food) // 편의 메소드를 쓸 때는 매개변수로 Entity가 들어와야 함
    // suspend(중단하다)는 원샷 쿼리 앞에 suspend 붙인다
    // 함수를 수행 중에 메인에서 동작이 있어서 호출이 오면 함수가 멈추고
    // 메인이 할 일이 끝나면 다시 돌아와서 할 일을 이어서 한다
    
    //@Update
    //suspend fun updateFood(food: Food) // _id(Primary Key)를 기준으로
    // 음식 이름 기준으로 나라 이름 변경
    @Query("UPDATE food_table SET country = :newCountry WHERE food = :food_name")
    suspend fun updateFood(food_name: String, newCountry: String)

//    @Delete
//    suspend fun deleteFood(food: Food)
    // 음식 이름 기준으로 지우기
    @Query("DELETE FROM food_table WHERE food = :food_name")
    suspend fun deleteFood(food_name: String)

    @Query("SELECT * FROM food_table")
//    fun getAllFoods() : List<Food>
    fun getAllFoods() : Flow<List<Food>> // 코루틴의 Flow를 ALT+ENTER해야 한다
    // 계속 관찰할 결과값(List<Food>)을 Flow로 감싸준다

    @Query("SELECT * FROM food_table WHERE country = :country")
    suspend fun getFoodsByCountry(country: String) : List<Food>
}