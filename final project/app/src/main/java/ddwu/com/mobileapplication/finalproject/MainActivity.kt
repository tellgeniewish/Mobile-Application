package ddwu.com.mobileapplication.finalproject

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ddwu.com.mobileapplication.finalproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    // 과제명: J의 병원 후기 다이어리
    // 분반: 02 분반
    // 학번: 20211689 성명: 김현진
    // 제출일: 2024년 12월 23일
    val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.main.setOnClickListener {
            val intent = Intent(this@MainActivity, SearchActivity::class.java)
            startActivity(intent)
        }

    }
}