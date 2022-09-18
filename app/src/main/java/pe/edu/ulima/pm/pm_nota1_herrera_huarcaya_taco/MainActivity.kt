package pe.edu.ulima.pm.pm_nota1_herrera_huarcaya_taco

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import pe.edu.ulima.pm.pm_nota1_herrera_huarcaya_taco.databinding.ActivityMainBinding
import java.util.*


class MainActivity : AppCompatActivity() {
    enum class Turn{
        O,
        X
    }

    //Primer turno es 0
    private var firstTurn = Turn.O
    //Esto para que pinte en el textView
    private var currentTurn = Turn.O

    //Binding: una forma para vincular las vistas
    private lateinit var binding: ActivityMainBinding

    //Lista mutable de botones
    private var boardList = mutableListOf<Button>()

    private var Letras = listOf<String>("α","β","γ","δ","ε","ζ",
        "η","θ","ι","κ","λ","μ","ν","ξ","ο","π","ρ","σ","ς","τ",
        "θ","φ","χ","ψ","ω")

    private var colour = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Parte del binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initMichi()
        binding.turnoJugador.setOnClickListener {
            resetBoard()
        }
    }

    //Agregamos los botones a la lista mutable
    private fun initMichi() {
        boardList.add(binding.x1)
        boardList.add(binding.x2)
        boardList.add(binding.x3)
        boardList.add(binding.y1)
        boardList.add(binding.y2)
        boardList.add(binding.y3)
        boardList.add(binding.z1)
        boardList.add(binding.z2)
        boardList.add(binding.z3)
    }
    private fun closeGame() {
        for(button in boardList){
            button.setOnClickListener {
                resetBoard()
            }
        }
    }
    private fun openGame(){
        for(button in boardList){
            button.setOnClickListener {
                boardTapped(button)
            }
        }
    }
    //Checa
    fun boardTapped(view : View){
        if(view !is Button)
            return
        addToBoard(view)
        //gana 0
        if(checkForVictory(O)){
            result("O")
            closeGame()
        }
        //gana X
        if(checkForVictory(X)){
            result("X")
            closeGame()
        }
        //empate
        if(fullBoard() && !checkForVictory(X) && !checkForVictory(O) ){
            result("Draw")
            closeGame()
        }
    }

    private fun checkForVictory(s: String): Boolean {
        if(match(binding.x1, s) && match(binding.x2, s) && match(binding.x3, s))
            return true
        if(match(binding.y1, s) && match(binding.y2, s) && match(binding.y3, s))
            return true
        if(match(binding.z1, s) && match(binding.z2, s) && match(binding.z3, s))
            return true

        if(match(binding.x1, s) && match(binding.y1, s) && match(binding.z1, s))
            return true
        if(match(binding.x3, s) && match(binding.y3, s) && match(binding.z3, s))
            return true
        if(match(binding.x2, s) && match(binding.y2, s) && match(binding.z2, s))
            return true

        if(match(binding.x1, s) && match(binding.y2, s) && match(binding.z3, s))
            return true
        if(match(binding.x3, s) && match(binding.y2, s) && match(binding.z1, s))
            return true

        return false
    }

    private fun match(button: Button, symbol : String): Boolean = button.text == symbol

    private fun result(s: String) {
        if(s == "X"){
            binding.turnoJugador.text = "Ganador $X"
        }else if(s == "O"){
            binding.turnoJugador.text = "Ganador $O"
        }else{
            binding.turnoJugador.text = "Empate"
        }

    }

    fun getRandomColor(): Int {
        val rnd = Random()
        return  Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }

    private fun resetBoard() {
        for(button in boardList){
            button.text = ""
            colour = getRandomColor()
            binding.layoutTicTacToe.setBackgroundColor(colour)
        }


        O = Letras.random()
        X = Letras.random()
        while (O == X){
            X = Letras.random()
        }
        if(firstTurn ==Turn.O)
            firstTurn = Turn.X
        else if(firstTurn == Turn.X)
            firstTurn = Turn.O

        currentTurn = firstTurn
        setTurnLabel()
        openGame()
    }

    private fun fullBoard(): Boolean {
        for(button in boardList){
            if(button.text=="")
                return false
        }
        return true
    }

    private fun addToBoard(btn: Button) {
        if(btn.text!="")
            return
        if(currentTurn ==Turn.O){
            btn.text = O
            currentTurn = Turn.X
        }

        else if(currentTurn ==Turn.X){
            btn.text = X
            currentTurn = Turn.O
        }
        setTurnLabel()
    }

    private fun setTurnLabel() {
        var turnText =""
        if(currentTurn == Turn.X)
            turnText ="Turn $X"
        else if(currentTurn == Turn.O)
            turnText = "Turn $O"

        binding.turnoJugador.text = turnText
    }

    companion object{
        var O = "O"
        var X = "X"
    }
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putCharSequence("x1", binding.x1.text )
        outState.putCharSequence("x2", binding.x2.text )
        outState.putCharSequence("x3", binding.x3.text )
        outState.putCharSequence("y1", binding.y1.text )
        outState.putCharSequence("y2", binding.y2.text )
        outState.putCharSequence("y3", binding.y3.text )
        outState.putCharSequence("z1", binding.z1.text )
        outState.putCharSequence("z2", binding.z2.text )
        outState.putCharSequence("z3", binding.z3.text )
        outState.putCharSequence("turnoJugador", binding.turnoJugador.text)
        outState.putInt("color", colour)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        binding.x1.text = savedInstanceState.getCharSequence("x1", "")
        binding.x2.text = savedInstanceState.getCharSequence("x2", "")
        binding.x3.text = savedInstanceState.getCharSequence("x3", "")
        binding.y1.text = savedInstanceState.getCharSequence("y1", "")
        binding.y2.text = savedInstanceState.getCharSequence("y2", "")
        binding.y3.text = savedInstanceState.getCharSequence("y3", "")
        binding.z1.text = savedInstanceState.getCharSequence("z1", "")
        binding.z2.text = savedInstanceState.getCharSequence("z2", "")
        binding.z3.text = savedInstanceState.getCharSequence("z3", "")
        binding.turnoJugador.text = savedInstanceState.getCharSequence("turnoJugador", "")
        colour = savedInstanceState.getInt("color",0)
        binding.layoutTicTacToe
        super.onRestoreInstanceState(savedInstanceState)
    }

}