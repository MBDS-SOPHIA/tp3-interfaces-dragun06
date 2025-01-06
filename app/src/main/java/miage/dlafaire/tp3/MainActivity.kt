package miage.dlafaire.tp3

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val targetNumberEditText: EditText = findViewById(R.id.targetNumber)

        targetNumberEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val targetNumber = s.toString().toIntOrNull()
                if (targetNumber != null) {
                    rollDice(targetNumber)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun rollDice(targetNumber: Int) {
        val dice1 = Dice(6)
        val dice2 = Dice(6)
        val diceImage1: ImageView = findViewById(R.id.diceImage1)
        val diceImage2: ImageView = findViewById(R.id.diceImage2)

        shuffleDice(diceImage1, diceImage2) {
            val diceRoll1 = dice1.roll()
            val diceRoll2 = dice2.roll()
            setDiceImage(diceImage1, diceRoll1)
            setDiceImage(diceImage2, diceRoll2)

            val sum = diceRoll1 + diceRoll2
            if (sum == targetNumber) {
                Toast.makeText(this, "Félicitations, vous avez gagné", Toast.LENGTH_SHORT).show()
                animateDice(diceImage1, diceImage2)
            } else {
                Toast.makeText(this, "Essayez encore", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setDiceImage(diceImage: ImageView, roll: Int) {
        val drawableResource = when (roll) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            else -> R.drawable.dice_6
        }
        diceImage.setImageResource(drawableResource)
    }

    private fun shuffleDice(dice1: ImageView, dice2: ImageView, onComplete: () -> Unit) {
        val handler = Handler(Looper.getMainLooper())
        val shuffleDuration = 1000L
        val shuffleInterval = 100L
        val endTime = System.currentTimeMillis() + shuffleDuration

        val shuffleTask = object : Runnable {
            override fun run() {
                if (System.currentTimeMillis() < endTime) {
                    setDiceImage(dice1, (1..6).random())
                    setDiceImage(dice2, (1..6).random())
                    handler.postDelayed(this, shuffleInterval)
                } else {
                    onComplete()
                }
            }
        }
        handler.post(shuffleTask)
    }

    private fun animateDice(dice1: ImageView, dice2: ImageView) {
        val animator1 = ObjectAnimator.ofFloat(dice1, "translationY", -100f, 0f)
        animator1.duration = 500
        animator1.repeatCount = 3
        animator1.start()

        val animator2 = ObjectAnimator.ofFloat(dice2, "translationY", -100f, 0f)
        animator2.duration = 500
        animator2.repeatCount = 3
        animator2.start()
    }
}

class Dice(val numSides: Int) {
    fun roll(): Int {
        return (1..numSides).random()
    }
}