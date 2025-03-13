package ddwu.com.mobileapplication.finalproject.data

import ddwu.com.mobileapplication.finalproject.data.network.NetworkService

class MDRepository(private val netService: NetworkService) {
    suspend fun showMedical(instit_nm: String) : List<Medical> {
        return netService.getMedical(instit_nm)
    }
}