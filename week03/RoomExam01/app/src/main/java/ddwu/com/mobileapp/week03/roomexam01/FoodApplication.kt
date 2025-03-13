package ddwu.com.mobileapp.week03.roomexam01

import android.app.Application
import ddwu.com.mobile.roomexam01.data.FoodDatabase
import ddwu.com.mobileapp.week03.roomexam01.data.FoodRepository

class FoodApplication: Application() { // Application()을 상속받아야 함
    // 내가 만든 어플리케이션 사용하고 싶으면
    // AndroidManifest의 <application> 안에
    // android:name=".FoodApplication"로 지정
    val foodRepo by lazy {
        val database = FoodDatabase.getDatabase(this)
        FoodRepository(database.foodDao())
    }
}