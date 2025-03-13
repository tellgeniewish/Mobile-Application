package ddum.com.mobile.week09.naverretrofitsample

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import ddum.com.mobile.week09.naverretrofitsample.databinding.ActivityDetailBinding
import java.io.File
import java.io.FileOutputStream
import java.util.Date

class DetailActivity : AppCompatActivity() {
    val TAG = "DETAIL_ACTIVITY_TAG"

    val detailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }

    var imageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(detailBinding.root)

        // MainActivity 로부터 전달받은 이미지의 URL
        imageUrl = intent.getStringExtra("url")

        Glide.with(this)
            .load(imageUrl)//"${filesDir}/images/image.jpg")하면 전에 실행했을 때 저장한 image.jpg가 뜬다
            .into(detailBinding.ivBookCover)

        fun getCurrentTime() : String {
            return SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        }
        val storeImg = "${getCurrentTime()}.jpg"

        detailBinding.btnSave.setOnClickListener {
            Glide.with(this@DetailActivity)
                .asBitmap()
                .load(imageUrl)
                .into(object: CustomTarget<Bitmap> (350, 350) { // 픽셀 // 원본 사용하려면 Target.SIZE_ORIGINAL
                    override fun onResourceReady( // 잘 읽어온 상태
                        resources: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
//                        fun getCurrentTime() : String {
//                            return SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//                        }
                        Log.d(TAG, "file name: $storeImg")

                        // 파일로 저장하는 작업
                        val imageFile = File("${filesDir}/images", storeImg)
                        val fos = FileOutputStream(imageFile)
                        resources.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                        fos.close()
                    }

                    override fun onLoadCleared(placeholder: Drawable?) { // 사진을 읽어오다 문제 발생했을 때
                        Log.d(TAG, "Image load cleared")
                    }
                })
        }

        detailBinding.btnRead.setOnClickListener {
            val imageFile = File("${filesDir}/images", storeImg)
            val bitmap = BitmapFactory.decodeFile(imageFile.path)
                detailBinding.ivBookCover.setImageBitmap(bitmap)
            }

        detailBinding.btnInit.setOnClickListener {
            Glide.with(this)
                .load(R.drawable.ic_launcher_foreground)
                .into(detailBinding.ivBookCover)
        }

        detailBinding.btnRemove.setOnClickListener {
            val deleteFile = File ("${filesDir}/images/${storeImg}") // 지정 위치 파일 삭제
            Log.d(TAG, "delete file name: $deleteFile")
            deleteFile.delete()
        }
    }
}