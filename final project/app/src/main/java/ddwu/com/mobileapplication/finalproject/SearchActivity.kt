package ddwu.com.mobileapplication.finalproject

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ddwu.com.mobileapplication.finalproject.databinding.ActivitySearchBinding
import ddwu.com.mobileapplication.finalproject.ui.MDViewModel
import ddwu.com.mobileapplication.finalproject.ui.MDViewModelFactory
import ddwu.com.mobileapplication.finalproject.ui.MedicalAdapter

class SearchActivity: AppCompatActivity() {
    val TAG = "SearchActivity"

    val searchBinding by lazy {
        ActivitySearchBinding.inflate(layoutInflater)
    }

    lateinit var adapter : MedicalAdapter

    var selectedIdx = 0 // 멤버변수로 선언해야 함

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(searchBinding.root)

        val mdViewModel : MDViewModel by viewModels {
            MDViewModelFactory( (application as MDApplication).networkRepo )
        }

        adapter = MedicalAdapter()
        searchBinding.rvMedical.adapter = adapter
        searchBinding.rvMedical.layoutManager = LinearLayoutManager(this@SearchActivity)

        mdViewModel.medicals.observe(this) { medicals ->
            if (medicals.isNullOrEmpty()) {
                // 데이터가 없으면 "다시 입력해주세요"라는 메시지 표시
                Toast.makeText(this, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show()
            } else {
                adapter.medicals = medicals
                adapter.notifyDataSetChanged()
            }
        }

        searchBinding.btnSearch.setOnClickListener{
            val query = searchBinding.etKeyword.text.toString()
            if (query.isNotEmpty()) {
                mdViewModel.showMedical(query)
            } else {
                Toast.makeText(this, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        adapter.setOnItemClickListener(object: MedicalAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val where = adapter.medicals?.get(position)
                Log.d(TAG, "where: ${where}")
                Log.d(TAG, "institNm: ${where?.institNm}")

                // 클릭할 경우 Intent 에 저장(key: url) 후 DetailActivity 호출
                val intent: Intent = Intent(this@SearchActivity, DetailActivity::class.java)
                intent.putExtra("where", where)
                startActivity(intent)
            }
        })

        searchBinding.btnMove.setOnClickListener {
            val builder: AlertDialog.Builder =
                AlertDialog.Builder(this@SearchActivity).apply {
                    setTitle("어느 것을 보시겠습니까?")
                    setSingleChoiceItems(
                        R.array.move, selectedIdx
                    ) { // (보여줄 배열, 몇 번 째가 선택되어 있는지, 선택했을 때 리스너)
                            dialogInterface: DialogInterface?, idx: Int
                        -> selectedIdx = idx // 선택하는 순간 몇 번 째를 선택했는지를 바꾼다
                    }

                    setPositiveButton("확인") { p0: DialogInterface?, whichButton: Int ->
                        if (selectedIdx == 0) { // 검색
                            // 그대로
                        } else { // 후기란
                            val intent = Intent(this@SearchActivity, ShowActivity::class.java)
                            startActivity(intent)
                        }
                    }
                    setNegativeButton("취소", null)
                    setCancelable(false)
                }
            builder.show()
        }
    }
}