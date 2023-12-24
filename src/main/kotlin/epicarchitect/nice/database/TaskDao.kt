package epicarchitect.nice.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT content FROM tasks WHERE id = :id")
    suspend fun content(id: Int): String

    @Query("SELECT id FROM tasks")
    fun allIdsFlow(): Flow<List<Int>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun task(id: Int): TaskEntity

    @Insert
    suspend fun insert(taskEntity: TaskEntity)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun delete(id: Int)
}
