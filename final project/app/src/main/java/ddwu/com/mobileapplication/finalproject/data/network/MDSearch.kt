package ddwu.com.mobileapplication.finalproject.data.network

import ddwu.com.mobileapplication.finalproject.data.Root
import retrofit2.http.GET
import retrofit2.http.Query

interface MDSearch {
    // API 호출
    @GET("6260000/MedicInstitService/MedicalInstitInfo")
    suspend fun getMedicalInstitutions(
        @Query("serviceKey") serviceKey: String,
        @Query("numOfRows") numOfRows: String,
        @Query("pageNo") pageNo: String,
        @Query("instit_nm") instit_nm: String
    ): Root
}