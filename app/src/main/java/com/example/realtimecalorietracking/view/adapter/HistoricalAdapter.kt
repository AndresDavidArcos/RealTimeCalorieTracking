import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.realtimecalorietracking.R
import com.example.realtimecalorietracking.model.FoodRegistered
import com.example.realtimecalorietracking.model.Goal


class HistoricalAdapter(private val historicalList: List<Pair<FoodRegistered, Goal>>) :
    RecyclerView.Adapter<HistoricalAdapter.HistoricalViewHolder>() {

    inner class HistoricalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        val caloriesConsumedTextView: TextView = itemView.findViewById(R.id.caloriesConsumedTextView)
        val differenceTextView: TextView = itemView.findViewById(R.id.differenceTextView)
        val goalCaloriesTextView: TextView = itemView.findViewById(R.id.goalCaloriesTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoricalViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_historical, parent, false)
        return HistoricalViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HistoricalViewHolder, position: Int) {
        val (foodRegistered, goal) = historicalList[position]

        holder.dateTextView.text = foodRegistered.dateRegistered.toString()
        holder.caloriesConsumedTextView.text = "Calorías consumidas: ${foodRegistered.calories}"
        val difference = foodRegistered.calories - goal.calories
        holder.differenceTextView.text = "Diferencia: $difference"
        holder.goalCaloriesTextView.text = "Meta de calorías: ${goal.calories}"
    }

    override fun getItemCount() = historicalList.size
}
