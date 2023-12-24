package epicarchitect.nice

import android.app.Application
import android.content.Context
import epicarchitect.nice.database.AppDatabase

class App : Application() {
    val database by lazy {
        AppDatabase.Factory.create(this)
    }
}

val Context.app get() = applicationContext as App
