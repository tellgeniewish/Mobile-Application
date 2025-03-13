package ddwu.com.mobileapp.week02.fooddbexam_room.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database (entities = [Food::class], version=1) // 테이블이 여러 개 있을 수 있어서 entities
// 클래스 추가 후 abstract 적으면 됨
abstract class FoodDatabase : RoomDatabase() { // RoomDatabase()를 상속받아야 함
    abstract fun foodDao(): FoodDao

    // 싱글톤 패턴: 객체가 하나만 만들어진다
    companion object { // static -> 객체 안 만들고 class.으로 호출 ㄱㄴ
        @Volatile
        private var INSTANCE: FoodDatabase? = null // 자기 자신을 저장함

        fun getDatabase(context: Context): FoodDatabase {
            return INSTANCE ?: synchronized(this) { // null이면 ?: 뒷부분 실행
                val instance = Room.databaseBuilder(
                    context.applicationContext, FoodDatabase::class.java, "food_db"
                ).build()
                INSTANCE = instance // 정적 변수에 보관
                instance
            }
        }
    }
}