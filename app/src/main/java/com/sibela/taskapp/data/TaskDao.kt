package com.sibela.taskapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    fun getTasks(query: String, sortOrder: SortOrder, hideCompleted: Boolean) : Flow<List<Task>> {
        return when(sortOrder) {
            SortOrder.BY_DATE -> getTasksSearchByDateCreated(query, hideCompleted)
            SortOrder.BY_NAME -> getTasksSearchByName(query, hideCompleted)
        }
    }

    @Query("SELECT * FROM task_table WHERE (completed != :hideCompleted OR completed = 0) AND name LIKE '%' || :searchQuery || '%' ORDER BY important DESC, name")
    fun getTasksSearchByName(searchQuery: String, hideCompleted: Boolean): Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE (completed != :hideCompleted OR completed = 0) AND name LIKE '%' || :searchQuery || '%' ORDER BY important DESC, created")
    fun getTasksSearchByDateCreated(searchQuery: String, hideCompleted: Boolean): Flow<List<Task>>

    @Query("DELETE FROM task_table WHERE (completed = 1)")
    suspend fun deleteCompletedTasks()

}