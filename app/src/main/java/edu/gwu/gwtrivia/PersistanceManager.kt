package edu.gwu.gwtrivia

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.provider.SyncStateContract
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gwu.trivia.Constants
import edu.gwu.gwtrivia.model.Score

/**
 * Created by hoho on 2017-10-02.
 */
class PersistanceManager(context: Context) {
    val sharedPreferences: SharedPreferences

    init {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    }
    fun fetchScores(): List<Score> {
        var scoresJson = sharedPreferences.getString(Constants.SCORES_PREF_KEY, null)
        if(scoresJson == null) {
            return arrayListOf<Score>()
        }
        else{
            val scoreType = object : TypeToken<MutableList<Score>>() {}.type
            return  Gson().fromJson(scoresJson, scoreType)
        }
    }
    fun saveScore(score: Score) {
        val scores = fetchScores().toMutableList()
        scores.add(score)
        val editor = sharedPreferences.edit()
        editor.putString(Constants.SCORES_PREF_KEY, Gson().toJson(scores))
        editor.apply()
    }

    fun highScore(): Score? {
        val scores = fetchScores()
        return scores.sortedByDescending { it.score }.firstOrNull()
      }

}