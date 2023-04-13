package id.perqara.testing_perqara.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Games::class], exportSchema = false, version = 1)
abstract class GamesDatabase : RoomDatabase() {
    abstract fun gamesDao(): GamesDao?

    companion object {
        private const val DB_NAME = "games_db"
        private var instance: GamesDatabase? = null
        @Synchronized
        fun getInstance(context: Context): GamesDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    GamesDatabase::class.java, DB_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance
        }
    }
}