package bitspilani.dvm.apogee2016.ui.bottombar

import android.animation.ArgbEvaluator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import bitspilani.dvm.apogee2016.R
import bitspilani.dvm.apogee2016.ui.main.CC

/**
 * Created by Vaibhav on 03-02-2018.
 */

const val tagg = "bottombar"

class BottomInteractiveBar(context: Context, attributeSet: AttributeSet?) : View(context, attributeSet), ViewPager.OnPageChangeListener {

    private lateinit var colorInfo: Array<BarColorInfo>
    private val barHeight = resources.getDimension(R.dimen.BIBBarHeight).toInt()
    private val shadowHeight = resources.getDimension(R.dimen.BIBShadowHeight).toInt()
    private val evaluator = ArgbEvaluator()
    var currPos = 1

    private val baseRect = Rect()
    private val shadowRect = Rect()
    private val basePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val centerIconDrawableBackground = GradientDrawable()
    private var centerIcon = ContextCompat.getDrawable(context, R.drawable.wallet_icon)
    private val centerIconBackgroundGradientArray = arrayOf(Color.parseColor("#EF8D0C"), Color.parseColor("#EF0C0C")).toIntArray()

    private val shadowDrawable = GradientDrawable()
    private val shadowGradientArray = arrayOf(Color.parseColor("#6BEF0C0C"), Color.TRANSPARENT).toIntArray()

    private val iconsHeight = resources.getDimension(R.dimen.BIBIconsHeight)
    private val ongoingWidth = resources.getDimension(R.dimen.ongoingWidth)

    private val icons = arrayOf(ContextCompat.getDrawable(context, R.drawable.ic_profile),
            ContextCompat.getDrawable(context, R.drawable.ic_events),
            ContextCompat.getDrawable(context, R.drawable.ic_map),
            ContextCompat.getDrawable(context, R.drawable.ic_ongoing))
    private val text = arrayOf("PROFILE", "EVENTS", "MAP", "ONGOING")
    private val textPositionX = Array(4) { 0f }
    private val textPositionY = Array(4) { 0f }

    private val textPaint = Array(4) { Paint(Paint.ANTI_ALIAS_FLAG) }
    private val textColorUnselected = ContextCompat.getColor(context, R.color.BIBTextColorUnselected)
    private val textColorSelected = ContextCompat.getColor(context, R.color.BIBTextColorSelected)

    private var bottomInteractiveBarOnClickListener: BottomInteractiveBarOnClickListener? = null

    private val touchBoxes = Array(4) { Rect() }

    constructor(context: Context) : this(context, null)

    init {
        basePaint.color = Color.WHITE
        basePaint.style = Paint.Style.FILL

        shadowPaint.style = Paint.Style.FILL
        shadowPaint.shader = LinearGradient(0f, 0f,
                0f, shadowHeight.toFloat(),
                Color.TRANSPARENT, Color.parseColor("#12000000"),
                Shader.TileMode.REPEAT)

        centerIconDrawableBackground.shape = GradientDrawable.OVAL
        centerIconDrawableBackground.orientation = GradientDrawable.Orientation.BL_TR
        centerIconDrawableBackground.colors = centerIconBackgroundGradientArray

        shadowDrawable.shape = GradientDrawable.OVAL
        shadowDrawable.gradientType = GradientDrawable.RADIAL_GRADIENT
        shadowDrawable.gradientRadius = 200f
        shadowDrawable.colors = shadowGradientArray

        textPaint.forEach {
            it.textAlign = Paint.Align.CENTER
            it.color = textColorUnselected
            it.textSize = resources.getDimension(R.dimen.labelSize)
        }
        icons.forEach { DrawableCompat.setTint(it!!, textColorUnselected) }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val requiredHeight = shadowHeight + barHeight
        setMeasuredDimension(resolveSize(MeasureSpec.getSize(widthMeasureSpec), widthMeasureSpec),
                resolveSize(requiredHeight, heightMeasureSpec))
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {

        baseRect.set(0, height / 2, width, height)
        shadowRect.set(0, 0, width, height / 2)

        var halfSize = (0.6 * barHeight).toInt()
        centerIconDrawableBackground.bounds = Rect(w / 2 - halfSize, shadowHeight - halfSize, w / 2 + halfSize, shadowHeight + halfSize)

        halfSize *= 2
        shadowDrawable.bounds = Rect(w / 2 - halfSize, shadowHeight - halfSize, w / 2 + halfSize, shadowHeight + halfSize)

        halfSize = (barHeight * 0.24).toInt()
        centerIcon?.bounds?.set(Rect(w / 2 - halfSize, shadowHeight - halfSize, w / 2 + halfSize, shadowHeight + halfSize))

        val availSpace = w / 2 - barHeight
        val top = (shadowHeight + (barHeight / 2 - iconsHeight / 2)/2).toInt()
        val bottom = top + iconsHeight.toInt()

        icons[0]?.bounds?.set(Rect((availSpace / 4 - iconsHeight / 2).toInt(), top,
                (availSpace / 4 + iconsHeight / 2).toInt(), bottom))
        icons[1]?.bounds?.set(Rect((availSpace * 0.75 - iconsHeight / 2).toInt(), top,
                (availSpace * 0.75 + iconsHeight / 2).toInt(), bottom))
        icons[2]?.bounds?.set(Rect((w / 2 + barHeight + availSpace / 4 - iconsHeight / 2).toInt(), top,
                (w / 2 + barHeight + availSpace / 4 + iconsHeight / 2).toInt(), bottom))
        icons[3]?.bounds?.set(Rect((w / 2 + barHeight + availSpace * 0.75 - ongoingWidth / 2).toInt(), top,
                (w / 2 + barHeight + availSpace * 0.75 + ongoingWidth / 2).toInt(), bottom))


        val textBounds = Rect()
        textPaint[0].getTextBounds(text[0], 0, text[0].length, textBounds)
        val textYPos = height - ((barHeight - iconsHeight) / 4 - textBounds.height() / 2 + (top - shadowHeight))
        textPositionX[0] = (width / 2 - barHeight) * 0.25f
        textPositionX[1] = (width / 2 - barHeight) * 0.75f
        textPositionX[2] = (width / 2 + barHeight) + ((width / 2 - barHeight) * 0.25f)
        textPositionX[3] = (width / 2 + barHeight) + ((width / 2 - barHeight) * 0.75f)
        (0..3).forEach { textPositionY[it] = textYPos }

        touchBoxes[0].set(0, shadowHeight, ((width / 2 - barHeight) * 0.5).toInt(), height)
        touchBoxes[1].set(((width / 2 - barHeight) * 0.5).toInt(), shadowHeight, width / 2 - barHeight, height)
        touchBoxes[2].set(width / 2 + barHeight, shadowHeight, width / 2 + barHeight + ((width / 2 - barHeight) * 0.5).toInt(), height)
        touchBoxes[3].set(width / 2 + barHeight + ((width / 2 - barHeight) * 0.5).toInt(), shadowHeight, width, height)
    }


    override fun onDraw(canvas: Canvas) {

        canvas.drawRect(baseRect, basePaint)
        canvas.drawRect(shadowRect, shadowPaint)

        shadowDrawable.draw(canvas)
        centerIconDrawableBackground.draw(canvas)
        centerIcon?.draw(canvas)

        icons.forEach { it?.draw(canvas) }
        (0..3).forEach { canvas.drawText(text[it], textPositionX[it], textPositionY[it], textPaint[it]) }
    }

    fun setUpWithViewPager(viewPager: ViewPager, colorInfo: Array<BarColorInfo>) {
        this.colorInfo = colorInfo
        viewPager.addOnPageChangeListener(this)
        centerIconBackgroundGradientArray[0] = colorInfo[viewPager.currentItem].colorA
        centerIconBackgroundGradientArray[1] = colorInfo[viewPager.currentItem].colorB
        centerIconDrawableBackground.orientation = colorInfo[viewPager.currentItem].orientation
        shadowGradientArray[0] = colorInfo[viewPager.currentItem].shadowColor
        textPaint[viewPager.currentItem].color = textColorSelected
        DrawableCompat.setTint(icons[viewPager.currentItem]!!, textColorSelected)
        invalidate()
    }

    fun setBottomInteractiveBarOnClickListener(bottomInteractiveBarOnClickListener: BottomInteractiveBarOnClickListener) {
        this.bottomInteractiveBarOnClickListener = bottomInteractiveBarOnClickListener
    }

    fun setCentralIconAsWallet() {
        centerIcon = ContextCompat.getDrawable(context, R.drawable.wallet_icon)
        invalidate()
    }

    fun setCentralIconAs(drawable: Drawable) {
        centerIcon = drawable
        invalidate()
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        val evaluated = evaluator.evaluate(positionOffset, CC.getScreenColorFor(position).colorB, CC.getScreenColorFor(position + 1).colorB) as Int
        centerIconBackgroundGradientArray[0] = evaluator.evaluate(positionOffset, CC.getScreenColorFor(position).colorA, CC.getScreenColorFor(position + 1).colorA) as Int
        centerIconBackgroundGradientArray[1] = evaluated
        shadowGradientArray[0] = evaluated
        centerIconDrawableBackground.colors = centerIconBackgroundGradientArray
        shadowDrawable.colors = shadowGradientArray
        DrawableCompat.setTint(icons[currPos]!!, evaluated)
        textPaint[currPos].color = evaluated
        invalidate()
    }

    override fun onPageSelected(position: Int) {

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
            if( centerIconDrawableBackground.bounds.contains(event.x.toInt(), event.y.toInt())) {
                bottomInteractiveBarOnClickListener?.onCenterButtonClick()
                return true
            }

            touchBoxes.forEachIndexed { index, rect ->
                if (rect.contains(event.x.toInt(), event.y.toInt())) {
                    icons.forEach { DrawableCompat.setTint(it!!, textColorUnselected) }
                    textPaint.forEach { it.color = textColorUnselected }
                    DrawableCompat.setTint(icons[currPos]!!, CC.getScreenColorFor(0).colorB)
                    textPaint[currPos].color = CC.getScreenColorFor(0).colorB
                    centerIconBackgroundGradientArray[0] = CC.getScreenColorFor(0).colorA
                    centerIconBackgroundGradientArray[1] = CC.getScreenColorFor(0).colorB
                    shadowGradientArray[0] = CC.getScreenColorFor(0).colorB
                    centerIconDrawableBackground.colors = centerIconBackgroundGradientArray
                    shadowDrawable.colors = shadowGradientArray
                    invalidate()
                    currPos = index
                    bottomInteractiveBarOnClickListener?.onClickItem(index)
                    return true
                }
            }

        return super.onTouchEvent(event)
    }
}
