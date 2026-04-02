package io.alron.planner.data

import io.alron.planner.data.local.TaskEntity
import io.alron.planner.domain.Task

fun Task.toEntity() = TaskEntity(
    id = id,
    name = name,
    description = description,
    date_start = dateStart,
    date_finish = dateFinish
)

fun TaskEntity.toDomain() = Task(
    id = id,
    name = name,
    description = description,
    dateStart = date_start,
    dateFinish = date_finish
)