package io.alron.planner.data.local

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import java.time.LocalDate
import java.time.ZoneId

class TaskDatabaseCallback : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        val zone = ZoneId.systemDefault()
        val today = LocalDate.now()

        val tasks = listOf(
            Triple(
                "Утренний созвон",
                "Утренний созвон по поводу улучшения пользовательского опыта",
                9 to 10
            ),
            Triple(
                "Работа над проектом",
                "Внесение правок по техническому заданию",
                14 to 16
            )
        )

        tasks.forEach { (name, description, hours) ->
            val (startHour, endHour) = hours
            val start = today.atTime(startHour, 0).atZone(zone).toInstant().toEpochMilli()
            val end = today.atTime(endHour, 0).atZone(zone).toInstant().toEpochMilli()
            db.insertTask(name, description, start, end)
        }
    }
}

private fun SupportSQLiteDatabase.insertTask(
    name: String,
    description: String,
    start: Long,
    end: Long
) {
    execSQL(
        "INSERT INTO tasks (name, description, date_start, date_finish) VALUES (?, ?, ?, ?)",
        arrayOf<Any?>(name, description, start, end)
    )
}