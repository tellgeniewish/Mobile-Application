package ddwu.com.mobileapp.week04.roomexam01.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ddwu.com.mobileapp.week04.roomexam01.data.FoodRepository

// FoodViewModel이 액티비티의 멤버 변수로 선언되면 안 되기 때문에
class FoodViewModelFactory(private val foodRepository: FoodRepository): ViewModelProvider.Factory { // ViewModelProvider.Factory를 상속받아야 함
    // ViewModel 객체를 생성하는 함수를 재정의
    override fun <T : ViewModel> create(modelClass: Class<T>): T { // ViewModelProvider.Factory을 상속받으면 오류 사라짐
        // 생성하려는 클래스가 FoodViewModel 일 경우 객체 생성
        if (modelClass.isAssignableFrom(FoodViewModel::class.java)) { // FoodViewModel::class.java 내가 사용할 클래스
            return FoodViewModel(foodRepository) as T
        }
        return IllegalArgumentException("Unknown ViewModel class") as T
    }
}