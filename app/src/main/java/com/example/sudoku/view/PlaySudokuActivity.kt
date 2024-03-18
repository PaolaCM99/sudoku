package com.example.sudoku.view

import SudokuViewModel
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sudoku.R
import com.example.sudoku.view.custom.SudokuBoard

class MainActivity : AppCompatActivity(), SudokuBoard.OnTouchListener {

    private lateinit var viewModel: SudokuViewModel
    private lateinit var sudokuBoard: SudokuBoard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() //NO ESTA EN EL VIDEO
        setContentView(R.layout.activity_main)

        sudokuBoard.registerListener(this)

        //REVISAR ESTE BLOQUE DE CODIGO - NO ESTA EN EL VIDEO
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //
        viewModel = ViewModelProvider(this).get(SudokuViewModel::class.java)
        viewModel.sudokuGame.selectedCellLiveData.observe(this, Observer { updateSelectedCellUI(it)})
    }

    private fun updateSelectedCellUI(cell: Pair <Int, Int>) = cell.let {
        sudokuBoard.updateSelectedCellUI(cell.first, cell.second)
    }

    override fun onCellTouch(row:Int, col:Int){
        viewModel.sudokuGame.updateSelectedCell(row, col)
    }
}
