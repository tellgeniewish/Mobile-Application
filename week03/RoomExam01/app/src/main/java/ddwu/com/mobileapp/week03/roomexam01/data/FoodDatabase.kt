package ddwu.com.mobile.roomexam01.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database (entities = [Food::class], version = 1) // entities인 이유: 테이블이 여러 개일 수도 있다
abstract class FoodDatabase : RoomDatabase() {

    abstract fun foodDao(): FoodDao

    // singleton
    companion object {
        @Volatile
        private var INSTANCE: FoodDatabase? = null

        fun getDatabase(context: Context): FoodDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, FoodDatabase::class.java, "food_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}