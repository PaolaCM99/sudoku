package com.example.sudoku.view.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.sudoku.game.Cell
import kotlin.math.min

class SudokuBoard(context:Context, attributeSet: AttributeSet): View(context, attributeSet) {

    private var squareSize = 3
    private var size = 9
    private var cellPixels = 0F
    private var row = 0
    private var col = 0

    private var listener : OnTouchListener? = null

    private var cells: List<Cell>? = null

    private val thickLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.rgb(103, 80, 164)
        strokeWidth = 8F
    }

    private val thinLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 4F
    }

    private val selectCellPainting = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.rgb(103, 80, 164)
    }

    private val problematicCellPainting = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.rgb(214, 179, 255)
    }

    private val textPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.rgb(103, 80, 164)
        textSize = 34F
    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val sizePixels = min(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(sizePixels, sizePixels)
    }

    override fun onDraw(canvas: Canvas) {
        cellPixels = (width/size).toFloat()
        fillCells (canvas)
        drawLines(canvas)
        drawText(canvas)
    }
    private fun fillCells(canvas: Canvas){
        cells?.forEach {
            val r = it.row
            val c = it.col

            if (r == row && c == col ){
                fillCell(canvas, r,c, selectCellPainting)
            }else if ( r == row || c == col ){

                fillCell(canvas, r, c, problematicCellPainting)
            } else if (r / squareSize == row / squareSize && c / squareSize == col / squareSize){
                fillCell( canvas, r, c, problematicCellPainting)
            }
        }
    }

    private fun fillCell(canvas: Canvas, r: Int, c: Int, paint: Paint){
        canvas.drawRect(c * cellPixels, r * cellPixels, (c + 1) * cellPixels, (r + 1) * cellPixels, paint)
    }

    private fun drawLines(canvas: Canvas) {
        canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), thickLinePaint)

        for (i in 1 until size) {
            val paint = when (i % squareSize) {
                0 -> thickLinePaint
                else -> thinLinePaint
            }

            canvas.drawLine(
                i * cellPixels,
                0F,
                i * cellPixels,
                height.toFloat(),
                paint
            )

            canvas.drawLine(
                0F,
                i * cellPixels,
                width.toFloat(),
                i * cellPixels,
                paint
            )
        }
    }

    private fun drawText(canvas:Canvas){
        cells?.forEach{
            val row = it.row
            val col = it.col
            val valueString = it.value.toString()

            val textBounds = Rect()
            textPaint.getTextBounds(valueString, 0, valueString.length, textBounds)
            val textWidth = textPaint.measureText(valueString)
            val textHeight = textBounds.height()

            canvas.drawText(valueString, (col * cellPixels) + cellPixels / 2 - textWidth / 2,
                (row * cellPixels)+ cellPixels / 2 -textHeight/2, textPaint)

        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                handleTouchEvent(event.x, event.y)
                true
            }
            else -> false
        }
    }
    private fun handleTouchEvent (x : Float, y: Float){
        val possibleSelectedRow = (y / cellPixels).toInt()
        val possibleSelectedCol = (x / cellPixels).toInt()
        listener?.onCellTouch(possibleSelectedRow, possibleSelectedCol)
        //invalidate()

    }
    fun updateSelectedCellUI(selectRow: Int, selectCol:Int){
        row = selectRow
        col = selectCol
        invalidate()
    }

    fun updateCells(cells:List<Cell>){
        this.cells = cells
        invalidate()
    }

    fun registerListener(listener: OnTouchListener){
        this.listener = listener
    }
    interface OnTouchListener{
//        abstract val nineBtn: Any
//        abstract val eigthBtn: Any
//        abstract val sevenBtn: Any
//        abstract val sixBtn: Any
//        abstract val fiveBtn: Any
//        abstract val fourBtn: Any
//        abstract val threeBtn: Any
//        abstract val twoBtn: Any
//        abstract val oneBtn: Any

        fun onCellTouch(row:Int, col:Int)
    }

}