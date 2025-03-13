package ddwu.com.mobileapp.week02.fooddbexam_room.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao // room으로 만든 테이블을 저장 ㄱㄴ
interface FoodDao { // 인터페이스는 기능 구현 안 함
    // query 없는 편의 메소드
    @Insert fun insertFood(vararg food: Food) // food는 엔터티
    // 변수를 테이블에 저장함
    // vararg는 내부적으로 배열로 처리됨 // insertFood(food1), insertFood(food1, ...)

    @Update
    fun updateFood(food: Food)
    @Delete
    fun deleteFood(food: Food)

    @Query("SELECT * FROM food_table")
    fun getAllFoods(): List<Food> // SELECT하면 객체가 여러 개 튀어나올 수 있기 때문에 List라고 타입을 지정해줘야 함

    @Query("SELECT * FROM food_table WHERE country = :country")
    fun showFoodByCountry(country: String): List<Food>
}