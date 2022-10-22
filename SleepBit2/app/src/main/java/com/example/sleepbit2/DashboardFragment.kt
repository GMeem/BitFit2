package com.example.sleepbit2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.w3c.dom.Text


class DashboardFragment : Fragment() {

    private val sleeps = mutableListOf<Sleep>()

    private lateinit var avgSleep: TextView
    private lateinit var minSleep: TextView
    private lateinit var maxSleep: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        avgSleep = view.findViewById(R.id.avg_sleep)
        minSleep = view.findViewById(R.id.min_sleep)
        maxSleep = view.findViewById(R.id.max_sleep)

        lifecycleScope.launch {
            (activity?.application as SleepApplication).db.sleepDao().getAll().collect { databaseList ->
                databaseList.map { entity ->
                    Sleep(
                        entity.date,
                        entity.hours_slept,
                        entity.condition
                    )
                }.also { mappedList ->
                    sleeps.clear()
                    sleeps.addAll(mappedList)
                    calculateStats(sleeps)
                }
            }
        }
        return view
    }

    private fun calculateStats(sleeps: MutableList<Sleep>) {
        var total = 0
        var min = 10000
        var max = 0

        if (sleeps.isNotEmpty()){
            for (i in sleeps){
                var sleepHours =  i.hour.toInt()
                total += sleepHours
                if (sleepHours < min){
                    min = sleepHours
                }
                if (sleepHours > max){
                    max = sleepHours
                }
            }
        }

        var avg = total.toFloat()/sleeps.count()
        avgSleep.text = "$avg hours"
        minSleep.text = "$min hours"
        maxSleep.text = "$max hours"

    }

    companion object {
        fun newInstance(): DashboardFragment {
            return DashboardFragment()
        }
    }


}