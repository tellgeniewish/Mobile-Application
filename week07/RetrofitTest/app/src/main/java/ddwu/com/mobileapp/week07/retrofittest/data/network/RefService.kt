package ddwu.com.mobileapp.week07.retrofittest.data.network

import android.content.Context
import android.util.Log
import ddwu.com.mobileapp.week07.retrofittest.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RefService(val context: Context) {
    val TAG = "RefService"
    val movieService: IBoxOfficeService

    init {
        val retrofit : Retrofit = Retrofit.Builder()
                                  .baseUrl(context.resources.getString(R.string.kobis_url))
                                  .addConverterFactory(GsonConverterFactory.create())
                                  .build()


        movieService = retrofit.create(IBoxOfficeService::class.java) // IBoxOfficeService의 객체가 만들어짐
    }

    suspend fun getMovies(key: String, date: String)  : List<Movie>?   {
        // 응답이 왔을 때 호출되는 부분
//        val movieCallback = object : Callback<Root> {
//            // 응답이 제대로 왔을 때 호출됨
//            override fun onResponse(call: Call<Root>, response: Response<Root>) {
//                if (response.isSuccessful) {
//                    val boxOfficeRoot = response.body() // body를 확인하면 Root가 나온다
//                    val movies = boxOfficeRoot?.movieResult?.movieList //movies
//                    movies?.forEach { movie ->
//                        Log.d(TAG, movie.toString())
//                    }
//                }
//            }
//            // 응답이 제대로 안 왔을 때 호출됨
//            override fun onFailure(call: Call<Root>, t: Throwable) {
//                Log.d(TAG, t.stackTraceToString())
//            }
            val root: Root = movieService.getDailyBoxOffice("json", key, date)
            return root.movieResult.movieList
//        }

//        val movieCall : Call <Root> = movieService.getDailyBoxOffice("json", key, date)/* IBoxOfficeService 의 함수 호출 */
//        movieCall.enqueue(movieCallback) //비동기
        // val response = movieCall.execute() // 동기: 계속 기다리기 때문에 콜백 필요없음

//        return null // response.body()?.boxOfficeResult?.boxOfficeList
    }

}