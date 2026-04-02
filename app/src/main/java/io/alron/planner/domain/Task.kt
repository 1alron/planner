package io.alron.planner.domain

data class Task(
    val id: Int,
    val name: String,
    val dateStart: Long,
    val dateFinish: Long,
    val description: String
)
