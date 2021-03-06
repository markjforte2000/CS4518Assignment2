package wpi.mjforte.cs4518assignment2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

const val TAG = "WPIGameListFragment"
private val WINNING_TEAM_NAME : String = "wpi.mjforte.cs4518assignment2.winning_team_name"

class GameListFragment : Fragment() {

    companion object {
        fun newBundle(winningTeam: String): Bundle {
            val bundle = Bundle()
            bundle.putString(WINNING_TEAM_NAME, winningTeam)
            return bundle
        }
        fun getWinningTeamName(bundle: Bundle): String {
            return bundle.getString(WINNING_TEAM_NAME, "Team A")
        }
    }

    private lateinit var recyclerView: RecyclerView
    private val gameListViewModel : GameListViewModel by viewModels()
    private var adapter : GameAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game_list, container, false)
        recyclerView = view.findViewById(R.id.game_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameListViewModel.gameListLiveData.observe(
            viewLifecycleOwner,
            { games ->
                games?.let {
                    Log.i(TAG, "Got games: ${games.size}")
                    if (arguments != null) {
                        updateUI(filterGames(getWinningTeamName(requireArguments()), games))
                    } else {
                        updateUI(games)
                    }
                }
            }
        )
        Log.d(TAG, "Winning Team: ${arguments?.let { getWinningTeamName(it) }}")
    }

    private fun filterGames(winningTeam: String, games: List<BasketballGame>): List<BasketballGame> {
        val filteredGames = mutableListOf<BasketballGame>()
        games.forEach { game ->
            if (game.teamAName == winningTeam && game.teamAScore > game.teamBScore ||
                    game.teamBName == winningTeam && game.teamBScore > game.teamAScore) {
                filteredGames.add(game)
            }
        }
        return filteredGames
    }

    private fun updateUI(games: List<BasketballGame>) {
        adapter = GameAdapter(games)
        recyclerView.adapter = adapter
    }

    private inner class GameHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateLabel = view.findViewById<TextView>(R.id.gameDate)
        val teamScores = view.findViewById<TextView>(R.id.teamScores)
        val imageScores = view.findViewById<ImageView>(R.id.teamImage)
        var game: BasketballGame? = null
        init {
            view.setOnClickListener {
                val fragment = MainFragment()
                val fg = activity!!.supportFragmentManager
                val args = game?.let { it1 -> MainFragment.newBundle(it1) }
                fragment.arguments = args
                fg.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit()
            }
        }
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
                holder.dateLabel.text = "Game: ${game.id}\t${game.date}"
                holder.teamScores.text = "${game.teamAName}:${game.teamAScore}\t${game.teamBName}:${game.teamBScore}"
                if (game.teamAScore > game.teamBScore) {
                    holder.imageScores.setImageResource(R.mipmap.ic_capybara)
                } else {
                    holder.imageScores.setImageResource(R.mipmap.ic_qokka)
                }
                holder.game = game
            }
        }

        override fun getItemCount(): Int {
            return games.size
        }

    }

}