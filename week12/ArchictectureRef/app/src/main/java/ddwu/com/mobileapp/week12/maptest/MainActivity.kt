package ddwu.com.mobileapp.week12.maptest

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import ddwu.com.mobileapp.week12.maptest.databinding.ActivityMainBinding
import ddwu.com.mobileapp.week12.maptest.ui.MapViewModel
import ddwu.com.mobileapp.week12.maptest.ui.MapViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivityTag"

    val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback

    private lateinit var googleMap: GoogleMap

    var lat: Double? = null // nullable 타입으로 선언
    var lng: Double? = null // nullable 타입으로 선언

    lateinit var geocoder: Geocoder // 서버로 정보를 가져오기 때문에 네트워크가 활성화되어 있어야 함
    var geocodeAddress: String? = null

    private val mapReadyCallback = object: OnMapReadyCallback {
        override fun onMapReady(map: GoogleMap) {
            googleMap = map
            Log.d(TAG, "GoogleMap is ready")
            googleMap.setOnMapLongClickListener {
                latLng -> Log.d(TAG, "롱클릭 위도: ${latLng.latitude}, " +
                    "롱클릭 경도: ${latLng.longitude}")

                //if (lat != null && lng != null) {
                    geocoder.getFromLocation(latLng.latitude, latLng.longitude, 5) {
                            addresses ->
                        //CoroutineScope(Dispatchers.Main).launch {
                            geocodeAddress = addresses[0].getAddressLine(0)
                            Log.d(TAG, "리버스 지오코딩 주소: ${addresses.get(0).getAddressLine(0)}")
                        //}
                        CoroutineScope(Dispatchers.Main).launch { // 처음 롱클릭 시 스니펫에 geocodeAddress보이게 하려면
                            addMarker(latLng) // 롱클릭하는 곳에 마커가 보인다
                        }
                    }
                //}

            }
            //addMarker(LatLng(37.606537, 127.041758))

            // 마커 클릭 이벤트 처리
            googleMap.setOnMarkerClickListener { marker ->
                Toast.makeText(this@MainActivity, marker.tag.toString(), Toast.LENGTH_SHORT).show()
                //Toast.makeText(this, marker.tag.toString(), Toast.LENGTH_SHORT).show()
                false // true일 경우 이벤트처리 종료이므로 info window 미출력
            }
            // 마커 InfoWindow 클릭 이벤트 처리
            googleMap.setOnInfoWindowClickListener { marker ->
                Toast.makeText(this@MainActivity, marker.title, Toast.LENGTH_SHORT).show()
            }
            // PolyLineOptions 설정 추가
            polylineOptions = PolylineOptions().apply {
                color(Color.RED)
                width(5f)
            }
            currentLine = googleMap.addPolyline(polylineOptions)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(mapReadyCallback)


        val mapViewModel : MapViewModel by viewModels {
            MapViewModelFactory( (application as MapApplication).mapRepository )
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

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
                centerMarker?.position = targetLoc
                drawLine(targetLoc) // 이동할 때마다 선으로 이어진다
            }
        }

        geocoder = Geocoder(this, Locale.getDefault())

        binding.btnStart.setOnClickListener {
            checkPermissions()
            startLocationRequest()
        }

        binding.btnStop.setOnClickListener {
//            fusedLocationClient.removeLocationUpdates(locationCallback)
            fusedLocationClient.removeLocationUpdates(locationCallback)
//            val targetLoc = LatLng(37.606328, 127.041008)
//            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(targetLoc, 17F))
//            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(targetLoc, 17F))
        }

        binding.btnGetLast.setOnClickListener {
            // 마지막 위치 정보 가져오기
            if (lat == null && lng == null) {
                Log.d(TAG, "마지막 위치 정보 null")
                addMarker(LatLng(37.606537, 127.041758))
            }
            else
                getLastLocation()
        }

        binding.btnClear.setOnClickListener {
            centerMarker?.remove()
        }
    }

    private lateinit var markerOptions: MarkerOptions
    private var centerMarker: Marker? = null
    fun addMarker(targetLoc: LatLng) {
        markerOptions = MarkerOptions().apply {
            position(targetLoc)
            title("마커 제목")
            if (geocodeAddress == null)
                snippet("마커 스니펫")
            else
                snippet(geocodeAddress)
            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        }
        centerMarker = googleMap.addMarker(markerOptions)
        centerMarker?.showInfoWindow() // 없으면 마커를 클릭해야 정보가 보인다
        centerMarker?.tag = "db_key"
    }

    lateinit var polylineOptions : PolylineOptions  // 선의 옵션, 멤버변수로 지정
    lateinit var currentLine : Polyline     // 지도에 추가한 선, 멤버변수로 지정

    fun drawLine(latLng: LatLng) {
        val newLatLng = LatLng(latLng.latitude, latLng.longitude)
        val points = currentLine.points // 선을 구성하는 점 집합
        points.add(newLatLng)   // 점 집합에 새로운 점 추가
        currentLine.points = points
        Log.d(TAG, "add line")
    }


    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun startLocationRequest() {
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
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