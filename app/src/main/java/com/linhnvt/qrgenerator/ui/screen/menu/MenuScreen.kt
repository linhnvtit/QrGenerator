package com.linhnvt.qrgenerator.ui.screen.menu

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.google.android.gms.ads.*
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.nativead.MediaView
import com.linhnvt.qrgenerator.R
import com.linhnvt.qrgenerator.base.BaseFragment
import com.linhnvt.qrgenerator.databinding.AdUnunifiedBinding
import com.linhnvt.qrgenerator.databinding.MenuScreenBinding
import com.linhnvt.qrgenerator.ui.screen.history.HistoryScreen
import com.linhnvt.qrgenerator.ui.screen.qrscan.QrScanScreen
import com.linhnvt.qrgenerator.util.Constant
import com.linhnvt.qrgenerator.util.ScreenAnimation


class MenuScreen : BaseFragment<MenuScreenBinding>() {
    companion object {
        @JvmStatic
        fun newInstance() =
            MenuScreen().apply {
                arguments = Bundle().apply {
                }
            }
    }

    private var _adViewBinding: AdUnunifiedBinding? = null
    protected val adViewBinding: AdUnunifiedBinding
        get() = _adViewBinding ?: throw IllegalStateException(Constant.BINDING_INVALID_WARNING)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): MenuScreenBinding {
        return MenuScreenBinding.inflate(inflater, container, false)
    }

    override fun initAction() {
        binding.scanQrItem.setOnClickListener {
            navigateToQrScanScreen()
        }

        binding.historyItem.setOnClickListener {
            navigateToHistoryScreen()
        }
    }

    override fun shouldShowHeader(): Boolean = true

    override fun initView(view: View, savedInstanceState: Bundle?) {
        getHeaderView().apply {
            setHeaderContent(Constant.MENU_TITLE)
            hideLeftIcon(hide = true)
        }

        val builder = AdLoader.Builder(
            this@MenuScreen.requireContext(),
            "ca-app-pub-3940256099942544/2247696110"
//            "ca-app-pub-9364176215580010/5191715951"
        )

        builder.forNativeAd { nativeAd ->
            // OnUnifiedNativeAdLoadedListener implementation.
            // If this callback occurs after the activity is destroyed, you must call
            // destroy and return or you may get a memory leak.
            if (isDetached) {
                adViewBinding?.adView?.destroy()
                return@forNativeAd
            }
            // You must call destroy on old ads when you are done with them,
            // otherwise you will have a memory leak.
            _adViewBinding = AdUnunifiedBinding.inflate(layoutInflater, null, false)

            // Set the media view.
            adViewBinding.adView.mediaView = adViewBinding.adView.findViewById(R.id.ad_media)

            // Set other ad assets.
            adViewBinding.adView.headlineView = adViewBinding.adView.findViewById(R.id.ad_headline)
            adViewBinding.adView.bodyView = adViewBinding.adView.findViewById(R.id.ad_body)
            adViewBinding.adView.callToActionView =
                adViewBinding.adView.findViewById(R.id.ad_call_to_action)
            adViewBinding.adView.iconView = adViewBinding.adView.findViewById(R.id.ad_app_icon)
            adViewBinding.adView.priceView = adViewBinding.adView.findViewById(R.id.ad_price)
            adViewBinding.adView.starRatingView = adViewBinding.adView.findViewById(R.id.ad_stars)
            adViewBinding.adView.storeView = adViewBinding.adView.findViewById(R.id.ad_store)
            adViewBinding.adView.advertiserView =
                adViewBinding.adView.findViewById(R.id.ad_advertiser)
            Log.i(
                "TLTL",
                "${adViewBinding.adHeadline} ${adViewBinding.adView}"
            )
            val xx = adViewBinding
            // The headline and media content are guaranteed to be in every UnifiedNativeAd.
            (adViewBinding.adView.headlineView as? TextView)?.text = nativeAd.headline
            (adViewBinding.adView.mediaView as? MediaView)?.mediaContent = nativeAd.mediaContent

            // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
            // check before trying to display them.
            if (nativeAd.body == null) {
                (adViewBinding.adView.bodyView as TextView).visibility = View.INVISIBLE
            } else {
                (adViewBinding.adView.bodyView as TextView).visibility = View.VISIBLE
                (adViewBinding.adView.bodyView as TextView).text = nativeAd.body
            }

            if (nativeAd.callToAction == null) {
                (adViewBinding.adView.callToActionView as Button).visibility = View.INVISIBLE
            } else {
                (adViewBinding.adView.callToActionView as Button).visibility = View.VISIBLE
                (adViewBinding.adView.callToActionView as Button).text = nativeAd.callToAction
            }

            if (nativeAd.icon == null) {
                (adViewBinding.adView.iconView as ImageView).visibility = View.GONE
            } else {
                (adViewBinding.adView.iconView as ImageView).setImageDrawable(
                    nativeAd.icon!!.drawable
                )
                (adViewBinding.adView.iconView as ImageView).visibility = View.VISIBLE
            }

            if (nativeAd.price == null) {
                (adViewBinding.adView.priceView as TextView).visibility = View.INVISIBLE
            } else {
                (adViewBinding.adView.priceView as TextView).visibility = View.VISIBLE
                (adViewBinding.adView.priceView as TextView).text = nativeAd.price
            }

            if (nativeAd.store == null) {
                (adViewBinding.adView.storeView as TextView).visibility = View.INVISIBLE
            } else {
                (adViewBinding.adView.storeView as TextView).visibility = View.VISIBLE
                (adViewBinding.adView.storeView as TextView).text = nativeAd.store
            }

            if (nativeAd.starRating == null) {
                (adViewBinding.adView.starRatingView as RatingBar).visibility = View.INVISIBLE
            } else {
                (adViewBinding.adView.starRatingView as RatingBar).rating =
                    nativeAd.starRating!!.toFloat()
                (adViewBinding.adView.starRatingView as RatingBar).visibility = View.VISIBLE
            }

            if (nativeAd.advertiser == null) {
                (adViewBinding.adView.advertiserView as TextView).visibility = View.INVISIBLE
            } else {
                (adViewBinding.adView.advertiserView as TextView).text = nativeAd.advertiser
                (adViewBinding.adView.advertiserView as TextView).visibility = View.VISIBLE
            }

            // This method tells the Google Mobile Ads SDK that you have finished populating your
            // native ad view with this native ad.
            adViewBinding.adView.setNativeAd(nativeAd)

            // Get the video controller for the ad. One will always be provided, even if the ad doesn't
            // have a video asset.
            val vc = nativeAd.mediaContent?.videoController

            // Updates the UI to say whether or not this ad has a video asset.
            if (vc?.hasVideoContent() == true) {
                Log.i(
                    "TLTL",
                    "Video status: Ad contains a %.2f:1 video asset.",
                )

                // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
                // VideoController will call methods on this object when events occur in the video
                // lifecycle.
                vc.videoLifecycleCallbacks =
                    object : VideoController.VideoLifecycleCallbacks() {
                        override fun onVideoEnd() {
                            // Publishers should allow native ads to complete video playback before
                            // refreshing or replacing them with another ad in the same UI location.
                            Log.i("TLTL", "Video status: Video playback has ended.")
                            super.onVideoEnd()
                        }
                    }
            } else {
                Log.i("TLTL", "Video status: Ad does not contain a video asset.")
            }



            binding.adFrame.removeAllViews()
            binding.adFrame.addView(adViewBinding.root)
        }

        val videoOptions = VideoOptions.Builder()
            .build()

        val adOptions = NativeAdOptions.Builder()
            .setVideoOptions(videoOptions)
            .build()

        builder.withNativeAdOptions(adOptions)

        val adLoader = builder.withAdListener(object : AdListener() {
            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                val error =
                    """
                           domain: ${loadAdError.domain}, code: ${loadAdError.code}, message: ${loadAdError.message}
                    """"
                Log.i(
                    "TLTL", "Failed to load native ad with error $error",
                )
            }
        }).build()

        adLoader.loadAd(AdRequest.Builder().build())
    }

    private fun navigateToHistoryScreen() {
        navigateToScreenFromViewPager(
            HistoryScreen.newInstance(),
            HistoryScreen::class.java.name,
            ScreenAnimation.SLIDE_RIGHT_TO_LEFT
        )
    }

    private fun navigateToQrScanScreen() {
        navigateToScreenFromViewPager(
            QrScanScreen.newInstance(),
            QrScanScreen::class.java.name,
            ScreenAnimation.SLIDE_RIGHT_TO_LEFT
        )
    }
}