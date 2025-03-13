package ddwu.com.mobileapp.week07.retrofittest.data.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


// @Get:  kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json
// @Query:  key
// @Query:  targetDt

interface IBoxOfficeService {
    //@GET ("kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json")
    @GET ("kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.{type}")

    suspend fun getDailyBoxOffice(
        // path상의 값을 변경하고 싶을 때
        @Path("type") type: String,

        // 쿼리 부분은 매개변수로
        @Query("key") key: String, // import 시, Retrofit으로
        @Query("targetDt") targetDate: String
    ): Root //Call<Root> // 비동기 방식으로 요청 시 Retrofit에 해당하는 Call을 import


}