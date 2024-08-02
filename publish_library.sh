#!/bin/bash

set -e  # 遇到错误立即退出

# 获取脚本所在目录
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# 定义SDK目录路径（与脚本同级）
SDK_DIR="$SCRIPT_DIR/js-bridge-npm"

# 检查SDK目录是否存在
if [ ! -d "$SDK_DIR" ]; then
    echo "Error: SDK directory not found at $SDK_DIR"
    exit 1
fi

# 切换到SDK目录
cd "$SDK_DIR" || exit 1

# 更新版本号
NEW_VERSION=$1
sed -i '' "s/version = '.*'/version = '$VERSION'/" "$SDK_DIR/build.gradle"

# 提交变更
git add .
git commit -m "Bump version to $NEW_VERSION"

# 创建tag并推送
git tag -a "$NEW_VERSION" -m "Version $NEW_VERSION"
git push origin master --tags

echo "Library published to JitPack. Version: $NEW_VERSION"

# 切回原始目录
cd - || exit 1