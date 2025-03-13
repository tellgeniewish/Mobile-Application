package ddwu.com.mobileapp.week04.roomexam01

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import ddwu.com.mobileapp.week04.roomexam01.data.Food
import ddwu.com.mobileapp.week04.roomexam01.databinding.ActivityMainBinding
import ddwu.com.mobileapp.week04.roomexam01.ui.FoodViewModel
import ddwu.com.mobileapp.week04.roomexam01.ui.FoodViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"

    // view binding object
    val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

//    val foodViewModel: FoodViewModel by viewModels {
//        FoodViewModelFactory( (application as FoodApplication).foodRepo )
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContentView(binding.root)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        val adapter = FoodAdapter(ArrayList<Food>())

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        binding.foodRecyclerView.layoutManager = layoutManager
        binding.foodRecyclerView.adapter = adapter

        // FoodViewModel
        // 지역변수로 만들면 액티비티가 사라질 때 함께 사라진다 -> Factory를 사용해서 만들어야 함
        val viewModel: FoodViewModel by viewModels { // by는 코틀린에서 제공하는 대리(delegate)하는 역할: 아직 구현하지 않은 기능을 viewModels에서 가져온다
            FoodViewModelFactory((application as FoodApplication).foodRepo)
        }

        // get all foods
        // 화면이 안 보여져도 계속 관찰(수집)함
//        val foodRepo = (application as FoodApplication).foodRepo

//        val foodFlow = foodRepo.allFoods
//        CoroutineScope(Dispatchers.Main).launch {
//            foodFlow.collect { foods ->
//                adapter.foods.clear()
//                adapter.foods.addAll(foods)
//                adapter.notifyDataSetChanged()
//            }
//        }
        viewModel.allFoods.observe(this, Observer{ foods -> // 라이브 데이터 타입
            // allFoods가 화면이 보일 때만 관찰함
            adapter.foods = foods
            adapter.notifyDataSetChanged()
        })

        // food by country
        binding.btnShow.setOnClickListener {
            val country = binding.etCountry.text.toString()

//            CoroutineScope(Dispatchers.IO).launch {
//                val foods = foodRepo.getFoodByCountry(country)
//                for (food in foods) {
//                    Log.d(TAG, food.toString())
//                }
//            }
            CoroutineScope(Dispatchers.Main).launch { // Main인 이유: binding.etFood 때문에
                // 화면에 있는 요소에 접근하려면 Main을 사용해야 함
                // IO를 쓰면 죽음
                val foods = viewModel.getFoodByCountry(country).await() // await은 코루틴 안에서 호출 ㄱㄴ
                binding.etFood.setText(foods[0].foodName)
                for (food in foods) {
                    Log.d(TAG, food.toString())
                }
            }

        }

        binding.btnInsert.setOnClickListener {
            val foodName = binding.etFood.text.toString()
            val countryName = binding.etCountry.text.toString()
            val food = Food(0, foodName, countryName)   // new food

            // 직접 쓰는 방식
//            CoroutineScope(Dispatchers.IO).launch {
//                foodRepo.addFood(food)
//            }
            viewModel.addFood(food)
        }

        // update food
        binding.btnUpdate.setOnClickListener {
            val foodName = binding.etFood.text.toString()
            val countryName = binding.etCountry.text.toString()
            val food = Food(0, foodName, countryName)

//            CoroutineScope(Dispatchers.IO).launch {
////                foodRepo.modifyFood(food)
//                foodRepo.modifyFoodCountryByFood(food)
//            }
            viewModel.modifyFoodCountryByFood(food)
        }

        // delete food
        binding.btnDelete.setOnClickListener {
            val foodName = binding.etFood.text.toString()
            val food = Food(0, foodName, "")
//            CoroutineScope(Dispatchers.IO).launch {
////                foodRepo.removeFood(food)
//                foodRepo.removeFoodByName(food)       // 음식 이름으로 삭제
//            }
            viewModel.removeFoodByName(food)
        }

    }
}