package ddwu.com.mobileapplication.finalproject

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import ddwu.com.mobileapplication.finalproject.data.Diary
import ddwu.com.mobileapplication.finalproject.data.Medical
import ddwu.com.mobileapplication.finalproject.databinding.ActivityWriteBinding
import ddwu.com.mobileapplication.finalproject.ui.DiaryViewModel
import ddwu.com.mobileapplication.finalproject.ui.DiaryViewModelFactory
import java.util.Calendar

class WriteActivity: AppCompatActivity() {
    val TAG = "WriteActivity"

    val writeBinding by lazy {
        ActivityWriteBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(writeBinding.root)

        var medical = intent.getSerializableExtra("medical") as Medical
        Log.d(TAG, "WriteActivity: ${medical}")

        writeBinding.tvName.setText(medical.institNm)

        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH) + 1 // 월은 0부터 시작하므로 1을 더해줍니다.
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        writeBinding.writeYear.setText(currentYear.toString())  // 현재 연도
        writeBinding.writeMonth.setText(currentMonth.toString())  // 현재 월
        writeBinding.writeDay.setText(currentDay.toString())  // 현재 일

        val viewModel: DiaryViewModel by viewModels { // by는 코틀린에서 제공delegate하는 대리하는 역할: 아직 구현하지 않은 기능을 viewModels에서 가져온다
            DiaryViewModelFactory((application as MDApplication).diaryRepo)
        }

        writeBinding.btnBack.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this@WriteActivity).apply {
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

        writeBinding.btnSave.setOnClickListener {
            val mdName = writeBinding.tvName.text.toString()
            val memo = writeBinding.writeMemo.text.toString()
            val diary = Diary(0, mdName, currentYear, currentMonth, currentDay, memo)
            viewModel.addDiary(diary)

            val intent: Intent = Intent(this@WriteActivity, ShowActivity::class.java)
            startActivity(intent)
        }
    }
}