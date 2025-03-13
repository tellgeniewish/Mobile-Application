package ddwu.com.mobileapp.week04.roomexam01.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Insert
    suspend fun insertFood(food: Food)

    @Update
    suspend fun updateFood(food: Food)

    @Delete
    suspend fun deleteFood(food: Food)

    @Query("UPDATE food_table SET country = :country WHERE food = :newName")
    suspend fun updateFoodCountryByName(newName : String, country: String)

    @Query("DELETE FROM food_table WHERE food = :foodName")
    suspend fun deleteFoodByName(foodName: String)

    @Query("SELECT * FROM food_table")
    fun getAllFoods() : Flow<List<Food>>

    @Query("SELECT * FROM food_table WHERE country = :country") // *은 _id, food, country(모든 컬럼)이다
    suspend fun getFoodsByCountry(country: String) : List<Food> // suspend는 한 번만 수행 --> 원샷 쿼리
}