package ddwu.com.mobileapplication.finalproject.data.network

import android.content.Context
import android.util.Log
import ddwu.com.mobileapplication.finalproject.R
import ddwu.com.mobileapplication.finalproject.data.Medical
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

class NetworkService(private val context: Context) {
    val TAG = "NetworkService"
    private val mdService : MDSearch

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("${context.resources.getString(R.string.md_url)}")
            .addConverterFactory( SimpleXmlConverterFactory.create() )
            .build()

        mdService = retrofit.create(MDSearch::class.java)
    }
    suspend fun getMedical(instit_nm: String) : List<Medical> {
        val serviceKey = context.resources.getString(R.string.serviceKey)
        val numOfRows = context.resources.getString(R.string.numOfRows)
        val pageNo = context.resources.getString(R.string.pageNo)

        //Log.d(TAG, "NetworkService instit_nm: ${instit_nm}")
        val mdRoot = mdService.getMedicalInstitutions(serviceKey, numOfRows, pageNo, instit_nm)
        //Log.d(TAG, "넽웤NetworkService: ${mdRoot}")
        if (mdRoot.body?.items?.mdList == null) {
            Log.d(TAG, "널이구나... Received null mdList")
        }

        return mdRoot.body?.items?.mdList ?: emptyList()
    }
}