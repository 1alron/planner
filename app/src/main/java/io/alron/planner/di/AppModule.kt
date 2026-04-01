package io.alron.planner.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.alron.planner.data.TaskRepositoryImpl
import io.alron.planner.data.local.TaskDao
import io.alron.planner.data.local.TaskDatabase
import io.alron.planner.data.local.TaskDatabaseCallback
import io.alron.planner.domain.TaskRepository
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideDatabase(app: Application): TaskDatabase {
        return Room.databaseBuilder(
            app,
            TaskDatabase::class.java,
            "task_db"
        )
            .addCallback(TaskDatabaseCallback())
            .build()
    }

    @Provides
    @Singleton
    fun provideTaskDao(db: TaskDatabase) = db.taskDao()

    @Provides
    @Singleton
    fun provideTaskRepository(dao: TaskDao): TaskRepository {
        return TaskRepositoryImpl(dao)
    }
}
