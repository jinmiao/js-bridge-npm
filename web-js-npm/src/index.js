import {
    WalletFactory,
    WalletAccount,
    setCurrentChain,
    getCurrentChain,
    getCurrentWalletAsync,
    putCurrentWalletAsync,
    initWalletAsync,
    WalletEntityItem
} from 'debeem-wallet';
import { EtherWallet } from "debeem-id";

import { replacer } from './utils.js';

// BTC/USD
window.queryPairPrice = async function(pair, callback) {
    try {
        console.log('queryPairPrice(' + pair + ')');
//        const mnemonic = 'frog science fold balance climb resist torch egg clay spell silk emotion';
//        const walletObj = new WalletFactory().createWalletFromMnemonic( mnemonic );
//        console.error('walletObj address:', walletObj.address);
//        const balance = await new WalletAccount().queryBalance( walletObj.address );
//        console.error('walletObj balance:', balance);
//
//        // switch chain/network to Eth.Sepolia
//        setCurrentChain( 11155111 );
//        const chainId = getCurrentChain();
//        console.error('getCurrentChain chainId after:', chainId);

        const walletAccount = new WalletAccount();
        const priceObj = await walletAccount.queryPairPrice(pair);
        const jsonString = JSON.stringify(priceObj, replacer);
        console.log(jsonString);
        callback(jsonString);
    } catch (error) {
        console.error('Error querying pair price:', error);
        return null;
    }
}

window.getCurrentChain = function() {
    const chainId = getCurrentChain();
    console.log('chainId: ' + chainId);
    return chainId
}

// Function to handle messages from WebView
window.addEventListener('message', async (event) => {
    console.log(event.data);
    const { functionName, pair } = event.data;
    const priceObj = await queryPairPrice(pair);
    Android.onJsCallback(functionName, JSON.stringify(priceObj));
});

// 暴露一个全局函数来在 WebView 中调用
window.myWalletFunction = function() {
  const wallet = new WalletAccount();
  // 根据需要调用函数或处理数据
  return wallet.getInfo(); // 示例方法
};

// test
function fetchNpmDataAsync(callback) {
    setTimeout(() => {
        const result = "Data from JS 22112";
        callback(result);  // 调用 Kotlin 中的回调函数
    }, 2000);
}

// 暴露一个全局函数来在 WebView 中调用
// 写同步方法时，不需要传递 callbackScript
window.callJsWithCallbackSync = function(data) {
    console.log("callJsWithCallbackSync: JS method called with parameters: " + data);
    // 执行一些操作
    var result = "Result from JS for data: " + data;
    // 直接返回结果
    return result;
}

// 写异步方法时，需要传递 callbackScript
window.callJsWithCallback = function(data, callback) {
    console.log("callJsWithCallback: JS method called with parameters: " + data);
    // 执行一些操作
    var result = "ttt Result from JS for data: " + data;
    // 调用回调函数并传递结果
    callback(result);
}

window.callJsWithVariousParams = function(intParam, floatParam, stringParam, mapParam, arrayParam, callback) {
    console.log("hhh JS method called with parameters ");

//    console.log("hhh JS method called with parameters: ", intParam, floatParam, stringParam, mapParam, arrayParam);
    // 模拟处理参数
    var result = {
        intResult: intParam + 1,
        floatResult: floatParam + 1.1,
        stringResult: stringParam + " from JS",
        mapResult: Object.assign({}, mapParam, { fromJs: true }),
        arrayResult: arrayParam.map(item => item + " from JS")
    };
    // 调用回调函数并传递结果
    callback(JSON.stringify(result));
}