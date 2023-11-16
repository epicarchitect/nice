package epicarchitect.nice

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import epicarchitect.nice.databinding.MainActivityBinding
import epicarchitect.nice.databinding.TaskItemBinding
import epicarchitect.recyclerview.EpicAdapter
import epicarchitect.recyclerview.bind
import epicarchitect.recyclerview.requireEpicAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.random.Random

object DI {
    val database: Database = NiceDatabase()
}

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageButtonSave.setOnClickListener {
            SaveTask(
                database = DI.database,
                coroutineScope = lifecycleScope,
                content = binding.editTextContent.text.toString()
            ).execute()
        }

        binding.recyclerViewTasks.adapter = EpicAdapter {
            setup<Task, TaskItemBinding>(TaskItemBinding::inflate) {
                bind { item ->
                    textViewContent.text = item.content
                    imageButtonDelete.setOnClickListener {
                        DeleteTask(
                            database = DI.database,
                            coroutineScope = lifecycleScope,
                            taskId = item.id
                        ).execute()
                    }
                }
            }
        }

        GetAllTasks(DI.database).execute().onEach {
            binding.recyclerViewTasks.requireEpicAdapter().loadItems(it)
        }.launchIn(lifecycleScope)
    }
}

data class Task(
    val id: Int,
    val content: String
)

class SaveTask(
    val database: Database,
    val coroutineScope: CoroutineScope,
    val content: String
) {
    fun execute() {
        coroutineScope.launch {
            database.saveTask(
                Task(
                    id = 0,
                    content = content
                )
            )
        }
    }
}

class DeleteTask(
    val database: Database,
    val coroutineScope: CoroutineScope,
    val taskId: Int
) {
    fun execute() {
        coroutineScope.launch {
            database.deleteTaskById(taskId)
        }
    }
}

class GetAllTasks(
    val database: Database
) {
    fun execute() = database.getTasks()
}

interface Database {
    fun getTasks(): Flow<List<Task>>
    suspend fun saveTask(task: Task)
    suspend fun deleteTaskById(id: Int)
}

class NiceDatabase : Database {
    private val random = Random(0)
    private val state = MutableStateFlow(emptyList<Task>())

    override fun getTasks(): Flow<List<Task>> = state

    override suspend fun saveTask(task: Task) {
        state.value += task.copy(id = random.nextInt())
    }

    override suspend fun deleteTaskById(id: Int) {
        state.value = state.value.filter { it.id != id }
    }
}