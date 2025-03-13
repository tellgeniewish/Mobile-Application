package ddum.com.mobile.week09.naverretrofitsample

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Placeholder
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import ddum.com.mobile.week09.naverretrofitsample.data.util.FileManager
import ddum.com.mobile.week09.naverretrofitsample.databinding.ActivityMainBinding
import ddum.com.mobile.week09.naverretrofitsample.ui.BookAdapter
import ddum.com.mobile.week09.naverretrofitsample.ui.NVViewModel
import ddum.com.mobile.week09.naverretrofitsample.ui.NVViewModelFactory
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader

class MainActivity : AppCompatActivity() {

    val TAG = "MAIN_ACTIVITY_TAG"

    val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val adapter = BookAdapter()
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        binding.rvBooks.layoutManager = layoutManager
        binding.rvBooks.adapter = adapter

        val nvViewModel : NVViewModel by viewModels {
            NVViewModelFactory( (application as NVApplication).nvRepository )
        }

        nvViewModel.books.observe(this) { books ->
            adapter.books = books
            adapter.notifyDataSetChanged()
        }

        nvViewModel.drawable.observe(this) { drawable ->
            binding.imageView.setImageBitmap(drawable)
        }

        // Device Explorer
        // 내부 저장소: /data/data/패키지 이름/files
        // 외부 저장소: /sdcard 또는 system 아래는 전부 외부 저장소
        // 해당하는 앱만 사용할 수 있는 외부 저장소: /sdcard 또는 system/Android/data/패키지 이름
        // --> 내부 저장소와 외부 저장소에 앱 전용 공간이 존재할 수 있다
        Log.d(TAG, "${filesDir}")
        Log.d(TAG, "${cacheDir}")
        Log.d(TAG, "${getExternalFilesDir(null).toString()}")

        // 쓰기(매번 Synchronize해야 확인 ㄱㄴ)
//        val writeData = "Mobile Application"
//        val writeFile = File(filesDir, "test.txt")
//        val outputStream = FileOutputStream(writeFile)
//        outputStream.write(writeData.toByteArray())
//        outputStream.close()

        // 읽기(내부 저장소)
//        openFileInput("test.txt").bufferedReader().useLines { lines ->
//            for (line in lines) {
//                Log.d(TAG, line + "\n")
//            }
//        }

        // 내부 저장소에 있는 이미지 파일 읽기
//        val imageFile = File(filesDir, "image.jpg")
//        val bitmap = BitmapFactory.decodeFile(imageFile.path)
//        binding.imageView.setImageBitmap(bitmap)
        // 저장할 땐 상관없지만 읽어올 때 사진 용량이 너무 크면 앱이 죽는다 --> Glide 사용하면 됨

        // Glide로 내부 저장소에 있는 이미지 파일 읽기
//        Glide.with(this)
//            .load("${filesDir}/images/image.jpg") // 기본 위치라면 "${filesDir}/image.jpg"
//            .into(binding.imageView)

        // 필요할 경우 파일 디렉토리 생성
        // 내부저장소 전용위치에 images 하위 디렉토리 생성
        FileManager.createSubDirectory(filesDir, "images")

        adapter.setOnItemClickListener(object: BookAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val url = adapter.books?.get(position)?.image
                Log.d(TAG, url.toString())
                // 실습1. url 에 해당하는 이미지 바로 표시
//                Glide.with(this@MainActivity)
//                    .load(url)
//                    .into(binding.imageView)
                    // 내부에서 스레드 처리를 함

                // 실습2. ViewModel 을 통해 Bitmap 을 가져와 표시
//                nvViewModel.setImage(url)

//                Glide.with(this@MainActivity)
//                    .asBitmap()
//                    .load(url)
//                    .into(object: CustomTarget<Bitmap> (350, 350) { // 픽셀 // 원본 사용하려면 Target.SIZE_ORIGINAL
//                        override fun onResourceReady( // 잘 읽어온 상태
//                            resources: Bitmap,
//                            transition: Transition<in Bitmap>?
//                        ) { // 파일로 저장하는 작업
//                            val imageFile = File("${filesDir}/images", "image.jpg")
//                            val fos = FileOutputStream(imageFile)
//                            resources.compress(Bitmap.CompressFormat.JPEG, 100, fos)
//                            fos.close()
//                        }
//
//                        override fun onLoadCleared(placeholder: Drawable?) { // 사진을 읽어오다 문제 발생했을 때
//                            Log.d(TAG, "Image load cleared")
//                        }
//                    })

                // 실습3. 클릭할 경우 Image 의 url 을 Intent 에 저장(key: url) 후 DetailActivity 호출
                val intent: Intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra("url", url)
                startActivity(intent)
            }
        })



        binding.btnSearch.setOnClickListener{
            val query = binding.etKeyword.text.toString()
            nvViewModel.getBooks(query)
        }


    }
}