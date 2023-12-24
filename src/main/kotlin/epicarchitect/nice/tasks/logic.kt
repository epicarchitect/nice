package epicarchitect.nice.tasks

import epicarchitect.nice.database.AppDatabase
import epicarchitect.nice.database.TaskEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

interface Task {
    suspend fun content(): String
    suspend fun delete()
}

interface Tasks {
    fun allFlow(): Flow<List<Task>>
    suspend fun task(id: Int): Task
    suspend fun add(content: String)
}

class LocalTasks(
    private val database: AppDatabase,
) : Tasks {
    override fun allFlow() = database.tasksDao.allIdsFlow().map {
        it.map { id ->
            LocalTask(id, database)
        }
    }

    override suspend fun task(id: Int) = withContext(Dispatchers.IO) {
        database.tasksDao.task(id).let {
            LocalTask(it.id, database)
        }
    }

    override suspend fun add(content: String) = withContext(Dispatchers.IO) {
        database.tasksDao.insert(
            TaskEntity(0, content),
        )
    }
}

class LocalTask(
    private val id: Int,
    private val database: AppDatabase,
) : Task {

    override suspend fun content() = withContext(Dispatchers.IO) {
        database.tasksDao.content(id)
    }

    override suspend fun delete() = withContext(Dispatchers.IO) {
        database.tasksDao.delete(id)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as LocalTask
        return id == other.id
    }

    override fun hashCode() = id
}