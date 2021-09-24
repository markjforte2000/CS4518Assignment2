package wpi.mjforte.cs4518assignment2

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

const val TAG = "WPIGameListFragment"

class GameListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private val gameListViewModel : GameListViewModel by viewModels()
    private var adapter : GameAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Loading ${gameListViewModel.games.size} games")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game_list, container, false)
        recyclerView = view.findViewById(R.id.game_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        updateUI()
        return view
    }

    private fun updateUI() {
        val games = gameListViewModel.games
        adapter = GameAdapter(games)
        recyclerView.adapter = adapter
    }

    private inner class GameHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateLabel = view.findViewById<TextView>(R.id.gameDate)
        val teamScores = view.findViewById<TextView>(R.id.teamScores)
        val imageScores = view.findViewById<ImageView>(R.id.teamImage)
    }

    private inner class GameAdapter(var games: List<BasketballGame>) : RecyclerView.Adapter<GameHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameHolder {
            val view = layoutInflater.inflate(R.layout.fragment_list_item_game, parent, false)
            return GameHolder(view)
        }

        override fun onBindViewHolder(holder: GameHolder, position: Int) {
            val game = games[position]
            holder.apply {
                Log.d(TAG, "Loading game ${game.id}")
                holder.dateLabel.text = "Game: ${game.id}\t${game.time}"
                holder.teamScores.text = "${game.teamAName}:${game.teamAScore}\t${game.teamBName}:${game.teamBScore}"
                if (game.teamAScore > game.teamBScore) {
                    holder.imageScores.setImageResource(R.mipmap.ic_capybara)
                } else {
                    holder.imageScores.setImageResource(R.mipmap.ic_qokka)
                }
            }
        }

        override fun getItemCount(): Int {
            return games.size
        }

    }

}