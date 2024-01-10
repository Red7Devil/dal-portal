package com.example.dalportal.ui.ta_portal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dalportal.R
import com.example.dalportal.ui.ta_portal.firebase.TaTasksModel
import com.example.dalportal.ui.ta_portal.tasks.TasksAdapter
import com.example.dalportal.util.UserData
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar
import java.util.Date
import java.util.Locale

class TaPortalHomeFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    private lateinit var recyclerView: RecyclerView
    private val adapter = TasksAdapter()
    private var limit: Int = 3;
    private lateinit var showMoreTasksButton: Button
    private lateinit var showLessTasksButton: Button
    private lateinit var tasksEmptyTextView: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ta_portal_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.tasksRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        tasksEmptyTextView = view.findViewById(R.id.tasksEmpty)
        showMoreTasksButton = view.findViewById(R.id.showMoreTasks)
        showLessTasksButton = view.findViewById(R.id.showLessTasks)

        showMoreTasksButton.setOnClickListener {
            limit = 50
            getTaTasks()
        }

        showLessTasksButton.setOnClickListener {
            limit = 3
            getTaTasks()
        }

        getTaTasks()


    }

    private fun getTaTasks() {
        val collectionRef = db.collection("TA_tasks")

        collectionRef.whereEqualTo("assignedTo", UserData.id).get()
            .addOnSuccessListener { documents ->
                var tasks: MutableList<TaTasksModel> = mutableListOf()

                if (documents.isEmpty) {
                    tasksEmptyTextView.visibility = View.VISIBLE
                    showMoreTasksButton.visibility = View.GONE
                    adapter.updateTasks(tasks)
                } else {
                    tasksEmptyTextView.visibility = View.GONE
                    for (document in documents) {
                        val task: TaTasksModel = document.toObject(TaTasksModel::class.java)
                        task.id = document.id
                        tasks.add(task)
                    }
                    val sortedTasks = sortTasks(tasks)
                    val filteredTasks = sortedTasks.take(limit)

                    if (sortedTasks.size > limit) {
                        showMoreTasksButton.visibility = View.VISIBLE
                        showLessTasksButton.visibility = View.GONE
                    } else if (sortedTasks.size > 3) {
                        showMoreTasksButton.visibility = View.GONE
                        showLessTasksButton.visibility = View.VISIBLE
                    }
                    adapter.updateTasks(filteredTasks)
                }


            }.addOnFailureListener {
                Log.d("TA_Tasks", it.toString())

            }
    }

    private fun sortTasks(tasks: List<TaTasksModel>): List<TaTasksModel> {
        val priorities = view?.context?.resources?.getStringArray(R.array.priority_levels)
        val priorityMap = mapOf(
            (priorities?.get(0) ?: "High") to 1,
            (priorities?.get(1) ?: "Medium") to 2,
            (priorities?.get(2) ?: "Low") to 3
        )

        val threeDaysAgo = Calendar.getInstance().apply {
            add(Calendar.DATE, -3)
        }.time

        Log.d("TA_tasks", tasks.toString())
        val sortedTasks =
            tasks.filter { it.deadline?.after(threeDaysAgo) == true } // Filter out tasks with deadline before 3 days ago
                .sortedWith(compareBy<TaTasksModel> {
                    when (it.status.lowercase()) {
                        "in_progress" -> 0
                        "completed" -> 2
                        else -> 1
                    } // Completed tasks will be sorted to the end
                }.thenBy {
                    it.deadline ?: Date(Long.MAX_VALUE) // Handle null deadlines
                }.thenBy {
                    priorityMap[it.priority.lowercase()] ?: Int.MAX_VALUE // Handle priorities
                })

        return sortedTasks
    }
}