package com.debeem.wallet.npm.sample

import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import com.debeem.wallet.npm.js_bridge_npm.NpmServiceSDK
import com.debeem.wallet.npm.sample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var npmServiceSDK: NpmServiceSDK
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        npmServiceSDK = NpmServiceSDK(this)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "请求数据中...", Snackbar.LENGTH_LONG).setAction("Action", null)
                .show()

            testNpmServiceAsync()
//            testNpmServiceSync()
        }
    }

    private fun testNpmServiceAsync() {

        // 调用带各种参数和回调的 JavaScript 方法
//        npmServiceSDK.callJsFunctionAsync(
//            "callJsWithVariousParams",
//            42,
//            3.14f,
//            "Hello from Kotlin",
//            mapOf("key1" to "value1", "key2" to 2),
//            listOf("item1", "item2", "item3")
//        ) { result ->
//            Log.d("MainActivity", "000 Received result from JS: $result")
//        }

        // 异步
//        npmServiceSDK.callJsFunctionAsync("callJsWithCallback", "000 hello") { result ->
//            Log.d("MainActivity", "000 Received result from JS: $result")
//
//            runOnUiThread {
//                binding.jsResultTv.text = result
//            }
//        }

        npmServiceSDK.callJsFunctionAsync("queryPairPrice", "BTC/USD") { result ->
            Log.d("queryPairPrice", "$result")

            runOnUiThread {
                binding.jsResultTv.text = result
            }
        }

        // 同步
        npmServiceSDK.callJsFunctionSync("getCurrentChain") { result ->
            Log.d("MainActivity", "result: $result")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp()
    }
}