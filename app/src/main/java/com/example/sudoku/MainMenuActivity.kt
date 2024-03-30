package com.example.sudoku

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.LocalContext
import com.example.sudoku.databinding.MainMenuActivityBinding
import com.example.sudoku.view.LevelOneActivity


class MainMenuActivity : AppCompatActivity() {
    lateinit var binding: MainMenuActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainMenuActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.level1Btn.setOnClickListener {
            val intent = Intent(this, LevelOneActivity::class.java)
            startActivity(intent)
        }
        binding.level2Btn.setOnClickListener {
            val intent = Intent(this, LevelTwoActivity::class.java)
            startActivity(intent)
        }
        binding.level3Bnt.setOnClickListener {
            val intent = Intent(this, LevelThreeActivity::class.java)
            startActivity(intent)
        }


    }
}
