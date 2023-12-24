package epicarchitect.nice

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import epicarchitect.nice.tasks.DefaultTasksScreen
import epicarchitect.nice.tasks.LocalTasks

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tasksUi = DefaultTasksScreen(
            tasks = LocalTasks(app.database),
            lifecycleOwner = this,
            layoutInflater = layoutInflater
        )

        setContentView(tasksUi.view())
    }
}
