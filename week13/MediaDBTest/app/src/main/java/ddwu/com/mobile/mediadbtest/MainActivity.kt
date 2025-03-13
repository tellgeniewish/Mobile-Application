package ddwu.com.mobile.mediadbtest

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import ddwu.com.mobile.mediadbtest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivityTag"

    val mainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    val adapter by lazy {
        MediaFileAdapater()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mainBinding.root)

        mainBinding.recyclerView.adapter = adapter
        mainBinding.recyclerView.layoutManager = LinearLayoutManager(this)


        mainBinding.button.setOnClickListener {
            if (checkPermissions()) {
                val list = searchImages()
                adapter.medias = list
                adapter.notifyDataSetChanged()
            }
        }
        // 이미지의 ID (Long 타입)

        adapter.setOnItemClickListener(object: MediaFileAdapater.OnItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                val id = adapter.medias?.get(position)?.id
                // 외부 저장소의 특정 이미지 가져오기
                if (id != null) {
                    val uri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id
                        // 내 갤러리에 있는 사진을 DB에 보관하고 싶으면. id나 uri를 보관하면 된다.
                    )
                    // 이미지의 ID를 사용하여 URI 생성 후 이미지뷰에 지정
                    mainBinding.imageView.setImageURI(uri)
                }
            }
        })

    }




    @SuppressLint("Range")
    private fun searchImages() : List<MediaDto> {
        val imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI // 외부저장소 이미지 대상

        val projection = arrayOf(
            MediaStore.Images.Media._ID,    // ID
            MediaStore.Images.Media.DISPLAY_NAME,   // 파일명
            MediaStore.Images.Media.DATA,       // 전체경로
        )
        val selection = MediaStore.Images.Media.MIME_TYPE + "=?"    // 이미지 유형 지정
        val selectArgs = arrayOf("image/jpeg")      // jpg 이미지 (image/png)

        var list = arrayListOf<MediaDto>()

        /*contentResolver 를 사용하여 외부저장소의 이미지 확인*/
        val cursor : Cursor = contentResolver.query(
            // 지금은 null이라서 모든 이미지를 가져온다.
            imageUri, null, null, null, null // selection, selectArgs를 넣으면 그 조건에 맞는 결과가 보임
        ) ?: return list

        /*Images 의 각 컬럼 인덱스를 확인하여 cursor 에서 데이터 확인*/
        with (cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndex(MediaStore.Images.Media._ID))
                val fileName = getString(getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME))
                val path = getString(getColumnIndex(MediaStore.Images.Media.DATA))
                list.add( MediaDto(id, fileName, path))
            }
        }
        
        return list
    }


    fun checkPermissions () : Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES)
                == PackageManager.PERMISSION_GRANTED
            ) {
                Log.d(TAG, "The permission is already granted")
                return true
            } else {
                readImagePermissionRequest.launch(Manifest.permission.READ_MEDIA_IMAGES,)
                return false
            }
        }
        return true
    }

    /*registerForActivityResult 는 startActivityForResult() 대체*/
    val readImagePermissionRequest
            = registerForActivityResult( ActivityResultContracts.RequestPermission() ) {
            isGranted ->
        if (isGranted) {
            Log.d(TAG, "The Permission is granted")
        } else {
            Log.d(TAG, "The Permission is not granted")
        }
    }


}