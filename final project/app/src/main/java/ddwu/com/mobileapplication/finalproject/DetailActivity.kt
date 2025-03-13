package ddwu.com.mobileapplication.finalproject

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import ddwu.com.mobileapplication.finalproject.data.Medical
import ddwu.com.mobileapplication.finalproject.databinding.ActivityDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class DetailActivity: AppCompatActivity() {
    val TAG = "DetailActivity"

    val detailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }

    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback

    private lateinit var googleMap: GoogleMap

    lateinit var geocoder: Geocoder // 서버로 정보를 가져오기 때문에 네트워크가 활성화되어 있어야 함
    var geocodeAddress: String? = null
    var lat: Double? = null // nullable 타입으로 선언
    var lng: Double? = null // nullable 타입으로 선언
//    val myLoc = LatLng(35.163153, 129.159328)
    var myCurrentLocation: LatLng? = null

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
                        addLongMarker(latLng) // 롱클릭하는 곳에 마커가 보인다
                    }
                }
                //}

            }
//            addMarker(myLoc) // 현재 내 위치
            // 마커 클릭 이벤트 처리
            googleMap.setOnMarkerClickListener { marker ->
                Toast.makeText(this@DetailActivity, marker.tag.toString(), Toast.LENGTH_SHORT).show()
                //Toast.makeText(this, marker.tag.toString(), Toast.LENGTH_SHORT).show()
                false // true일 경우 이벤트처리 종료이므로 info window 미출력
            }
            // 마커 InfoWindow 클릭 이벤트 처리
            googleMap.setOnInfoWindowClickListener { marker ->
                Toast.makeText(this@DetailActivity, marker.title, Toast.LENGTH_SHORT).show()
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
        setContentView(detailBinding.root)

        var medical = intent.getSerializableExtra("where") as Medical
        Log.d(TAG, "DetailActivity-medical: ${medical}")

        detailBinding.mdName.text = medical.institNm

        detailBinding.tvMon.setText(medical.monday)
        detailBinding.tvTue.setText(medical.tuesday)
        detailBinding.tvWed.setText(medical.wednesday)
        detailBinding.tvThu.setText(medical.thursday)
        detailBinding.tvFri.setText(medical.friday)
        detailBinding.tvSat.setText(medical.saturday)
        detailBinding.tvSun.setText(medical.sunday)

        val mdLat = medical.lat
        val mdLng = medical.lng

        detailBinding.btnBack.setOnClickListener {
            finish()
        }
        detailBinding.btnSave.setOnClickListener {
            val intent: Intent = Intent(this@DetailActivity, WriteActivity::class.java)
            intent.putExtra("medical", medical)
            startActivity(intent)
        }

        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(mapReadyCallback)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@DetailActivity)

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
                myCurrentLocation = targetLoc
                centerMarker?.position = targetLoc
                drawLine(targetLoc) // 이동할 때마다 선으로 이어진다
            }
        }

        geocoder = Geocoder(this@DetailActivity, Locale.getDefault())

        detailBinding.btnStart.setOnClickListener {
            checkPermissions()
            startLocationRequest()
        }
        detailBinding.btnStop.setOnClickListener {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
        detailBinding.btnFind.setOnClickListener {
            if (mdLat != null && mdLng != null) {
                val findMDLoc = LatLng(mdLat, mdLng)
                addMDMarker(findMDLoc, medical) // 내가 원하는 의료기관의 위치
                drawNaviLine(myCurrentLocation!!, findMDLoc)
            } else {
                Log.d(TAG, "mdLat: ${mdLat}, mdLng: ${mdLng}")
            }
        }

//        binding.btnGetLast.setOnClickListener {
//            // 마지막 위치 정보 가져오기
//            if (lat == null && lng == null) {
//                Log.d(TAG, "마지막 위치 정보 null")
//                addMarker(LatLng(37.606537, 127.041758))
//            }
//            else
//                getLastLocation()
//        }
//
//        binding.btnClear.setOnClickListener {
//            centerMarker?.remove()
//        }
    }
    private lateinit var markerOptions: MarkerOptions
    private var centerMarker: Marker? = null
//    private var markersList = mutableListOf<Marker>()
    fun addMarker(targetLoc: LatLng) {
        if (centerMarker != null) {
            // 기존 마커가 존재하면 위치만 업데이트
            centerMarker?.position = targetLoc
        } else {
            markerOptions = MarkerOptions().apply {
                position(targetLoc)
                title("위치는")
                if (geocodeAddress == null)
                    snippet("여기!")
                else
                    snippet(geocodeAddress)
                icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            }
            centerMarker = googleMap.addMarker(markerOptions)
            centerMarker?.showInfoWindow() // 없으면 마커를 클릭해야 정보가 보인다
            centerMarker?.tag = "위도: ${targetLoc.latitude}, 경도: ${targetLoc.longitude}"
//            val newMarker = googleMap.addMarker(markerOptions)
//            markersList.add(newMarker!!) // 🚨 수정된 부분: 마커 리스트에 추가 🚨
//            newMarker?.showInfoWindow()
//            newMarker?.tag = "위도: ${targetLoc.latitude}, 경도: ${targetLoc.longitude}"
        }
    }
    private var mdMarker: Marker? = null
    fun addMDMarker(targetLoc: LatLng, medical: Medical) {
        markerOptions = MarkerOptions().apply {
            position(targetLoc)
            title(medical.institNm)
            if (geocodeAddress == null)
                snippet(medical.streetNmAddr)
            else
                snippet(geocodeAddress)
            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        }
        mdMarker = googleMap.addMarker(markerOptions)
        mdMarker?.showInfoWindow() // 없으면 마커를 클릭해야 정보가 보인다
        mdMarker?.tag = "위도: ${targetLoc.latitude}, 경도: ${targetLoc.longitude}"
    }
    private var longMarker: Marker? = null
    fun addLongMarker(targetLoc: LatLng) {
        markerOptions = MarkerOptions().apply {
            position(targetLoc)
            title("위치는")
            if (geocodeAddress == null)
                snippet("여기!")
            else
                snippet(geocodeAddress)
            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        }
        longMarker = googleMap.addMarker(markerOptions)
        longMarker?.showInfoWindow() // 없으면 마커를 클릭해야 정보가 보인다
        longMarker?.tag = "위도: ${targetLoc.latitude}, 경도: ${targetLoc.longitude}"
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
    fun drawNaviLine(from: LatLng, to: LatLng) {
        // PolylineOptions 객체 설정 (선의 색, 두께 등 설정)
        val polylineOptions = PolylineOptions().apply {
            color(Color.RED)  // 선 색상
            width(5f)  // 선의 두께
            add(from)  // 시작 위치 추가
            add(to)    // 끝 위치 추가
        }

        // Polyline을 지도에 추가
        googleMap.addPolyline(polylineOptions)
        Log.d(TAG, "Line drawn between $from and $to")
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun startLocationRequest() {
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    // 마지막 위치 정보 가져오기
//    private fun getLastLocation() {
//        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
//            if (location != null) {
//                lat = location.latitude
//                lng = location.longitude
//                Log.d(TAG, "마지막 위치 -> 위도: $lat, 경도: $lng")
//            } else {
//                Log.d(TAG, "위치 정보를 가져올 수 없습니다.")
//            }
//        }
//        fusedLocationClient.lastLocation.addOnFailureListener { e: Exception ->
//            Log.d(TAG, "위치 정보 실패: ${e.toString()}")
//        }
//    }

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