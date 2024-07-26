
export function replacer(key, value) {
    if (typeof value === 'bigint') {
        return value.toString() + 'n'; // 显式地标记BigInt
    }
    return value;
}