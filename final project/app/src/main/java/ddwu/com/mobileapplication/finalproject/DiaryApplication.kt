//package ddwu.com.mobileapplication.finalproject
//
//import android.app.Application
//import ddwu.com.mobileapplication.finalproject.data.DiaryDatabase
//import ddwu.com.mobileapplication.finalproject.data.DiaryRepository
//
//class DiaryApplication: Application() {
//    val diaryDatabase by lazy {
//        DiaryDatabase.getDatabase(this)
//    }
//
//    val diaryRepo by lazy {
//        DiaryRepository(diaryDatabase.diaryDao())
//    }
//}