package ddwu.com.mobileapplication.finalproject

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import ddwu.com.mobileapplication.finalproject.data.Diary
import ddwu.com.mobileapplication.finalproject.databinding.ActivityUpdateBinding
import ddwu.com.mobileapplication.finalproject.ui.DiaryViewModel
import ddwu.com.mobileapplication.finalproject.ui.DiaryViewModelFactory
import java.util.Calendar

class UpdateActivity: AppCompatActivity() {
    val TAG = "UpdateActivity"

    val updateBinding by lazy {
        ActivityUpdateBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(updateBinding.root)

        val diary = intent.getSerializableExtra("diary") as Diary

        updateBinding.tvName.setText(diary.name) // 병원명
        updateBinding.writeMemo.setText(diary.memo) // 메모
        updateBinding.writeYear.setText(diary.year.toString())
        updateBinding.writeMonth.setText(diary.month.toString())
        updateBinding.writeDay.setText(diary.day.toString())

        val viewModel: DiaryViewModel by viewModels { // by는 코틀린에서 제공delegate하는 대리하는 역할: 아직 구현하지 않은 기능을 viewModels에서 가져온다
            DiaryViewModelFactory((application as MDApplication).diaryRepo)
        }

        updateBinding.btnBack.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this@UpdateActivity).apply {
                //setTitle("앱 종료")
                setMessage("작업을 취소하시겠습니까?")
                setPositiveButton("확인") { p0: DialogInterface?, whichButton: Int ->
                    finish()
                }
                setNegativeButton("취소", null)
                setCancelable(false)
            }
            builder.show()
        }
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH) + 1 // 월은 0부터 시작하므로 1을 더해줍니다.
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        updateBinding.btnSave.setOnClickListener {
            val mdName = updateBinding.tvName.text.toString()
            val memo = updateBinding.writeMemo.text.toString()

            val updatedDiary = Diary(diary._id, mdName, currentYear, currentMonth, currentDay, memo)
            viewModel.modifyFood(updatedDiary)

            val intent: Intent = Intent(this@UpdateActivity, ShowActivity::class.java)
            startActivity(intent)
        }
    }
}