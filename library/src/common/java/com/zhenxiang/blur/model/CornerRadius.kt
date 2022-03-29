package com.zhenxiang.blur.model

data class CornersRadius(
    val topLeft: Float,
    val topRight: Float,
    val bottomLeft: Float,
    val bottomRight: Float,
) {

    companion object {
        fun all(radius: Float): CornersRadius {
            return CornersRadius(radius, radius, radius, radius)
        }
    }
}