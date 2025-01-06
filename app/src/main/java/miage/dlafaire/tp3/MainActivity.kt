package miage.dlafaire.tp3

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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
        val rollButton: Button = findViewById(R.id.button)
        val targetNumberEditText: EditText = findViewById(R.id.targetNumber)

        rollButton.visibility = View.GONE

        targetNumberEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                rollButton.visibility = if (!s.isNullOrEmpty() && s.toString().toIntOrNull() != null) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        rollButton.setOnClickListener {
            rollDice()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun rollDice() {
        val dice1 = Dice(6)
        val dice2 = Dice(6)
        val diceRoll1 = dice1.roll()
        val diceRoll2 = dice2.roll()

        val resultTextView1: TextView = findViewById(R.id.textView)
        val resultTextView2: TextView = findViewById(R.id.textView2)
        resultTextView1.text = diceRoll1.toString()
        resultTextView2.text = diceRoll2.toString()

        val targetNumberEditText: EditText = findViewById(R.id.targetNumber)
        val targetNumber = targetNumberEditText.text.toString().toIntOrNull()

        if (targetNumber != null) {
            val sum = diceRoll1 + diceRoll2
            if (sum == targetNumber) {
                Toast.makeText(this, "Félicitations, vous avez gagné", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Essayez encore", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Entrez un nombre valide", Toast.LENGTH_SHORT).show()
        }
    }
}

class Dice(val numSides: Int) {
    fun roll(): Int {
        return (1..numSides).random()
    }
}