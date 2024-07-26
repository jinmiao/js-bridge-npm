package com.debeem.wallet.npm.js_bridge_npm

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import org.json.JSONArray
import org.json.JSONObject

class NpmServiceSDK(context: Context) {
    private val webView: WebView = WebView(context)
    private var inited: Boolean = false
    private val callbackMap = mutableMapOf<String, (String) -> Unit>()

    companion object {
        const val TAG = "JSBridgeSDK"
    }

    init {
        setupWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        webView.apply {
            settings.javaScriptEnabled = true
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    super.onPageFinished(view, url)
                    Log.d("$TAG:WebViewClient", "onPageFinished")
                    inited = true
                }
            }
            webChromeClient = WebChromeClient()
            addJavascriptInterface(WebAppInterface(), "Android")
            loadUrl("file:///android_asset/index.html")
        }
    }

    // 异步调用
    // 使用场景：调用 js 方法不能立即返回结果，需要执行一段时间才能完成任务。
    // 例如网络请求等，这就需要完成异步操作后主动调用原生端的回调函数（onJsCallback）
    fun callJsFunctionAsync(
        functionName: String,
        vararg args: Any,
        callback: (String) -> Unit,
    ) {
        // 保存回调方法，等待 js 回调 onJsCallback
        callbackMap[functionName] = callback
        // 调用 JS 函数
        webView.evaluateJavascript(script(functionName, *args), null)
    }

    // 同步调用
    // 所谓的“同步”是指等待JavaScript的执行结果，而不是阻塞主线程
    // 使用场景：调用 js 方法可以立即返回结果（result）
    fun callJsFunctionSync(
        functionName: String,
        vararg args: Any?,
        callback: (String) -> Unit,
    ) {
        // 调用 JS 函数
        webView.evaluateJavascript(script(functionName, *args)) { result ->
            Log.d("$TAG:webView", "Callback data: $result")
            callback(result)
        }
    }

    private fun script(
        functionName: String,
        vararg args: Any?,
        async: Boolean = true,
    ): String {
        // 调用带回调的 JavaScript 方法
        val jsArgs = args.joinToString(", ") { arg ->
            Log.d(TAG, "arg: $arg")
            when (arg) {
                is Int, is Float, is Double -> arg.toString()
                is String -> "\"$arg\""
                is Map<*, *> -> JSONObject(arg).toString()
                is Array<*> -> JSONArray(arg).toString()
                is List<*> -> JSONArray(arg).toString()
                else -> "\"$arg\""
            }
        }

        val callbackScript = """
            function(result) {
                console.log(result);
                Android.onJsCallback('$functionName', result);
            }
        """.trimIndent()

        Log.d(TAG, "callbackScript: $callbackScript, jsArgs: $jsArgs")

        // 同步不需要传递 callbackScript，异步需要传递
        val script = when {
            jsArgs.isNotEmpty() -> {
                if (async) "$functionName($jsArgs, $callbackScript);"
                else "$functionName($jsArgs);"
            }
            else -> {
                if (async) "$functionName($callbackScript);"
                else "$functionName();"
            }
        }

        Log.d(TAG, "script: $script")

        return script;
    }

    // JavaScript 接口类
    inner class WebAppInterface {
        @JavascriptInterface
        fun onJsCallback(functionName: String, result: String) {
            // 处理从 JS 接收的数据
            Log.d("$TAG:onJsCallback", "Received from JS: $functionName : $result")
            callbackMap[functionName]?.let {
                it(result)
            }
        }
    }

    // 从 Kotlin 调用 JavaScript 函数
//    fun callJsFunctionAsync(functionName: String, pair: String, callback: (String) -> Unit) {
//        callbackMap["queryPairPrice"] = callback
//
//        val script = "window.postMessage({ functionName: '$functionName', pair: '$pair' }, '*');"
//        webView.evaluateJavascript(script, null)
//    }
}