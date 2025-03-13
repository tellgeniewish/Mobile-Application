package ddwu.com.mobileapplication.finalproject

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import ddwu.com.mobileapplication.finalproject.data.Diary
import ddwu.com.mobileapplication.finalproject.databinding.ActivityShowBinding
import ddwu.com.mobileapplication.finalproject.ui.DiaryAdapter
import ddwu.com.mobileapplication.finalproject.ui.DiaryViewModel
import ddwu.com.mobileapplication.finalproject.ui.DiaryViewModelFactory

class ShowActivity: AppCompatActivity() {
    val TAG = "ShowActivity"

    val showBinding by lazy {
        ActivityShowBinding.inflate(layoutInflater)
    }

    var selectedIdx = 1 // 멤버변수로 선언해야 함

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(showBinding.root)

        val adapter = DiaryAdapter(ArrayList<Diary>())

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        showBinding.diaryRecyclerView.layoutManager = layoutManager
        showBinding.diaryRecyclerView.adapter = adapter

        val viewModel: DiaryViewModel by viewModels { // by는 코틀린에서 제공delegate하는 대리하는 역할: 아직 구현하지 않은 기능을 viewModels에서 가져온다
            DiaryViewModelFactory((application as MDApplication).diaryRepo)
        }

        viewModel.allDiarys.observe(this, Observer{ diarys ->
            adapter.diarys = diarys
            adapter.notifyDataSetChanged()

        }) // 라이브 데이터 타입

        adapter.setOnItemClickListener(object: DiaryAdapter.OnItemClickListener {
            //수정 구현
            override fun onItemClick(view: View, position: Int) {
                val diary = adapter.diarys.get(position)

                val intent = Intent(this@ShowActivity, UpdateActivity::class.java)
                intent.putExtra("diary", diary)
                startActivity(intent)
            }
        })

        adapter.setOnItemLongClickListener(object: DiaryAdapter.OnItemLongClickListener {
            //삭제 구현
            override fun onItemLongClickListener(view: View, position: Int) {
                val diary = adapter.diarys.get(position)

                viewModel.removeDiary(diary)
            }
        })

        showBinding.btnMove.setOnClickListener {
            val builder: AlertDialog.Builder =
                AlertDialog.Builder(this@ShowActivity).apply {
                    setTitle("어느 것을 보시겠습니까?")
                    setSingleChoiceItems(
                        R.array.move, selectedIdx
                    ) { // (보여줄 배열, 몇 번 째가 선택되어 있는지, 선택했을 때 리스너)
                            dialogInterface: DialogInterface?, idx: Int
                        -> selectedIdx = idx // 선택하는 순간 몇 번 째를 선택했는지를 바꾼다
                    }

                    setPositiveButton("확인") { p0: DialogInterface?, whichButton: Int ->
                        if (selectedIdx == 0) { // 검색
                            val intent = Intent(this@ShowActivity, SearchActivity::class.java)
                            startActivity(intent)
                        } else { // 후기란
                            // 그대로
                        }

                    }
                    setNegativeButton("취소", null)
                    setCancelable(false)
                }
            builder.show()
        }

        showBinding.btnNear.setOnClickListener {
            val intent = Intent(this@ShowActivity, NearActivity::class.java)
            startActivity(intent)
        }

        showBinding.btnBack.setOnClickListener {
            finish()
        }
    }
}