const path = require('path');

module.exports = {
  mode: 'development',  // 或 production development
  entry: './src/index.js',  // 入口文件
  output: {
    filename: 'bundle.js',  // 输出文件
    path: path.resolve(__dirname, 'output'),  // 输出路径
  }
};