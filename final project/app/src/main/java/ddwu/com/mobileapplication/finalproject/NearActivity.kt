package ddwu.com.mobileapplication.finalproject

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ddwu.com.mobileapplication.finalproject.data.Poi
import ddwu.com.mobileapplication.finalproject.data.network.NVService
import ddwu.com.mobileapplication.finalproject.data.network.TMapNetworkService
import ddwu.com.mobileapplication.finalproject.databinding.ActivityNearBinding
//import ddwu.com.mobileapplication.finalproject.ui.NVViewModel
//import ddwu.com.mobileapplication.finalproject.ui.NVViewModelFactory
//import ddwu.com.mobileapplication.finalproject.ui.TMapViewModel
//import ddwu.com.mobileapplication.finalproject.ui.TMapViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URLEncoder

class NearActivity: AppCompatActivity() {
    val TAG = "NearActivity"

    val nearBinding by lazy {
        ActivityNearBinding.inflate(layoutInflater)
    }
    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback

    private lateinit var googleMap: GoogleMap

//    lateinit var geocoder: Geocoder // 서버로 정보를 가져오기 때문에 네트워크가 활성화되어 있어야 함
    var geocodeAddress: String? = null
    var lat: Double? = null // nullable 타입으로 선언
    var lng: Double? = null // nullable 타입으로 선언

    lateinit var tmapNetworkService: TMapNetworkService
    lateinit var nvService: NVService

    private val mapReadyCallback = object: OnMapReadyCallback {
        override fun onMapReady(map: GoogleMap) {
            googleMap = map
            Log.d(TAG, "GoogleMap is ready")
            //addMarker(myLoc) // 현재 내 위치
            // 마커 클릭 이벤트 처리
            googleMap.setOnMarkerClickListener { marker ->
                Toast.makeText(this@NearActivity, marker.tag.toString(), Toast.LENGTH_SHORT).show()
                //Toast.makeText(this, marker.tag.toString(), Toast.LENGTH_SHORT).show()
                false // true일 경우 이벤트처리 종료이므로 info window 미출력
            }
            // 마커 InfoWindow 클릭 이벤트 처리
            googleMap.setOnInfoWindowClickListener { marker ->
                Toast.makeText(this@NearActivity, marker.snippet, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(nearBinding.root)

//        val tmViewModel : TMapViewModel by viewModels {
//            TMapViewModelFactory( (application as MDApplication).tmRepo )
//        }
//        val nvViewModel : NVViewModel by viewModels {
//            NVViewModelFactory( (application as MDApplication).nvRepository)
//        }

        tmapNetworkService = TMapNetworkService(this@NearActivity)
        nvService = NVService(this@NearActivity)

        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(mapReadyCallback)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@NearActivity)

        locationRequest = LocationRequest.Builder(3000)
            .setMinUpdateIntervalMillis(5000)
            .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val currentLocation: Location = locationResult.locations[0]
                lat = currentLocation.latitude
                lng = currentLocation.longitude
                Log.d(TAG, "위도: ${currentLocation.latitude}, " +
                        "경도: ${currentLocation.longitude}")
                val targetLoc = LatLng(currentLocation.latitude, currentLocation.longitude)
//                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(targetLoc, 17F))
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(targetLoc, 17F))
                addMarker(targetLoc)
                centerMarker?.position = targetLoc
            }
        }

        nearBinding.btnStart.setOnClickListener {
            checkPermissions()
            startLocationRequest()
        }
        nearBinding.btnStop.setOnClickListener {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }

            nearBinding.btnPHFind.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    if (lat != null && lng != null) {
                        // 데이터 가져오기
                        val pois = tmapNetworkService.getTMaps("약국", lat!!, lng!!)
                        withContext(Dispatchers.Main) {

                            googleMap.clear()

                            // POI 마커 추가
                            pois.forEach { poi ->
                                val markerOptions = MarkerOptions()
                                    .position(LatLng(poi.noorLat, poi.noorLon))
                                    .title("약국 이름은")
                                    .snippet(poi.name)
                                    .icon(
                                        BitmapDescriptorFactory.defaultMarker(
                                            BitmapDescriptorFactory.HUE_BLUE
                                        )
                                    )
                                val phMarker = googleMap.addMarker(markerOptions)
                                phMarker?.tag = "${poi.name}의 연락처는 ${poi.telNo}"
                            }
                        }
                    } else {
                        Log.e("오류", "위치 정보를 가져올 수 없습니다.")
                    }
                } catch(e: Exception) {
                    Log.e("오류", "오류 발생: ${e.message}")
                }
            }
        }
//
//        // tmaps LiveData 구독
//        tmViewModel.tmaps.observe(this) { pharmacies ->
//            // LiveData가 업데이트되면 호출되어 pharmacies를 사용할 수 있습니다.
//            // 예: pharmacies 리스트를 사용하여 지도에 마커를 추가하는 로직 작성
//            for (poi in pharmacies) {
//                Log.d(TAG, "Pharmacy: ${poi.name}, Location: ${poi.noorLat}, ${poi.noorLon}")
//                // poi 데이터를 사용하여 지도에 마커를 추가
//            }
//        }
        nearBinding.btnBack.setOnClickListener {
            finish()
        }
    }
    
    private lateinit var markerOptions: MarkerOptions
    private var centerMarker: Marker? = null
    fun addMarker(targetLoc: LatLng) {
        if (centerMarker != null) {
            // 기존 마커가 존재하면 위치만 업데이트
            centerMarker?.position = targetLoc
        } else {
            markerOptions = MarkerOptions().apply {
                position(targetLoc)
                title("내 위치")
                if (geocodeAddress == null)
                    snippet("여기!")
                else
                    snippet(geocodeAddress)
                icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            }
            centerMarker = googleMap.addMarker(markerOptions)
            centerMarker?.showInfoWindow() // 없으면 마커를 클릭해야 정보가 보인다
            centerMarker?.tag = "위도: ${targetLoc.latitude}, 경도: ${targetLoc.longitude}"
        }
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun startLocationRequest() {
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    // Permission 확인
    val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions() ) { permissions ->
        when {
            permissions.getOrDefault(ACCESS_FINE_LOCATION, false) ->
                Log.d(TAG, "정확한 위치 사용")
            permissions.getOrDefault(ACCESS_COARSE_LOCATION, false) ->
                Log.d(TAG, "근사 위치 사용")
            else ->
                Log.d(TAG, "권한 미승인")
        }
    }

    private fun checkPermissions() {
        if ( checkSelfPermission(ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && checkSelfPermission(ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            Log.d(TAG, "필요 권한 있음")
//            startLocationRequest()
        } else {
            locationPermissionRequest.launch(
                arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)
            )
        }
    }
}