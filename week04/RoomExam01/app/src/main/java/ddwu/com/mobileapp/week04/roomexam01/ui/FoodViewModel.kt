package ddwu.com.mobileapp.week04.roomexam01.ui


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import ddwu.com.mobileapp.week04.roomexam01.data.Food

import ddwu.com.mobileapp.week04.roomexam01.data.FoodRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FoodViewModel (val foodRepo: FoodRepository): ViewModel() { // ViewModel()을 상속받음
    var allFoods: LiveData<List<Food>> = foodRepo.allFoods.asLiveData()
    // Flow데이터(생명주기와 상관없이 살아있음)를 라이브 데이터로 바꿈
    // 라이브 데이터로 변화 -> 화면에 보일 때만 살아있음 -> 생명주기를 인식함

    fun getFoodByCountry(country: String): Deferred<List<Food>> {
        val defferedFoods = viewModelScope.async {
            foodRepo.getFoodByCountry(country)
        }
        return defferedFoods
    }

    // viewModel안에서 코루틴 관련 코드를 사용할 때 viewModelScope 사용
    fun addFood(food: Food) = viewModelScope.launch { // viewModelScope = viewModel전용 코루틴Scope
        foodRepo.addFood(food)
    }

    fun modifyFood(food: Food) = viewModelScope.launch {
        foodRepo.modifyFood(food)
    }

    fun removeFood(food: Food) = viewModelScope.launch {
        foodRepo.removeFood(food)
    }

    fun modifyFoodCountryByFood(food: Food) = viewModelScope.launch {
        foodRepo.modifyFoodCountryByFood(food)
    }

    fun removeFoodByName(food: Food) = viewModelScope.launch {
        foodRepo.removeFoodByName(food)
    }
}