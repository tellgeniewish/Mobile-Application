package ddwu.com.mobileapplication.finalproject.data.network

import ddwu.com.mobileapplication.finalproject.data.TMapRoot
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface TMapSearch {
    // API 호출
    @GET("tmap/pois/search/around")
    suspend fun getTMaps(
        @Header("accept") accept: String,
        @Header("appKey") appKey: String,
        @Query("version") version: String,
        @Query("centerLon") centerLon: String,
        @Query("centerLat") centerLat: String,
        @Query("categories") categories: String,
        @Query("page") page: String,
        @Query("count") count: String,
        @Query("radius") radius: String,
        @Query("reqCoordType") reqCoordType: String,
        @Query("resCoordType") resCoordType: String,
        @Query("multiPoint") multiPoint: String
    ): TMapRoot
}