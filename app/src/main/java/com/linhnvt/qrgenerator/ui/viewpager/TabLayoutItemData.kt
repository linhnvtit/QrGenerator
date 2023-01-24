package com.linhnvt.qrgenerator.ui.viewpager

import com.linhnvt.qrgenerator.R
import com.linhnvt.qrgenerator.ui.screen.home.HomeScreen
import com.linhnvt.qrgenerator.ui.screen.menu.MenuScreen

enum class TabItem(val iconActivate: Int, val text: String, val className: Class<*>) {
    Home(R.drawable.ic_baseline_home_24, "Home", HomeScreen::class.java),
    Menu(R.drawable.ic_baseline_menu_24,"Menu", MenuScreen::class.java),
}