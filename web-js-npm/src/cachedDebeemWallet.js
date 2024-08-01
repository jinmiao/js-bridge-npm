// src/cachedDebeemWallet.js

import * as DebeemWallet from 'debeem-wallet';

export function createCachedDebeemWallet(dbWrapper) {
    return {
        async initWalletAsync() {
            const cachedResult = await dbWrapper.get('initWalletAsync');
            if (cachedResult) {
                console.log('Using cached initWalletAsync result');
                return cachedResult;
            }
            const result = await DebeemWallet.initWalletAsync();
            await dbWrapper.set('initWalletAsync', result);
            return result;
        },

        async getCurrentChain() {
            const cachedResult = await dbWrapper.get('currentChain');
            if (cachedResult) {
                console.log('Using cached currentChain: ' + cachedResult);
                return cachedResult;
            }
            const result = DebeemWallet.getCurrentChain();
            await dbWrapper.set('currentChain', result);
            return result;
        },

        async setCurrentChain(chain) {
            const result = DebeemWallet.setCurrentChain(chain);
            await dbWrapper.set('currentChain', chain);
            return result;
        },

        // Add other methods as needed, with caching where appropriate
//        WalletFactory: DebeemWallet.WalletFactory,
//        WalletAccount: DebeemWallet.WalletAccount,
//        getCurrentWalletAsync: DebeemWallet.getCurrentWalletAsync,
//        putCurrentWalletAsync: DebeemWallet.putCurrentWalletAsync,
//        WalletEntityItem: DebeemWallet.WalletEntityItem
    };
}