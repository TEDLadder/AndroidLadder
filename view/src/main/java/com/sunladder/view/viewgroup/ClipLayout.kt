package com.sunladder.view.viewgroup

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 * Description:
 * Created by Sun Yaozong on 2019/1/12
 *
 * @author Sun Yaozong
 */
class ClipLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    var radiusX = 50F
    var radiusY = 50F

    val mPaint = Paint()
    val mPath = Path()
    val mAssistRectF = RectF()
    val mXfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        radiusX = Math.min(radiusX, measuredWidth.toFloat())
        radiusY = Math.min(radiusY, measuredHeight.toFloat())
    }

    override fun dispatchDraw(canvas: Canvas) {
        val width = canvas.width.toFloat()
        val height = canvas.height.toFloat()

        mPaint.reset()
        val layerID = canvas.saveLayer(0F, 0F, width, height,
                mPaint, Canvas.ALL_SAVE_FLAG)

        super.dispatchDraw(canvas)

        // clip
        mPaint.reset()
        mPaint.flags = Paint.ANTI_ALIAS_FLAG
        mPaint.strokeWidth = 0F
        mPaint.style = Paint.Style.FILL
        mPaint.xfermode = mXfermode

        // left top
        mPath.moveTo(0F, 0F)
        mPath.lineTo(0F, radiusY)
        mAssistRectF.set(0F, 0F, radiusX, radiusY)
        mPath.arcTo(mAssistRectF, 180F, 90F)
        mPath.lineTo(0F, 0F)
        canvas.drawPath(mPath, mPaint)

        // right top
        mPath.moveTo(width, 0F)
        mPath.lineTo(width - radiusX, 0F)
        mAssistRectF.set(width - radiusX, 0F, width, radiusY)
        mPath.arcTo(mAssistRectF, 270F, 90F)
        mPath.lineTo(width, 0F)
        canvas.drawPath(mPath, mPaint)

        // left bottom
        mPath.moveTo(0F, height)
        mPath.lineTo(radiusX, height)
        mAssistRectF.set(0F, height - radiusY, radiusX, height)
        mPath.arcTo(mAssistRectF, 90F, 90F)
        mPath.lineTo(0F, height)
        canvas.drawPath(mPath, mPaint)

        // right bottom
        mPath.moveTo(width, height)
        mPath.lineTo(width, height - radiusY)
        mAssistRectF.set(width - radiusX, height - radiusY, width, height)
        mPath.arcTo(mAssistRectF, 0F, 90F)
        mPath.lineTo(width, height)
        canvas.drawPath(mPath, mPaint)

        canvas.restoreToCount(layerID)
    }
}