package ddum.com.mobile.week11.lbstest

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import ddum.com.mobile.week11.lbstest.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivityTag"

    val mainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback
    lateinit var geocoder: Geocoder // 서버로 정보를 가져오기 때문에 네트워크가 활성화되어 있어야 함

    var lat: Double? = null // nullable 타입으로 선언
    var lng: Double? = null // nullable 타입으로 선언

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mainBinding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = LocationRequest.Builder(1000 * 3) // 수신 간격
            .setMinUpdateIntervalMillis(1000 * 5) // 최소 수신 간격
            .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY) // 우선 순위
            .build()

//        var lat: Double = 0.0
//        var lng: Double = 0.0
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val currentLocation: Location = locationResult.locations[0]
                lat = currentLocation.latitude
                lng = currentLocation.longitude
                Log.d(TAG, "위도: ${currentLocation.latitude}, 경도: ${currentLocation.longitude}")
            }
        }

        geocoder = Geocoder(this, Locale.getDefault())

        // 마지막 위치 정보 가져오기
        getLastLocation()

        mainBinding.btnLocation.setOnClickListener {
            checkPermissions()
            startLocationRequest()
        }

        mainBinding.btnGeocoding.setOnClickListener {
//            geocoder.getFromLocation(37.505816, 127.042383, 5) {
                // maxResults는 한 위도와 경도에 여러 위치 정보가 있을 수도 있어서 몇 개를 받을건지
                // 예를 들어, 한 건물에 층마다 다른 위치 정보가 있을 수도... 1층 꽃집, 2층 맥도날드, 3층 서점
            if (lat != null && lng != null) {
            geocoder.getFromLocation(lat!!, lng!!, 5) {
                    addresses ->
                        //CoroutineScope(Dispatchers.Main).launch {
                            Log.d(TAG, "리버스 지오코딩 주소: ${addresses.get(0).getAddressLine(0)}")
                        //}
            }
                }
            geocoder.getFromLocationName("동덕여자대학교", 5) {
                    addresses ->
                        //CoroutineScope(Dispatchers.Main).launch {
                            Log.d(TAG, "지오코딩 위도: ${addresses.get(0).latitude}, 경도: ${addresses.get(0).longitude}")
                        //}
            }
        }

        mainBinding.btnExternal.setOnClickListener {
            callExternalMap()
        }
    }

    override fun onPause() {
        super.onPause()

        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun startLocationRequest() {
        // 경고일뿐 실행에 문제없음
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    // 마지막 위치 정보 가져오기
    private fun getLastLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                lat = location.latitude
                lng = location.longitude
                Log.d(TAG, "마지막 위치 -> 위도: $lat, 경도: $lng")
            } else {
                Log.d(TAG, "위치 정보를 가져올 수 없습니다.")
            }
        }
        fusedLocationClient.lastLocation.addOnFailureListener { e: Exception ->
            Log.d(TAG, "위치 정보 실패: ${e.toString()}")
        }
    }

    fun callExternalMap() {
        val locLatLng   // 위도/경도 정보로 지도 요청 시
            = String.format("geo:%f,%f?z=%d", 37.606320, 127.041808, 17) // 위도, 경도, 배율
        val locName     // 위치명으로 지도 요청 시
                = "https://www.google.co.kr/maps/place/" + "Hawolgok-dong"
//        val uri = Uri.parse(locLatLng)
         val uri = Uri.parse(locName)

        val intent = Intent(Intent.ACTION_VIEW, uri) // 암묵적 인텐트로 구글맵이 실행됨
        startActivity(intent)
    }

    val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions() ) { permissions -> // 람다함수
        when {
            // FINE 먼저 검사해야 함
            permissions.getOrDefault(ACCESS_FINE_LOCATION, false) ->
                Log.d(TAG, "정확한 위치 사용")
            permissions.getOrDefault(ACCESS_COARSE_LOCATION, false) ->
                Log.d(TAG, "근사 위치 사용")
            else ->
                Log.d(TAG, "권한 미승인") // flag = false 후 위치 사용 코드 실행 안 되도록
        }
    }

    private fun checkPermissions() {
        if ( checkSelfPermission(ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && checkSelfPermission(ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            Log.d(TAG, "필요 권한 있음")
//            startLocationRequest()
        } else { // 권한을 요청하는 대화상자
            locationPermissionRequest.launch(
                arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)
            )
        }
    }
}