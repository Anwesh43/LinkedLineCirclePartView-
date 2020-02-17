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
