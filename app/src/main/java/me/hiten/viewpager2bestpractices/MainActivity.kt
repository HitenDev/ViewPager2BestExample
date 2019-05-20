package me.hiten.viewpager2bestpractices

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRefreshLayout()
        initBannerLayout()
        setupAdapter()
        initTabLayout()
        resolveScrollConflict()
    }

    private fun resolveScrollConflict() {
        val appbarListenerBehavior = AppbarSteadyBehavior(this)
        val layoutParams = (appBarLayout.layoutParams as CoordinatorLayout.LayoutParams)
        layoutParams.behavior = appbarListenerBehavior
        appbarListenerBehavior.setOnBehaviorListener {
            if (it == AppbarSteadyBehavior.State.START) {
                view_pager2.isUserInputEnabled = false
            } else if (it == AppbarSteadyBehavior.State.STOP) {
                view_pager2.isUserInputEnabled = true
            }
        }

    }

    private fun initTabLayout() {
        TabLayoutMediator(tabLayout, view_pager2,
            TabLayoutMediator.OnConfigureTabCallback { tab, position ->
                tab.text = "tab$position"
            }).attach()
    }

    private fun setupAdapter() {
        view_pager2.adapter = object : FragmentStateAdapter(this) {
            override fun getItem(position: Int): Fragment {
                return InfoListFragment.create(position)
            }

            override fun getItemCount(): Int {
                return 10
            }

        }

    }

    private fun initBannerLayout() {
        banner.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val textView = TextView(parent.context)
                textView.gravity = Gravity.CENTER
                textView.layoutParams =
                    ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                return object : RecyclerView.ViewHolder(textView) {}
            }

            override fun getItemCount(): Int {
                return 10
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                (holder.itemView as TextView).text = "banner:$position"
            }

        }
    }

    private fun initRefreshLayout() {
        smartRefreshLayout.setRefreshHeader(ClassicsHeader(this))
        smartRefreshLayout.setOnRefreshListener {
            smartRefreshLayout.postDelayed({
                smartRefreshLayout.finishRefresh()
            }, 1000)
        }
        smartRefreshLayout.setEnableOverScrollBounce(false)
    }
}
