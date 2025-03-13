package ddwu.com.mobileapp.week03.roomexam01

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ddwu.com.mobile.roomexam01.data.Food
import ddwu.com.mobile.roomexam01.data.FoodDatabase
import ddwu.com.mobileapp.week03.roomexam01.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"

    // view binding object
    val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

//    val foodDabase by lazy {
//        FoodDatabase.getDatabase(this)
//    }
//
//    val foodDao by lazy {
//        foodDabase.foodDao()
//    }
    val foodRepo by lazy { // 데이터에 변경이나 접근이 많을 때 사용하면 좋다
        (application as FoodApplication).foodRepo
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

        // init RecyclerView
        val adapter = FoodAdapter(ArrayList<Food>())

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        binding.foodRecyclerView.layoutManager = layoutManager
        binding.foodRecyclerView.adapter = adapter

        // get all foods
        //val foods = foodDao.getAllFoods() // app이 죽음

//        Thread { // 스레드 만들면 app이 죽지 않는다
//            val foods = foodDao.getAllFoods()
//            for (food in foods) {
//                Log.d(TAG, food.toString())
//            }
//        }.start() // onCreate가 실행될 때, 한 번 수행하고 끝난다
        // 새로 추가된 데이터가 Logcat에 보이지 않는다
        // 새로 실행해야 보임!

//        val foodFlow: Flow<List<Food>> = foodDao.getAllFoods()
        val foodFlow: Flow<List<Food>> = foodRepo.allFoods
        // 변경이 있을 때마다 실행
        CoroutineScope(Dispatchers.Main).launch { // collect는 코루틴 스코프 안에서 사용해야 함
            // collect로 계속 관찰 ㄱㄴ
            foodFlow.collect { foods -> // foods는 List<Food>를 의미한다
            // foodFlow.distinctUntilChanged().collect { foods -> 처럼 distinctUntilChanged()를 사용하면 데이터의 값이 변화가 있을 때만 작동
//                for (food in foods) {
//                    Log.d(TAG, food.toString()) // 데이터를 추가하면 바로 Logcat에 찍힌다
//                }
                adapter.foods.clear()
                adapter.foods.addAll(foods)
                adapter.notifyDataSetChanged()
            }
        }

        /*
        Dispatchers.Main: 메인 쓰레드 화면 갱신
        Dispatchers.IO: DB 접근, 입출력
        Dispatchers.default: 계산량이 많아 cpu파워가 많이 필요할 때

        launch하면 스코프가 동작하면서 함수 실행됨
        launch는 리턴값(응답값)이 없는 동작을 실행할 때
        */

        // food by country
        binding.btnShow.setOnClickListener {
            val countrName = binding.etCountry.text.toString()

//            Thread {
//                val foods = foodDao.getFoodsByCountry(countrName)
//                Log.d(TAG, foods.toString())
//            }.start()
            CoroutineScope(Dispatchers.Main).launch {
//                val foods = foodDao.getFoodsByCountry(countrName)
                val foods = foodRepo.showFoodsByCountry(countrName)
                for (food in foods) {
                    Log.d(TAG, foods.toString())
                }
            }
//            adapter.foods.clear()
//            adapter.foods.addAll(foods)
//            adapter.notifyDataSetChanged()
        }


        // insert new food
        binding.btnInsert.setOnClickListener {
            val foodName = binding.etFood.text.toString()
            val countryName = binding.etCountry.text.toString()
            val food = Food(0, foodName, countryName)   // new food

//            Thread {
//                foodDao.insertFood(food)
//            }.start()
            CoroutineScope(Dispatchers.IO).launch { // 변경이 있을 때마다 DB에서 읽어와서 갱신
//                foodDao.insertFood(food)
                foodRepo.addFood(food)
            }
        }

        // update food id 2
//        binding.btnUpdate.setOnClickListener {
//            val foodName = binding.etFood.text.toString()
//            val countryName = binding.etCountry.text.toString()
//            val targetFood = Food(2, foodName, countryName)
//            CoroutineScope(Dispatchers.IO).launch {
////                foodDao.updateFood(targetFood)
//                foodRepo.modifyFood(targetFood)
//            }
//        }
        // 음식 이름 기준으로 나라 이름 변경
        binding.btnUpdate.setOnClickListener {
            val foodName = binding.etFood.text.toString()
            val countryName = binding.etCountry.text.toString()
            //val targetFood = Food(2, foodName, countryName)
            CoroutineScope(Dispatchers.IO).launch {
                foodRepo.modifyFood(foodName, countryName)
            }
        }

//        // update food id 3
//        binding.btnDelete.setOnClickListener {
//            val targetFood = Food(3, "", "")    // delete food _id 3
//            CoroutineScope(Dispatchers.IO).launch {
////                foodDao.deleteFood(targetFood)
//                foodRepo.removeFood(targetFood)
//            }
//        }
        // 음식 이름 기준으로 지우기
        binding.btnDelete.setOnClickListener {
            val foodName = binding.etFood.text.toString()
            //val targetFood = Food(3, "", "")    // delete food _id 3
            CoroutineScope(Dispatchers.IO).launch {
                foodRepo.removeFood(foodName)
            }
        }
        adapter.setOnItemLongClickListener(object: FoodAdapter.OnItemLongClickListener {
            override fun onItemLongClickListener(view: View, pos: Int) {
                val clickedFood = adapter.foods[pos].food.toString()
                CoroutineScope(Dispatchers.IO).launch {
                    foodRepo.removeFood(clickedFood)
                }
            }
        })
    }
}