package com.anwesh.uiprojects.linecirclepartview

/**
 * Created by anweshmishra on 17/02/20.
 */

import android.view.View
import android.view.MotionEvent
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RectF
import android.app.Activity
import android.content.Context

val nodes : Int = 5
val lines : Int = 2
val strokeFactor : Float = 90f
val sizeFactor : Float = 2.9f
val scGap : Float = 0.02f
val foreColor : Int = Color.parseColor("#673AB7")
val backColor : Int = Color.parseColor("#BDBDBD")
val rFactor : Float = 4f

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n
fun Float.sinify() : Float = Math.sin(this * Math.PI).toFloat()

fun Canvas.drawLineCirclePart(i : Int, scale : Float, size : Float, paint : Paint) {
    val r : Float = size / rFactor
    val sf : Float = scale.sinify().divideScale(i, lines)
    val y : Float = size - (size - r) * sf
    var y1 : Float = 0f
    if (scale > 0.5f) {
        y1 = (size - r) * sf
    }
    save()
    scale(1f, 1f - 2 * i)
    translate(0f, 0f)
    drawLine(0f, 0f, 0f, y, paint)
    restore()
    save()
    translate(0f, y1)
    drawArc(RectF(-r, -r, r, r), 0f, 180f, false, paint)
    restore()
}

fun Canvas.drawLineCircleParts(scale : Float, size : Float, paint : Paint) {
    paint.style = Paint.Style.STROKE
    for (j in 0..1) {
        drawLineCirclePart(j, scale , size, paint)
    }
}

fun Canvas.drawLCPNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    val gap : Float = h / (nodes + 1)
    val size : Float = gap / sizeFactor
    paint.color = foreColor
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    paint.strokeCap = Paint.Cap.ROUND
    save()
    translate(w / 2, gap * (i + 1))
    drawLineCircleParts(scale, size, paint)
    restore()
}
