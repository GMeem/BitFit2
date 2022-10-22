package com.example.sleepbit2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class LogFragment : Fragment() {

    private var sleeps = mutableListOf <Sleep>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_log, container, false)
        val sleepsRv = view.findViewById<RecyclerView>(R.id.sleepsRv)
        val adapter = SleepAdapter(sleeps)

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
                    adapter.notifyDataSetChanged()
                }
            }
        }

        sleepsRv.adapter = adapter
        sleepsRv.layoutManager = LinearLayoutManager(context)

        return view
    }

    companion object {
        fun newInstance(): LogFragment {
            return LogFragment()
        }
    }
}