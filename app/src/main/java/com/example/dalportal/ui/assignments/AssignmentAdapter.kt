import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dalportal.ui.assignments.AssignmentDetailActivity
import com.example.dalportal.R
import com.example.dalportal.model.Assignment

// AssignmentAdapter.kt
class AssignmentAdapter(private var assignments: List<Assignment>) :
    RecyclerView.Adapter<AssignmentAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val assignmentDetails: TextView = view.findViewById(R.id.assignmentDetails)
        val completionStatus: TextView = view.findViewById(R.id.completionStatus)
        val score: TextView = view.findViewById(R.id.score)
        val feedback: TextView = view.findViewById(R.id.feedback)
        val deadline: TextView = view.findViewById(R.id.deadline)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.assignment_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val assignment = assignments[position]
        val assignmentDetails = "${assignment.name}"
        holder.assignmentDetails.text = assignmentDetails
        holder.completionStatus.text = assignment.completionStatus
        holder.score.text = assignment.score.toString()
        holder.feedback.text = assignment.feedback
        holder.deadline.text = "Due on ${assignment.deadline}"


        // Handle item click to open AssignmentDetailActivity
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, AssignmentDetailActivity::class.java).apply {
                putExtra("assignmentName", assignment.name)
                putExtra("assignmentDeadline", assignment.deadline)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return assignments.size
    }

    // Function to update the data in the adapter
    fun updateAssignments(newAssignments: List<Assignment>) {
        assignments = newAssignments
        notifyDataSetChanged()
    }
}
