package ddwu.com.mobileapp.week03.roomexam01.data

import ddwu.com.mobile.roomexam01.data.Food
import ddwu.com.mobile.roomexam01.data.FoodDao
import kotlinx.coroutines.flow.Flow

class FoodRepository(private val foodDao: FoodDao) { // Dao를 접근하는 멤버변수를 추가(private 사용 이유: SSOT 나만 통해서 사용 ㄱㄴ!)
    val allFoods: Flow<List<Food>> = foodDao.getAllFoods()
    
    suspend fun addFood(food: Food) { // 실제 Dao와 분리
        foodDao.insertFood(food) // 간접적으로 사용할 수 있도록
    }
//    suspend fun modifyFood(food: Food) {
//        foodDao.updateFood(food)
//    }
    // 음식 이름 기준으로 나라 이름 변경
    suspend fun modifyFood(food_name: String, countryName: String) {
        foodDao.updateFood(food_name, countryName)
    }
//    suspend fun removeFood(food: Food) {
//        foodDao.deleteFood(food)
//    }
    // 음식 이름 기준으로 지우기
    suspend fun removeFood(food_name: String) {
        foodDao.deleteFood(food_name)
    }

    suspend fun showFoodsByCountry(country: String): List<Food> {
        return foodDao.getFoodsByCountry(country)
    }
}