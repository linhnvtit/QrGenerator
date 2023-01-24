package com.linhnvt.qrgenerator.ui.screen.qrscan

import android.Manifest
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.core.Camera
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.concurrent.futures.await
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.window.layout.WindowInfoTracker
import androidx.window.layout.WindowMetricsCalculator
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.linhnvt.qrgenerator.base.BaseFragment
import com.linhnvt.qrgenerator.databinding.QrScanOverlayBinding
import com.linhnvt.qrgenerator.databinding.QrScanScreenBinding
import com.linhnvt.qrgenerator.model.QrItem
import com.linhnvt.qrgenerator.model.QrType
import com.linhnvt.qrgenerator.permission.PermissionUtil
import com.linhnvt.qrgenerator.util.*
import com.linhnvt.qrgenerator.viewmodel.QrViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


class QrScanScreen : BaseFragment<QrScanScreenBinding>() {
    companion object {
        @JvmStatic
        fun newInstance() = QrScanScreen().apply {
            arguments = Bundle().apply {}
        }

        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0
    }

    private val qrViewModel: QrViewModel by activityViewModels()

    private lateinit var qrScanOverlayBinding: QrScanOverlayBinding

    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private var lensFacing: Int = CameraSelector.LENS_FACING_BACK
    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var cameraSelector: CameraSelector? = null
    private lateinit var windowManager: WindowInfoTracker
    private lateinit var cameraExecutor: ExecutorService
    private var barCodeScanner = BarcodeScanning.getClient()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun createBinding(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): QrScanScreenBinding {
        return QrScanScreenBinding.inflate(inflater, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        if (::cameraExecutor.isInitialized) cameraExecutor.shutdown()
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        PermissionUtil(requireActivity()).request(
            Manifest.permission.CAMERA
        ) { areAllGranted, _ ->
            setUpOverlay()

            if (areAllGranted) {
                initCamera(view, savedInstanceState)
            } else {
                Toast.makeText(
                    context,
                    "Cannot open camera. Please allow app to use camera!",
                    Toast.LENGTH_SHORT
                ).show()

                qrScanOverlayBinding.tvQrScanTop.text =
                    "Cannot open camera. Please allow app to use camera!"
            }
        }
    }

    private fun initCamera(view: View, savedInstanceState: Bundle?) {
        cameraExecutor = Executors.newSingleThreadExecutor()

        windowManager = WindowInfoTracker.getOrCreate(view.context)

        binding.pvCamera.post {
            lifecycleScope.launch {
                setUpCamera()
            }
        }
    }

    private suspend fun setUpCamera() {
        cameraProvider = ProcessCameraProvider.getInstance(requireContext()).await()
        if (cameraProvider == null) throw Exception("Cannot initialize camera provider")

        bindCameraUseCases()
    }

    private fun setUpOverlay() {
        qrScanOverlayBinding =
            QrScanOverlayBinding.inflate(LayoutInflater.from(context), binding.root, true)
        val frameHeight = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 350F - 25F,
            context?.resources?.displayMetrics
        )

        qrScanOverlayBinding.ivQrScanFlip.setOnClickListener {
            lensFacing = if (CameraSelector.LENS_FACING_FRONT == lensFacing) {
                CameraSelector.LENS_FACING_BACK
            } else {
                CameraSelector.LENS_FACING_FRONT
            }
            // Re-bind use cases to update selected camera
            bindCameraUseCases()
        }

        ObjectAnimator.ofFloat(
            qrScanOverlayBinding.qrScanLoadingIndicator,
            "translationY",
            0F, frameHeight
        ).apply {
            duration = 2500
            repeatMode = ValueAnimator.REVERSE
            repeatCount = 100000
            start()
        }
    }

    private fun bindCameraUseCases() {
        val metrics = WindowMetricsCalculator.getOrCreate()
            .computeCurrentWindowMetrics(requireActivity()).bounds

        val screenAspectRatio = aspectRatio(metrics.width(), metrics.height())

        val rotation = binding.pvCamera.display.rotation

        cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

        initPreview(screenAspectRatio, rotation)
        initImageCapture(screenAspectRatio, rotation)
        initImageAnalyzer(screenAspectRatio, rotation)

        if (camera != null) camera!!.cameraInfo.cameraState.removeObservers(viewLifecycleOwner)

        try {
            preview?.setSurfaceProvider(binding.pvCamera.surfaceProvider)
            startCamera()
        } catch (e: Exception) {
            Log.e("TL", "Use case binding failed", e)
        }
    }

    private fun startCamera() {
        cameraProvider?.unbindAll()
        camera = cameraProvider?.bindToLifecycle(
            this, cameraSelector!!, preview, imageCapture, imageAnalyzer
        )
    }

    private fun restartImageAnalyzer(screenAspectRatio: Int, rotation: Int) {
        cameraProvider?.unbind(imageAnalyzer)
        initImageAnalyzer(screenAspectRatio, rotation)
        camera = cameraProvider?.bindToLifecycle(
            this, cameraSelector!!, preview, imageCapture, imageAnalyzer
        )
    }

    private fun stopCamera() {
        cameraProvider?.unbindAll()
    }

    private fun initPreview(screenAspectRatio: Int, rotation: Int) {
        preview =
            Preview.Builder().setTargetAspectRatio(screenAspectRatio).setTargetRotation(rotation)
                .build()
    }

    private fun initImageCapture(screenAspectRatio: Int, rotation: Int) {
        imageCapture =
            ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .setTargetAspectRatio(screenAspectRatio).setTargetRotation(rotation).build()
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun initImageAnalyzer(screenAspectRatio: Int, rotation: Int) {
        imageAnalyzer = ImageAnalysis.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_YUV_420_888)
            .setTargetRotation(rotation)
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build().apply {
                setAnalyzer(cameraExecutor, object : ImageAnalysis.Analyzer {
                    var shouldSkipFrame = false

                    override fun analyze(imageProxy: ImageProxy) {
                        if (!shouldSkipFrame) {
                            val image = transformBitmap(imageProxy)
                            if (image != null) {
                                barCodeScanner.process(image)
                                    .addOnSuccessListener { barcodes ->
                                        // only take first barcode atm
                                        if (barcodes.isNotEmpty()) {
                                            shouldSkipFrame = true
                                            val barcode = barcodes.first()

                                            repositionPointingView(barcode.boundingBox)
                                            with(genQrItem(barcode)) {
                                                showBottomSheet(this)
                                            }
                                        } else {
                                            qrScanOverlayBinding.cvQrScanPointer.visibility =
                                                View.GONE
                                        }
                                    }
                                    .addOnCompleteListener {
                                        imageProxy.close()
                                    }
                            } else
                                imageProxy.close()
                        } else
                            imageProxy.close()
                    }

                    fun showBottomSheet(qrItem: QrItem) {
                        QrScanBottomSheetDialog(qrItem, {
                            shouldSkipFrame = false
                        }, {
                            qrViewModel.saveQrContentToLocal(qrItem)
                        }).show(
                            childFragmentManager, QrScanBottomSheetDialog::class.java.name
                        )
                    }
                })
            }
    }

    fun genQrItem(barcode: Barcode): QrItem {
        val content: String
        var extra = ""
        var type = QrType.TEXT

        when (barcode.valueType) {
            Barcode.TYPE_WIFI -> {
                extra = barcode.wifi!!.ssid.toString()
                content = barcode.wifi!!.password.toString()
                type = QrType.WIFI
            }
            Barcode.TYPE_URL -> {
                content = barcode.url!!.url.toString()
                type = QrType.URL
            }
            else -> {
                content = barcode.rawValue ?: Constant.EMPTY
            }
        }

        return QrItem(
            type,
            content,
            barcode.rawValue ?: Constant.EMPTY,
            extra = extra
        )
    }

    fun repositionPointingView(bounds: Rect?) {
        bounds?.let {
            qrScanOverlayBinding.cvQrScanPointer.visibility =
                View.VISIBLE
            qrScanOverlayBinding.cvQrScanPointer.x =
                (it.right + it.left) / 2F + qrScanOverlayBinding.ivQrScanFrame.x
            qrScanOverlayBinding.cvQrScanPointer.y =
                (it.bottom + it.top) / 2F + qrScanOverlayBinding.ivQrScanFrame.y
        }
    }

    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }

    fun transformBitmap(imageProxy: ImageProxy): InputImage? {
        var bitmap = imageProxy.image?.toBitMap()
        bitmap?.let {
            val imageInfo = imageProxy.imageInfo
            val scale = binding.pvCamera.height / it.width.toFloat()
            val transformedPoints = correctRotationDegreeMismatch(
                imageInfo.rotationDegrees,
                qrScanOverlayBinding.ivQrScanFrame.x,
                qrScanOverlayBinding.ivQrScanFrame.y,
                qrScanOverlayBinding.ivQrScanFrame.width,
                qrScanOverlayBinding.ivQrScanFrame.height,
                scale
            )
            val matrix = Matrix().apply {
                postRotate(imageInfo.rotationDegrees.toFloat())
                if (lensFacing == CameraSelector.LENS_FACING_FRONT) {
                    preScale(-1.0f, 1.0f)
                    postRotate(imageInfo.rotationDegrees.toFloat() + 270F)
                }
            }

            bitmap =
                Bitmap.createBitmap(
                    it,
                    transformedPoints[0],
                    transformedPoints[1],
                    transformedPoints[2],
                    transformedPoints[3],
                    matrix,
                    true
                )

//            lifecycleScope.launch(Dispatchers.Main) {
//                qrScanOverlayBinding.ivReview.setImageBitmap(bitmap)
//            }

            return InputImage.fromBitmap(
                it, imageProxy.imageInfo.rotationDegrees
            )
        }
        return null
    }

    private fun correctRotationDegreeMismatch(
        rotation: Int,
        x: Float,
        y: Float,
        width: Int,
        height: Int,
        scale: Float
    ): Array<Int> {
        val newX = x.div(scale).toInt()
        val newY = y.div(scale).toInt()
        val newWidth = width.div(scale).toInt()
        val newHeight = height.div(scale).toInt()

        return when (rotation) {
            90, 270 -> arrayOf(newY, newX, newHeight, newWidth)
            else -> arrayOf(newX, newY, newWidth, newHeight)
        }
    }
}

