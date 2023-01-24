package com.linhnvt.qrgenerator.ui.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder

class ViewPagerAdapter(fragment: Fragment, private val supportFragmentManager: FragmentManager) :
    FragmentStateAdapter(supportFragmentManager, fragment.lifecycle) {
    override fun getItemCount() = TabItem.values().size

    override fun createFragment(position: Int): Fragment {
        return (TabItem.values()[position].className.newInstance() as Fragment)
    }

    override fun onBindViewHolder(
        holder: FragmentViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
    }
}