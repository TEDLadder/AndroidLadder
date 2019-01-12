package com.sunladder.summary.view.xfermode

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuffXfermode
import android.util.AttributeSet
import android.view.View

/**
 * Description:
 * Created by Sun Yaozong on 2019/1/12
 *
 * @author Sun Yaozong
 */

class XferModeView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val partWidth = 150
    private val partOffset = partWidth / 2
    private val part1C = Color.RED
    private val part2C = Color.GREEN

    private val mPaint = Paint()
    private var mNeedOffset = false

    var mXfermode: PorterDuffXfermode? = null

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val drawX = (measuredWidth - partWidth).toFloat() / 2
        val drawY = (measuredHeight - partWidth).toFloat() / 2

        val layerID = canvas.saveLayer(0F, 0F, canvas.width.toFloat(), canvas.height.toFloat(),
                mPaint, Canvas.ALL_SAVE_FLAG)

        mPaint.reset()
        mPaint.style = Paint.Style.FILL
        mPaint.color = part1C
        canvas.drawRect(drawX, drawY, drawX + partWidth, drawY + partWidth, mPaint)

        mPaint.reset()
        mPaint.xfermode = mXfermode
        mPaint.style = Paint.Style.FILL
        mPaint.color = part2C
        var part2DrawX = drawX
        var part2DrawY = drawY
        if (mNeedOffset) {
            part2DrawX += partOffset
            part2DrawY += partOffset
        }
        canvas.drawRect(part2DrawX, part2DrawY, part2DrawX + partWidth, part2DrawY + partWidth, mPaint)
        mPaint.xfermode = null;

        canvas.restoreToCount(layerID);
    }

    fun bind(xfermode: PorterDuffXfermode?, needOffset: Boolean = false) {
        mXfermode = xfermode
        mNeedOffset = needOffset
    }
}