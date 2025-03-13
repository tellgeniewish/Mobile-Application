package ddwu.com.mobileapplication.finalproject.data

import ddwu.com.mobileapplication.finalproject.data.network.NVLoc
import ddwu.com.mobileapplication.finalproject.data.network.NVService

class NVRepository (private val nvService: NVService) {

    suspend fun getNVLocs(query: String, id: String, secret: String) : List<NVLoc>? {
        return nvService.getNVLocs(query, id, secret)
    }
}