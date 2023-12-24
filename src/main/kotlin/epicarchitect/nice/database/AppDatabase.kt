package epicarchitect.nice.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [TaskEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract val tasksDao: TaskDao

    object Factory {
        fun create(context: Context) = Room.databaseBuilder(
            context = context,
            klass = AppDatabase::class.java,
            name = "epic.db",
        ).build()
    }
}
