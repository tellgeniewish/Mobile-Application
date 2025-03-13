package ddwu.com.mobileapplication.finalproject

import android.app.Application
import ddwu.com.mobileapplication.finalproject.data.DiaryDatabase
import ddwu.com.mobileapplication.finalproject.data.MDRepository
import ddwu.com.mobileapplication.finalproject.data.network.NetworkService
import ddwu.com.mobileapplication.finalproject.data.DiaryRepository
import ddwu.com.mobileapplication.finalproject.data.NVRepository
//import ddwu.com.mobileapplication.finalproject.data.TMapRepository
import ddwu.com.mobileapplication.finalproject.data.network.NVService
import ddwu.com.mobileapplication.finalproject.data.network.TMapNetworkService

class MDApplication : Application() {
    val networkService by lazy {
        NetworkService(this)
    }
    val tmNetworkService by lazy {
        TMapNetworkService(this)
    }
    val networkRepo by lazy {
        MDRepository(networkService)
    }
//    val tmRepo by lazy {
//        TMapRepository(tmNetworkService)
//    }

    val nvService by lazy {
        NVService(this)
    }
    val nvRepository by lazy {
        NVRepository(nvService)
    }

    val diaryDatabase by lazy {
        DiaryDatabase.getDatabase(this)
    }
    val diaryRepo by lazy {
        DiaryRepository(diaryDatabase.diaryDao())
    }
}