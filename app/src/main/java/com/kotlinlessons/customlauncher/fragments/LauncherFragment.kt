package com.kotlinlessons.customlauncher.fragments

import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kotlinlessons.customlauncher.R
import java.util.*

class LauncherFragment: Fragment() {

    companion object {
        private const val TAG = "LauncherFragment"

        fun newInstance(): LauncherFragment = LauncherFragment()
    }

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_launcher, container, false)
        recyclerView = view.findViewById(R.id.app_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        setupAdapter()
        return view
    }

    private fun setupAdapter() {
        val startupIntent = Intent(Intent.ACTION_MAIN)
        startupIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        val pm = activity!!.packageManager
        val activities = pm.queryIntentActivities(startupIntent, 0)
        activities.sortWith(Comparator<ResolveInfo> { a, b ->
            String.CASE_INSENSITIVE_ORDER.compare(
                a.loadLabel(pm).toString(),
                b.loadLabel(pm).toString())
        })
        Log.i(TAG, "Found ${activities.size} activities.")
        recyclerView.adapter = ActivityAdapter(activities)
    }

    private inner class ActivityHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener {
        private lateinit var resolveInfo: ResolveInfo
        private var nameTextView: TextView = view as TextView

        fun bindActivity(resolveInfo: ResolveInfo) {
            this.resolveInfo = resolveInfo
            val pm = activity!!.packageManager
            val appName = resolveInfo.loadLabel(pm).toString()
            nameTextView.text = appName
            nameTextView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val activityInfo = resolveInfo.activityInfo
            val intent = Intent(Intent.ACTION_MAIN)
                .setClassName(activityInfo.applicationInfo.packageName, activityInfo.name)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private inner class ActivityAdapter(activities: List<ResolveInfo>): RecyclerView.Adapter<ActivityHolder>() {
        private var activities = activities

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityHolder {
            val layoutInflater = LayoutInflater.from(activity)
            val view = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false)
            return ActivityHolder(view)
        }

        override fun getItemCount(): Int = activities.size

        override fun onBindViewHolder(holder: ActivityHolder, position: Int) {
            val resolveInfo = activities[position]
            holder.bindActivity(resolveInfo)
        }

    }

}