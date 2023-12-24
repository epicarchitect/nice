package epicarchitect.nice.tasks

import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import epicarchitect.nice.databinding.TaskItemBinding
import epicarchitect.nice.databinding.TasksScreenBinding
import epicarchitect.recyclerview.EpicAdapter
import epicarchitect.recyclerview.bind
import epicarchitect.recyclerview.requireEpicAdapter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

interface TasksScreen {
    fun view(): View
}

class DefaultTasksScreen(
    private val tasks: Tasks,
    private val lifecycleOwner: LifecycleOwner,
    private val layoutInflater: LayoutInflater
) : TasksScreen {
    override fun view(): View {
        val binding = TasksScreenBinding.inflate(layoutInflater)

        binding.imageButtonSave.setOnClickListener {
            lifecycleOwner.lifecycleScope.launch {
                tasks.add(binding.editTextContent.text.toString())
            }
        }

        binding.recyclerViewTasks.adapter = EpicAdapter(lifecycleOwner.lifecycleScope) {
            setup<Task, TaskItemBinding>(TaskItemBinding::inflate) {
                init { item ->
                    imageButtonDelete.setOnClickListener {
                        lifecycleOwner.lifecycleScope.launch {
                            item.value.delete()
                        }
                    }
                }
                bind { item ->
                    textViewContent.text = item.content()
                }
            }
        }

        tasks.allFlow().onEach {
            binding.recyclerViewTasks.requireEpicAdapter().loadItems(it)
        }.launchIn(lifecycleOwner.lifecycleScope)

        return binding.root
    }
}