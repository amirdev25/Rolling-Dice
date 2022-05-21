package uz.amirdev.dicerolltask

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import uz.amirdev.dicerolltask.databinding.ActivityMainBinding
import uz.amirdev.dicerolltask.databinding.LayoutDialogBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private var state = MutableLiveData<Int>()
    private val arrayImages = arrayOf(
        R.drawable.ic_1,
        R.drawable.ic_2,
        R.drawable.ic_3,
        R.drawable.ic_4,
        R.drawable.ic_5,
        R.drawable.ic_6
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        state.value = 1
        binding.rollBtn.setOnClickListener {
            if (binding.finalScore.text.isEmpty()) {
                binding.finalScore.error = "Please fill this field!"
            } else {
                rollDice()
            }
        }
        binding.holdBtn.setOnClickListener {
            hold()
        }
        binding.newGameBtn.setOnClickListener {
            newGame()
        }

        setObserve()

    }

    private fun setObserve() {
        state.observe(this) {
            when (it) {
                1 -> {
                    binding.player1.isEnabled = true
                    binding.player2.isEnabled = false
                    binding.player1Indicator.visibility = View.VISIBLE
                    binding.player2Indicator.visibility = View.GONE
                }
                2 -> {
                    binding.player1.isEnabled = false
                    binding.player2.isEnabled = true
                    binding.player1Indicator.visibility = View.GONE
                    binding.player2Indicator.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun hold() {
        if (state.value == 1) {
            val current = Integer.parseInt(binding.player1Current.text.toString())
            var player1Score = Integer.parseInt(binding.player1Score.text.toString())
            player1Score += current
            binding.player1Score.text = player1Score.toString()
            binding.player1Current.text = "0"
            if (Integer.parseInt(binding.finalScore.text.toString()) <= player1Score) {
                showAlertDialog(1)
            }

        } else {
            val current = Integer.parseInt(binding.player2Current.text.toString())
            var player2Score = Integer.parseInt(binding.player2Score.text.toString())
            player2Score += current
            binding.player2Score.text = player2Score.toString()
            binding.player2Current.text = "0"
            if (Integer.parseInt(binding.finalScore.text.toString()) <= player2Score) {
                showAlertDialog(2)
            }
        }
    }


    private fun showAlertDialog(i: Int) {
        val alertDialog =
            AlertDialog.Builder(binding.rollBtn.context, R.style.CustomDialogTheme).create()
        val view = LayoutDialogBinding.inflate(layoutInflater)

        view.restartBtn.setOnClickListener {
            alertDialog.dismiss()
            newGame()
        }
        view.logOutBtn.setOnClickListener {
            finish()
        }

        if (i == 1) {
            view.winnerTv.text = "Winner: Player1"
        } else {
            view.winnerTv.text = "Winner: Player2"
        }
        alertDialog.setCancelable(false)
        alertDialog.setView(view.root)
        alertDialog.show()

    }

    private fun newGame() {
        binding.dice1.setImageResource(R.drawable.ic_resource_default)
        binding.dice2.setImageResource(R.drawable.ic_resource_default)
        binding.player1Current.text = "0"
        binding.player2Current.text = "0"
        binding.player1Score.text = "0"
        binding.player2Score.text = "0"
        binding.finalScore.text = null
        state.value = 1
    }

    @SuppressLint("SetTextI18n")
    private fun rollDice() {
        val number1 = getRandomNumber()
        val number2 = getRandomNumber()
        val number = number1 + 1 + number2 + 1
        binding.dice1.setImageResource(arrayImages[number1])
        binding.dice2.setImageResource(arrayImages[number2])
        if (state.value == 1) {
            val current = Integer.parseInt(binding.player1Current.text.toString())
            binding.player1Current.text = (current + number).toString()
            if (number1 == number2) {
                hold()
                state.value = 2
            }
        } else {
            val current = Integer.parseInt(binding.player2Current.text.toString())
            binding.player2Current.text = (current + number).toString()
            if (number1 == number2) {
                hold()
                state.value = 1
            }
        }
    }

    private fun getRandomNumber() = (0..5).random()

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}