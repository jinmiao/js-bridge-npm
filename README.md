# js-bridge-npm

# js 项目创建

JavaScript 中调用 npm 服务并通过 webpack 打包多个 JS 文件成一个 bundle.js。
这个过程涉及设置一个适合的项目结构，安装必要的 npm 包，配置 webpack，并最终打包生成 bundle.js。

### 1.初始化项目
首先创建一个新的项目文件夹，并初始化一个 npm 项目：
```shell
mkdir web-js-npm
cd web-js-npm
npm init -y  # 自动生成 package.json 文件
```

生成的 `package.json` 如下：
```json
{
    "name": "js-npm-web",
    "version": "1.0.0",
    "description": "",
    "main": "index.js",
    "scripts": {
        "test": "echo \"Error: no test specified\" && exit 1"
    },
    "keywords": [],
    "author": "",
    "license": "ISC"
}
```

### 2.安装依赖
安装 webpack ：
```shell
npm install webpack webpack-cli --save-dev 
```
`webpack` 是核心工具，`webpack-cli` 允许你在命令行中运行 `webpack`

### 3.创建项目结构
在项目目录下创建一个简单的文件结构：
```shell
/web-js-npm
  /src
    index.js
    component.js
  /output
  webpack.config.js
  package.json
```
在 src 文件夹中，index.js 可以是入口文件，而 component.js 是一个额外的模块。

### 4.编写 JavaScript 文件
调用 npm 服务 [debeem-wallet](https://www.npmjs.com/package/debeem-wallet)，需要安装 debeem-wallet 服务和对应的依赖
```shell
npm install debeem-wallet debeem-id debeem-cipher ethers idb
npm install fake-indexeddb --save
```

### 5.配置 webpack
在项目根目录下创建 webpack.config.js：
```shell
// webpack.config.js
const path = require('path');

module.exports = {
  mode: 'production',
  entry: './src/index.js',  // 入口文件
  output: {
    filename: 'bundle.js',  // 输出文件
    path: path.resolve(__dirname, 'output'),  // 输出路径
  }
};
```
这个配置告诉 webpack 从 src/index.js 开始打包，将所有依赖打包到 output/bundle.js。

### 6.生成 bundle.js 文件
使用 webpack 命令生成 bundle.js
```shell
npx webpack 
```


