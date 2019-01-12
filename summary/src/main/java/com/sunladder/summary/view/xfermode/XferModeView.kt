package com.sunladder.summary.view.xfermode

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * Description:
 * Created by syzhugh on 2019/1/12
 *
 * @author syzhugh
 */

class XferModeView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val part1Width = 200
    private val part1C = Color.RED
    private val part2Width = 200
    private val part2C = Color.GREEN
    private val partOffset = part1Width / 2

    private val mPaint = Paint()
    private var mNeedOffset = false
    private val mAssistRect = RectF()

    var mXfermode: PorterDuffXfermode? = null

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val drawX = (measuredWidth - part1Width).toFloat() / 2
        val drawY = (measuredHeight - part1Width).toFloat() / 2

        val layerID = canvas.saveLayer(0F, 0F, canvas.width.toFloat(), canvas.height.toFloat(),
                mPaint, Canvas.ALL_SAVE_FLAG)

        mPaint.reset()
        mPaint.flags = Paint.ANTI_ALIAS_FLAG
        mPaint.style = Paint.Style.FILL
        mPaint.color = Color.RED
        canvas.drawRect(drawX, drawY, drawX + part1Width, drawY + part1Width, mPaint)
//        mAssistRect.set(drawX, drawY, drawX + part1Width, drawY + part1Width)
//        canvas.drawRoundRect(mAssistRect, 20F, 20F, mPaint)

        mPaint.reset()
        mPaint.flags = Paint.ANTI_ALIAS_FLAG
        mPaint.style = Paint.Style.FILL
//        mPaint.style = Paint.Style.STROKE
        mPaint.xfermode = mXfermode
        mPaint.color = part2C
        var part2DrawX = drawX
        var part2DrawY = drawY
        if (mNeedOffset) {
            part2DrawX += partOffset
            part2DrawY += partOffset
        }
//        canvas.drawRect(part2DrawX, part2DrawY, part2DrawX + part2Width, part2DrawY + part2Width, mPaint)
        mAssistRect.set(part2DrawX, part2DrawY, part2DrawX + part2Width, part2DrawY + part2Width)
        canvas.drawRoundRect(mAssistRect, 20F, 20F, mPaint)

        mPaint.xfermode = null

        canvas.restoreToCount(layerID)
    }

    fun bind(xfermode: PorterDuffXfermode?, needOffset: Boolean = false) {
        mXfermode = xfermode
        mNeedOffset = needOffset
    }
}