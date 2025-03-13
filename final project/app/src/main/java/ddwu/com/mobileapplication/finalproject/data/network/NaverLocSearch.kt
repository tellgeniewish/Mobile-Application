package ddwu.com.mobileapplication.finalproject.data.network

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NaverLocSearch {
    @GET("v1/search/local.json")

    suspend fun getLocs(
        @Query("query") query: String,
        @Header("X-Naver-Client-Id") clientID: String,
        @Header("X-Naver-Client-Secret") clientSecret: String,
    ): NaverRoot
}