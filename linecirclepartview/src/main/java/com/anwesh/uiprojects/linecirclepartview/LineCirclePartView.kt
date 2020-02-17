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
val delay : Long = 20

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

class LineCirclePartView(ctx : Context) : View(ctx) {

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }

    data class State(var scale : Float = 0f, var dir : Float = 0f, var prevScale : Float = 0f) {

        fun update(cb : (Float) -> Unit) {
            scale += scGap * dir
            if (Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                cb(prevScale)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            if (dir == 0f) {
                dir = 1f - 2 * prevScale
                cb()
            }
        }
    }

    data class Animator(var view : View, var animated : Boolean = false) {

        fun animate(cb : () -> Unit) {
            if (animated) {
                cb()
                try {
                    Thread.sleep(delay)
                    view.invalidate()
                } catch(ex : Exception) {

                }
            }
        }

        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }

    data class LCPNode(var i : Int, val state : State = State()) {

        private var next : LCPNode? = null
        private var prev : LCPNode? = null

        init {
            addNeighbor()
        }

        fun addNeighbor() {
            next = LCPNode(i + 1)
            next?.prev = this
        }

        fun draw(canvas : Canvas, paint : Paint) {
            canvas.drawLCPNode(i, state.scale, paint)
            next?.draw(canvas, paint)
        }

        fun update(cb : (Float) -> Unit) {
            state.update(cb)
        }

        fun startUpdating(cb : () -> Unit) {
            state.startUpdating(cb)
        }

        fun getNext(dir : Int, cb : () -> Unit) : LCPNode {
            var curr : LCPNode? = prev
            if (dir == 1) {
                curr = next
            }
            if (curr != null) {
                return curr
            }
            cb()
            return this
        }
    }
}