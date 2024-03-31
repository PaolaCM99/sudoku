package com.example.sudoku

import android.app.AlertDialog
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.GridLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import kotlin.random.Random
import android.graphics.Color


class LevelThreeActivity : ComponentActivity() {
    private var selectedCell: Button? = null
    private var lastSelectedButton: Button? = null
    private lateinit var mainGridLayout: GridLayout
    private lateinit var sudokuBoard: Array<IntArray>
    private lateinit var backgroundMusic: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.level_three_activity)
        mainGridLayout = findViewById<GridLayout>(R.id.main_grid_layout)
        sudokuBoard = generateSudokuBoard()
        backgroundMusic = MediaPlayer.create(this, R.raw.triforce)
        backgroundMusic.start()


        val sounds = arrayOf(
            MediaPlayer.create(this, R.raw.pedo1),
            MediaPlayer.create(this, R.raw.pedo2),
            MediaPlayer.create(this, R.raw.pedo3),
            MediaPlayer.create(this, R.raw.pedo4),
            MediaPlayer.create(this, R.raw.pedo5),
            MediaPlayer.create(this, R.raw.pedo6),
            MediaPlayer.create(this, R.raw.pedo7),
            MediaPlayer.create(this, R.raw.pedo8),
            MediaPlayer.create(this, R.raw.pedo9),
            MediaPlayer.create(this, R.raw.pedo10),
        )
        val recarge = findViewById<Button>(R.id.recarge);recarge.setOnClickListener { board() }
        val home = findViewById<Button>(R.id.mainMenu).setOnClickListener {
            if (backgroundMusic.isPlaying) {
                backgroundMusic.stop()
            }
            val intent = Intent(this@LevelThreeActivity, MainMenuActivity::class.java)
            startActivity(intent)
        }
        val one = findViewById<Button>(R.id.oneBtn).setOnClickListener {
            selectedCell?.text = "1"; getRandomSound(sounds).start()
        }
        val two = findViewById<Button>(R.id.twoBtn).setOnClickListener {
            selectedCell?.text = "2"; getRandomSound(sounds).start()
        }
        val three =
            findViewById<Button>(R.id.threeBtn).setOnClickListener {
                selectedCell?.text = "3"; getRandomSound(sounds).start()
            }
        val four =
            findViewById<Button>(R.id.fourBtn).setOnClickListener {
                selectedCell?.text = "4"; getRandomSound(sounds).start()
            }
        val del = findViewById<Button>(R.id.eraseBtn).setOnClickListener { selectedCell?.text = "" }
        val checkButton = findViewById<Button>(R.id.finishBtn)
        val timer = findViewById<TextView>(R.id.timerTextView)
        checkButton.setOnClickListener {
            println("verificando")
            var allCorrect = true
            for (rowIndex in 0 until mainGridLayout.rowCount) {
                for (colIndex in 0 until mainGridLayout.columnCount) {
                    val childGridLayout =
                        mainGridLayout.getChildAt(rowIndex * mainGridLayout.columnCount + colIndex) as GridLayout
                    for (childRowIndex in 0 until childGridLayout.rowCount) {
                        for (childColIndex in 0 until childGridLayout.columnCount) {
                            val button =
                                childGridLayout.getChildAt(childRowIndex * childGridLayout.columnCount + childColIndex) as? Button
                            val correctNumber =
                                sudokuBoard[rowIndex * childGridLayout.rowCount + childRowIndex][colIndex * childGridLayout.columnCount + childColIndex]
                            if (button != null && button.text.toString() != correctNumber.toString()) {
                                allCorrect = false
                                // Guarda el borde original
                                val originalBorder =
                                    if ((rowIndex * mainGridLayout.columnCount + colIndex) % 2 == 0) {
                                        R.drawable.blackborder
                                    } else {
                                        R.drawable.blueborder
                                    }
                                button.setBackgroundResource(R.drawable.wrongborder)
                                Handler(Looper.getMainLooper()).postDelayed({
                                    button.setBackgroundResource(originalBorder)
                                }, 2000)
                            }
                        }
                    }
                }
            }

            if (allCorrect) {

                val builder = AlertDialog.Builder(this)
                builder.setTitle("¡Felicidades!")
                builder.setMessage("¡Has ganado!")
                builder.setPositiveButton("OK") { dialog, which ->
                    if (backgroundMusic.isPlaying) {
                        backgroundMusic.stop()
                    }
                    val intent = Intent(this, MainMenuActivity::class.java)
                    startActivity(intent)
                }
                builder.show()
            }
        }
        val timerWrapper = mutableListOf<CountDownTimer>()

        timerWrapper.add(object : CountDownTimer(15200, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timer.text = "Tiempo: ${millisUntilFinished / 1000}"
            }

            override fun onFinish() {
                val main =
                    findViewById<RelativeLayout>(R.id.main); main.setBackgroundResource(R.drawable.dubstepgreenborder)
                val timer =
                    findViewById<TextView>(R.id.timerTextView); timer.setTextColor(Color.WHITE)
                val handler = Handler(Looper.getMainLooper())
                val runnable = object : Runnable {
                    var toggle = true
                    override fun run() {
                        for (rowIndex in 0 until mainGridLayout.rowCount) {
                            for (colIndex in 0 until mainGridLayout.columnCount) {
                                val childGridLayout =
                                    mainGridLayout.getChildAt(rowIndex * mainGridLayout.columnCount + colIndex) as GridLayout
                                for (childRowIndex in 0 until childGridLayout.rowCount) {
                                    for (childColIndex in 0 until childGridLayout.columnCount) {
                                        val button =
                                            childGridLayout.getChildAt(childRowIndex * childGridLayout.columnCount + childColIndex) as? Button
                                        if (toggle) {
                                            button?.setBackgroundResource(R.drawable.dubstepblueborder)
                                        } else {
                                            button?.setBackgroundResource(R.drawable.dubstepbluewhiteborder)
                                        }
                                    }
                                }
                            }
                        }
                        toggle = !toggle // Cambia el estado del interruptor
                        handler.postDelayed(
                            this,
                            800
                        ) // Ejecuta este bloque de código cada 800 milisegundos
                    }
                }
                handler.post(runnable)

                timerWrapper[0] = object : CountDownTimer(170000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        timer.text = "Tiempo: ${millisUntilFinished / 1000}"
                    }

                    override fun onFinish() {
                        val builder = AlertDialog.Builder(this@LevelThreeActivity)
                        builder.setTitle("Tiempo agotado")
                        builder.setMessage("El juego ha terminado, serás redirigido al menú principal.")
                        builder.setPositiveButton("Aceptar") { _, _ ->
                            if (backgroundMusic.isPlaying) {
                                backgroundMusic.stop()
                            }
                            val intent =
                                Intent(this@LevelThreeActivity, MainMenuActivity::class.java)
                            startActivity(intent)
                        }
                        builder.show()


                    }
                }
                timerWrapper[0].start()
            }
        })
        timerWrapper[0].start()
        board()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (backgroundMusic.isPlaying) {
            backgroundMusic.stop()
        }
        backgroundMusic.release()
    }

    private fun board() {
        printSolution(sudokuBoard)
        for (rowIndex in 0 until mainGridLayout.rowCount) {
            for (colIndex in 0 until mainGridLayout.columnCount) {
                val childGridLayout =
                    mainGridLayout.getChildAt(rowIndex * mainGridLayout.columnCount + colIndex) as GridLayout
                val border = if ((rowIndex * mainGridLayout.columnCount + colIndex) % 2 == 0) {
                    R.drawable.blackborder
                } else {
                    R.drawable.blueborder
                }
                val positionsToBlank = getRandomPositions(childGridLayout.childCount)
                for (childRowIndex in 0 until childGridLayout.rowCount) {
                    for (childColIndex in 0 until childGridLayout.columnCount) {
                        val button =
                            childGridLayout.getChildAt(childRowIndex * childGridLayout.columnCount + childColIndex) as? Button
                        val number =
                            sudokuBoard[rowIndex * childGridLayout.rowCount + childRowIndex][colIndex * childGridLayout.columnCount + childColIndex]
                        if (button != null) {
                            if (Pair(childRowIndex, childColIndex) in positionsToBlank) {
                                button.text = ""
                            } else {
                                button.text = number.toString()
                            }
                            button.setBackgroundResource(border)
                            button.setOnClickListener {
                                lastSelectedButton?.setBackgroundResource(border)
                                button.setBackgroundResource(R.drawable.redborder)
                                lastSelectedButton = button
                                selectedCell = button
                            }
                        }
                    }
                }
            }
        }
    }

    private fun generateSudokuBoard(): Array<IntArray> {
        val board = Array(4) { IntArray(4) }

        for (i in 0 until 4) {
            for (j in 0 until 4) {
                board[i][j] = 0
            }
        }

        val numbers = (1..4).shuffled()
        for (i in 0 until 4) {
            board[0][i] = numbers[i]
        }
        solveSudoku(board)


        return board
    }

    private fun solveSudoku(board: Array<IntArray>): Boolean {
        for (row in 0 until 4) {
            for (col in 0 until 4) {
                if (board[row][col] == 0) {
                    for (num in 1..4) {
                        if (isSafe(board, row, col, num)) {
                            board[row][col] = num
                            if (solveSudoku(board)) {
                                return true
                            }
                            board[row][col] = 0
                        }
                    }
                    return false
                }
            }
        }
        return true
    }

    private fun isSafe(board: Array<IntArray>, row: Int, col: Int, num: Int): Boolean {
        return !isInRow(board, row, num) && !isInCol(board, col, num) && !isInBox(
            board, row - row % 2, col - col % 2, num
        )
    }

    private fun isInRow(board: Array<IntArray>, row: Int, num: Int): Boolean {
        for (col in 0 until 4) {
            if (board[row][col] == num) {
                return true
            }
        }
        return false
    }

    private fun isInCol(board: Array<IntArray>, col: Int, num: Int): Boolean {
        for (row in 0 until 4) {
            if (board[row][col] == num) {
                return true
            }
        }
        return false
    }

    private fun isInBox(board: Array<IntArray>, startRow: Int, startCol: Int, num: Int): Boolean {
        for (row in 0 until 2) {
            for (col in 0 until 2) {
                if (board[row + startRow][col + startCol] == num) {
                    return true
                }
            }
        }
        return false
    }

    private fun getRandomPositions(totalPositions: Int): Set<Pair<Int, Int>> {
        val positionsToBlank = mutableSetOf<Pair<Int, Int>>()
        val random = Random.Default
        while (positionsToBlank.size < 3) {
            val randomRow = random.nextInt(0, 2)
            val randomCol = random.nextInt(0, 2)
            positionsToBlank.add(Pair(randomRow, randomCol))
        }
        return positionsToBlank
    }

    private fun printSolution(sudokuBoard: Array<IntArray>) {
        for (row in sudokuBoard) {
            for (num in row) {
                print("$num ")
            }
            println()
        }
    }

    fun getRandomSound(sounds: Array<MediaPlayer>): MediaPlayer {
        val random = Random
        val index = random.nextInt(sounds.size)
        return sounds[index]
    }

}
