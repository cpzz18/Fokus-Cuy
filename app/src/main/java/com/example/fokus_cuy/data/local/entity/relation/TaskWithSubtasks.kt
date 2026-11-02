package com.example.fokus_cuy.data.local.entity.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.fokus_cuy.data.local.entity.SubtaskEntity
import com.example.fokus_cuy.data.local.entity.TaskEntity

data class TaskWithSubtasks(
    @Embedded
    val task: TaskEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "task_id"
    )
    val subtasks: List<SubtaskEntity> = emptyList()
)
