package com.example.sudoku.view

import SudokuViewModel
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sudoku.R
import com.example.sudoku.databinding.LevelOneActivutyBinding
import com.example.sudoku.game.Cell
import com.example.sudoku.view.custom.SudokuBoard

class LevelOneActivity : AppCompatActivity(), SudokuBoard.OnTouchListener {

    private lateinit var viewModel: SudokuViewModel
    private lateinit var sudokuBoard: SudokuBoard
    private lateinit var binding: LevelOneActivutyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LevelOneActivutyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sudokuBoard = findViewById(R.id.sudokuBoard) // inicializar el board
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
        viewModel.sudokuGame.cellLiveData.observe(this, Observer {updateCells(it) })

        val buttons = listOf(binding.oneBtn, binding.twoBtn, binding.threeBtn, binding.fourBtn, binding.fiveBtn, binding.sixBtn, binding.sevenBtn, binding.eightBtn, binding.nineBtn)
        buttons.forEachIndexed { index, button ->

            button.setOnClickListener {
                viewModel.sudokuGame.handleInput(index+1)
            }
        }
    }

    private fun updateCells(cells:List<Cell>?) = cells?.let{
        sudokuBoard.updateCells(cells)
    }

    private fun updateSelectedCellUI(cell: Pair <Int, Int>) = cell.let {
        sudokuBoard.updateSelectedCellUI(cell.first, cell.second)
    }

    override fun onCellTouch(row:Int, col:Int){
        viewModel.sudokuGame.updateSelectedCell(row, col)
    }
}
