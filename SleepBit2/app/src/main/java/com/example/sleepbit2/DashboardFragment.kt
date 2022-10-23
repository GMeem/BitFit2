package com.example.sleepbit2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DashboardFragment : Fragment() {

    private val sleeps = mutableListOf<Sleep>()

    private lateinit var avgSleep: TextView
    private lateinit var minSleep: TextView
    private lateinit var maxSleep: TextView
    private lateinit var clearData: Button



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
        clearData = view.findViewById(R.id.clearBtn)

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
        clearData.setOnClickListener{
            lifecycleScope.launch(Dispatchers.IO){
                (activity?.application as SleepApplication).db.sleepDao().deleteAll()
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
            var avg = total.toDouble()/sleeps.count()
            avg = String.format("%.1f", avg).toDouble()
            avgSleep.text = "$avg hours"
            minSleep.text = "$min hours"
            maxSleep.text = "$max hours"
        }else{
            avgSleep.text = "0 hours"
            minSleep.text = "0 hours"
            maxSleep.text = "0 hours"
        }
    }

    companion object {
        fun newInstance(): DashboardFragment {
            return DashboardFragment()
        }
    }


}