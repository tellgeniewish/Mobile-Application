package ddwu.com.mobileapp.week07.naverretrofit.data.network

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

//  @Get:  v1/search/book.json
//  @Header:  X-Naver-Client-Id
//  @Header:  X-Naver-Client-Secret
//  @Query:   query

interface INaverBookSearch {
    @GET("v1/search/book.json")

    suspend fun getBooks(
        @Query("query") query: String,
        @Header("X-Naver-Client-Id") clientID: String,
        @Header("X-Naver-Client-Secret") clientSecret: String,
    ): BookRoot


}