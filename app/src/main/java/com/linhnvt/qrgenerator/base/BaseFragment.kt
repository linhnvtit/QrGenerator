package com.linhnvt.qrgenerator.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.linhnvt.qrgenerator.R
import com.linhnvt.qrgenerator.databinding.FragmentBaseLayoutBinding
import com.linhnvt.qrgenerator.tracking.Analytic
import com.linhnvt.qrgenerator.ui.screen.history.HistoryScreen
import com.linhnvt.qrgenerator.ui.screen.qrscan.QrScanScreen
import com.linhnvt.qrgenerator.ui.viewpager.ViewPagerFragment
import com.linhnvt.qrgenerator.util.Constant
import com.linhnvt.qrgenerator.util.Info
import com.linhnvt.qrgenerator.util.ScreenAnimation
import com.linhnvt.qrgenerator.util.navigateToScreen

abstract class BaseFragment<out T : ViewBinding> : Fragment() {
    /**
     * binding for fragment that inherit from this. It just a frame layout in base layout
     */
    private var _binding: T? = null
    protected val binding: T
        get() = _binding ?: throw IllegalStateException(Constant.BINDING_INVALID_WARNING)


    /**
     * binding for base fragment
     */
    private var _baseViewBinding: FragmentBaseLayoutBinding? = null
    private val baseViewBinding: FragmentBaseLayoutBinding
        get() = _baseViewBinding ?: throw IllegalStateException(Constant.BINDING_INVALID_WARNING)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (shouldEnableCustomBackPressed()) {
            with(requireActivity()) {
                onBackPressedDispatcher.addCallback(this) {
                    val result = isVisible && this@BaseFragment.onBackPressed()
                    if (!result) {
                        if (supportFragmentManager.backStackEntryCount >= 1)
                            supportFragmentManager.popBackStack()
                        else
                            finish()
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = createBinding(inflater, container, savedInstanceState)
        _baseViewBinding = FragmentBaseLayoutBinding.inflate(inflater, container, false)

        // setup header
        initHeaderView()

        // setup main layout for fragment
        baseViewBinding.fragmentMainLayout.apply {
            removeAllViews()
            addView(binding.root)
        }

        return baseViewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view, savedInstanceState)
        initData()
        initAction()
        sendGA()
        logGA()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onUnmountView()
        _binding = null
    }

    fun getHeaderView(): BaseHeader = baseViewBinding.headerView

    private fun initHeaderView() {
        if (!shouldShowHeader())
            baseViewBinding.headerView.visibility = View.GONE
        else {
            baseViewBinding.headerView.apply {
                setLeftAction(R.drawable.ic_baseline_keyboard_arrow_left_24) {
                    goBack()
                }
            }
        }
    }

    private fun sendGA() {
        Analytic.getInstance().logEvent(Constant.GA_SCREEN, Bundle().apply {
            putString(Constant.GA_SCREEN_NAME, this::class.java.name)
            putString(Constant.GA_DEVICE_INFO, Info.getAnalyticInfo())
        })
    }

    private fun logGA() {
        Log.i(this::class.java.name, Info.getAnalyticInfo())
    }

    /**
     * go back to previous fragment
     */
    private fun goBack() {
        activity?.onBackPressed()
    }

    /**
     * fragment from view pager need to navigate to others through this function
     */
    fun navigateToScreenFromViewPager(
        fragment: Fragment,
        tag: String,
        type: ScreenAnimation,
        shouldReplacePriorFragment: Boolean = false,
        shouldAddToBackStack: Boolean = true,
    ) {
        val fromFragment =
            activity?.supportFragmentManager?.findFragmentByTag(ViewPagerFragment::class.java.name)
                ?: this
        navigateToScreen(
            fragment,
            tag,
            type,
            shouldReplacePriorFragment,
            shouldAddToBackStack,
            fromFragment
        )
    }

    /**
     * normal screens go here
     */
    fun navigateToScreen(
        fragment: Fragment,
        tag: String,
        type: ScreenAnimation = ScreenAnimation.FADE,
        shouldReplacePriorFragment: Boolean = false,
        shouldAddToBackStack: Boolean = true,
        fromFragment: Fragment = this,
    ) {
        if (navigationInterceptorCheck(fragment::class.java.name))
            Toast.makeText(context, Constant.FEATURE_NOT_AVAILABLE, Toast.LENGTH_SHORT).show()
        else
            (activity as? AppCompatActivity)?.navigateToScreen(
                fromFragment,
                fragment,
                tag,
                type,
                shouldReplacePriorFragment,
                shouldAddToBackStack
            )
    }

    private fun navigationInterceptorCheck(fragmentName: String): Boolean {
        Info.getAppManifest()?.let {
            return (!it.enableHistory && fragmentName == HistoryScreen::class.java.name) ||
                    (!it.enableQrScan && fragmentName == QrScanScreen::class.java.name)
        }

        return false
    }

    /**
     * create binding for fragment
     */
    abstract fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): T

    open fun shouldEnableCustomBackPressed() = false

    open fun onBackPressed(): Boolean = false

    open fun shouldShowHeader() = false

    open fun initView(view: View, savedInstanceState: Bundle?) = Unit

    open fun initData() = Unit

    open fun initAction() = Unit

    open fun onUnmountView() = Unit
}
