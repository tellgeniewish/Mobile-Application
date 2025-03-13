package ddwu.com.mobileapplication.finalproject.data.network

import android.content.Context
import android.util.Log
import ddwu.com.mobileapplication.finalproject.R
import ddwu.com.mobileapplication.finalproject.data.Poi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TMapNetworkService(private val context: Context) {
    val TAG = "TMapNetworkService"
    private val tmService : TMapSearch

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("${context.resources.getString(R.string.tmap_url)}")
            .addConverterFactory( GsonConverterFactory.create() )
            .build()

        tmService = retrofit.create(TMapSearch::class.java)
    }
    suspend fun getTMaps(categories: String, lat: Double, lng: Double) : List<Poi> {
        Log.d(TAG, "getTMaps(categories:${categories}")
        val version = context.resources.getString(R.string.version)

//        val centerLon = context.resources.getString(R.string.centerLon)
//        val centerLat = context.resources.getString(R.string.centerLat)
        val centerLon = lng.toString()
        val centerLat = lat.toString()
        val reqCoordType = context.resources.getString(R.string.reqCoordType)
        val resCoordType = context.resources.getString(R.string.resCoordType)

        val page = context.resources.getString(R.string.page)
        val count = context.resources.getString(R.string.count)
        val radius = context.resources.getString(R.string.radius)

        val multiPoint = context.resources.getString(R.string.multiPoint)
        val accept = context.resources.getString(R.string.accept)
        val appKey = context.resources.getString(R.string.appKey)

        Log.d(TAG, "API Request - version: $version, centerLon: $centerLon, centerLat: $centerLat, categories: $categories, page: $page, count: $count, radius: $radius, reqCoordType: $reqCoordType, resCoordType: $resCoordType, multiPoint: $multiPoint, accept: $accept, appKey: $appKey")
        return try {
        val tmRoot = tmService.getTMaps(
            accept,
            appKey,
            version,
            centerLon,
            centerLat,
            categories,
            page,
            count,
            radius,
            reqCoordType,
            resCoordType,
            multiPoint
        )

//        if (tmRoot.searchPoiInfo.pois.poi == null) {
//            Log.d(TAG, "TMapNetworkService널이구나... Received null mdList")
//        }
//        Log.d(TAG, "tmRoot.searchPoiInfo.pois.poi: ${tmRoot.searchPoiInfo.pois.poi}")

        val pois = tmRoot.searchPoiInfo.pois.poi
        Log.d(TAG, "tmRoot.searchPoiInfo.pois.poi: $pois")
        pois
        } catch (e: Exception) {
            Log.e(TAG, "API 호출 중 오류 발생: ${e.message}")
            emptyList()
        }
    }
}