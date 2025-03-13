package ddwu.com.mobileapp.week02.fooddbexam_room

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.room.Room
import ddwu.com.mobileapp.week02.fooddbexam_room.data.Food
import ddwu.com.mobileapp.week02.fooddbexam_room.data.FoodDao
import ddwu.com.mobileapp.week02.fooddbexam_room.data.FoodDatabase
import ddwu.com.mobileapp.week02.fooddbexam_room.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    val foodDatabase by lazy {
        FoodDatabase.getDatabase(this)
    }

    val foodDao by lazy {
        foodDatabase.foodDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContentView(binding.root)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }


        // insert test
        // FoodDatabase는 abstract라서 객체 만들 수 없음 -> ROOM을 사용해서 만들기
        // 여러 번 사용하면 부하가 많이 걸릴 수도
//        val foodDatabase: FoodDatabase = Room.databaseBuilder(applicationContext,
//                                                                FoodDatabase::class.java,
//                                                            "food_db")
//                                                                .build() // FoodDatabase는 abstract라서 이렇게 객체 생성함

//        val foodDatabase: FoodDatabase = FoodDatabase.getDatabase(this) // 싱글톤 패턴 적용

//        val foodDao: FoodDao = foodDatabase.foodDao()

        // 하나의 프로세스 안에서 여러 개의 작업 수행 ㄱㄴ하게 하는 흐름: Thread
//        Thread { // 안 쓰면 바로 죽음
//            foodDao.insertFood(Food(0, "순두부찌개", "대한민국"))
            //메인 쓰레드(사용자 클릭, 입출력 등을 검사하고 체크하는 UI를 담당하는 쓰레드)는 하나는 꼭 있다
            //메인 쓰레드에서 시간 많이 걸리는 작업하면 안 됨
//        }.start()


//        show all foods

    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.btnShowFood -> {
//                show food by country
                Thread { // 안 쓰면 바로 죽음
                    val foods: List<Food> = foodDao.showFoodByCountry("대한민국")

                    for (food in foods) {
                        Log.d("MainActivity", food.toString())
                    }
                }.start()
            }
            R.id.btnAdd -> {
//                add food
                Thread { // 안 쓰면 바로 죽음
                    foodDao.insertFood(Food(0, "순두부찌개", "대한민국"))
                }.start()
            }
            R.id.btnModify -> {
//                modify food
                Thread { // 안 쓰면 바로 죽음
                    foodDao.updateFood(Food(1, "김치찌개", "korea"))
                }.start()
            }
            R.id.btnRemove -> {
//                remove food
                Thread { // 안 쓰면 바로 죽음
                    foodDao.deleteFood(Food(2, "순두부찌개", "대한민국"))
                }.start()
            }
        }
    }
}