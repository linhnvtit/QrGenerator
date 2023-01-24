package com.linhnvt.qrgenerator.ui.viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.linhnvt.qrgenerator.R
import com.linhnvt.qrgenerator.base.BaseFragment
import com.linhnvt.qrgenerator.databinding.ViewPagerBinding
import com.linhnvt.qrgenerator.viewmodel.AppViewModel


class ViewPagerFragment : BaseFragment<ViewPagerBinding>() {
    companion object {
        @JvmStatic
        fun newInstance() =
            ViewPagerFragment().apply {
                arguments = Bundle().apply {
                }
            }

        const val DEFAULT_TAB_INDEX = 0
    }

    private val appViewModel: AppViewModel by activityViewModels()
    private val viewPagerHistory: ArrayList<Int> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): ViewPagerBinding {
        return ViewPagerBinding.inflate(inflater, container, false)
    }

    override fun initData() {
        binding.pager.adapter =
            ViewPagerAdapter(this, activity?.supportFragmentManager ?: parentFragmentManager)
        appViewModel.changePageCount(TabItem.values().size)

        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            // inflate custom view to tab item
            TabItem.values()[position].let {
                tab.customView = layoutInflater.inflate(R.layout.main_tab_layout_item, null).apply {
                    this.findViewById<ImageView>(R.id.tabIcon).let { imgView ->
                        imgView.setBackgroundResource(it.iconActivate)
                        imgView.alpha = if (position == DEFAULT_TAB_INDEX) 1F else 0.3F
                    }
                    this.findViewById<TextView>(R.id.tabTv).let { tv ->
                        tv.text = it.text
                        tv.alpha = if (position == DEFAULT_TAB_INDEX) 1F else 0.3F
                    }
                }
            }
        }.attach()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position = tab?.position ?: 0
                // update current page index
                appViewModel.changePageIndex(position)
                //  tracking pages' index that user go through
                if (viewPagerHistory.last() != position)
                    viewPagerHistory.add(position)
                // update layout of current tab
                tab?.customView?.findViewById<ImageView>(R.id.tabIcon)?.animate()?.alpha(1F)
                tab?.customView?.findViewById<TextView>(R.id.tabTv)?.animate()?.alpha(1F)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.customView?.findViewById<ImageView>(R.id.tabIcon)?.animate()?.alpha(0.3F)
                tab?.customView?.findViewById<TextView>(R.id.tabTv)?.animate()?.alpha(0.3F)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                //pass
            }
        });

        // set current item to first page
        binding.pager.currentItem = 0
        viewPagerHistory.add(0)
    }

    /**
     * custom back behavior for viewpager
     * default behavior for back pressed here is go back to previous page that user just went to
     */
    override fun shouldEnableCustomBackPressed() = true
    override fun onBackPressed(): Boolean {
        // if there is at least a view in history then move back to it
        if (viewPagerHistory.size > 1) {
            viewPagerHistory.removeAt(viewPagerHistory.size - 1)
            binding.pager.currentItem = viewPagerHistory.last()
            return true
        }

        return false
    }

    override fun onUnmountView() {
    }
}