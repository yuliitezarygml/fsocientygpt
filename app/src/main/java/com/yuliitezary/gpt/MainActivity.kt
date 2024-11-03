package com.yuliitezary.gpt

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.webkit.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

/**
 * Главная активность приложения, отвечающая за отображение веб-интерфейса ChatGPT
 * и управление рекламой
 */
class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var mAdView: AdView
    private var filePathCallback: ValueCallback<Array<Uri>>? = null

    private val filePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            val results = if (data?.clipData != null) {
                val count = data.clipData?.itemCount ?: 0
                Array(count) { i -> data.clipData?.getItemAt(i)?.uri!! }
            } else {
                data?.data?.let { arrayOf(it) }
            }
            filePathCallback?.onReceiveValue(results ?: arrayOf())
        } else {
            filePathCallback?.onReceiveValue(arrayOf())
        }
        filePathCallback = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupWebView(savedInstanceState)
        setupAds()
        setupBackHandler()
    }

    private fun setupWebView(savedInstanceState: Bundle?) {
        webView = findViewById(R.id.webView)

        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true

            allowFileAccess = false
            allowContentAccess = false

            @Suppress("DEPRECATION")
            allowFileAccessFromFileURLs = false
            @Suppress("DEPRECATION")
            allowUniversalAccessFromFileURLs = false

            databaseEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
            setSupportZoom(true)
            builtInZoomControls = true
            displayZoomControls = false

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
            }

            cacheMode = WebSettings.LOAD_DEFAULT
            mediaPlaybackRequiresUserGesture = true
            setGeolocationEnabled(false)
        }

        webView.webChromeClient = object : WebChromeClient() {
            @Deprecated("Deprecated in Java")
            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                this@MainActivity.filePathCallback = filePathCallback
                openFileChooser()
                return true
            }
        }

        webView.webViewClient = object : WebViewClient() {
            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                showSecurityAlert(error)
                handler?.cancel()
            }

            @Deprecated("Deprecated in Java")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return !isUrlSafe(url)
            }
        }

        if (savedInstanceState != null) {
            webView.restoreState(savedInstanceState)
        } else {
            webView.loadUrl("https://chatgptchatapp.com")
        }
    }

    private fun setupAds() {
        MobileAds.initialize(this)
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }

    private fun setupBackHandler() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (webView.canGoBack()) {
                    webView.goBack()
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        webView.saveState(outState)
    }

    override fun onDestroy() {
        webView.clearCache(true)
        webView.clearHistory()
        webView.clearFormData()
        webView.clearSslPreferences()
        super.onDestroy()
    }

    private fun isUrlSafe(url: String?): Boolean {
        return url != null && (url.startsWith("https://") ||
                url.startsWith("http://") &&
                url.contains("trusted-domain.com"))
    }

    private fun openFileChooser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        filePickerLauncher.launch(Intent.createChooser(intent, "Выберите файл"))
    }

    private fun showSecurityAlert(error: SslError?) {
        val message = when (error?.primaryError) {
            SslError.SSL_DATE_INVALID -> "Сертификат просрочен"
            SslError.SSL_EXPIRED -> "Сертификат истек"
            SslError.SSL_IDMISMATCH -> "Несоответствие имени хоста"
            SslError.SSL_INVALID -> "Недействительный сертификат"
            SslError.SSL_NOTYETVALID -> "Сертификат еще не действителен"
            SslError.SSL_UNTRUSTED -> "Ненадежный сертификат"
            else -> "Проблема с безопасностью соединения"
        }

        AlertDialog.Builder(this)
            .setTitle("Предупреждение безопасности")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
}