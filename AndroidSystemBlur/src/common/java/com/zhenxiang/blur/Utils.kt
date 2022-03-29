package com.zhenxiang.blur

// Consider edgeRadius when more than -1, otherwise use allSidesRadius
fun formatEdgeCornerRadius(allSidesRadius: Int, edgeRadius: Int): Float {
    return if (edgeRadius > -1) {
        edgeRadius
    } else {
        allSidesRadius
    }.toFloat()
}
