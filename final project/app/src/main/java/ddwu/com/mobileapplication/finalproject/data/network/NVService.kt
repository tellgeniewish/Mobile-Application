package ddwu.com.mobileapplication.finalproject.data.network

import android.content.Context
import ddwu.com.mobileapplication.finalproject.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NVService (val context: Context) {
    private val TAG = "NVService"
    private val service : NaverLocSearch

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(context.resources.getString(R.string.naver_url))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(NaverLocSearch::class.java)
    }


    suspend fun getNVLocs(query: String, clientID: String, clientSecret: String) : List<NVLoc>? {
        val root: NaverRoot = service.getLocs(query, clientID, clientSecret)
        return root.items
        //return null
    }
}