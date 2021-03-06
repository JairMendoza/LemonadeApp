/*
 * Copyright (C) 2021 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.lemonade

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    /**
     * DO NOT ALTER ANY VARIABLE OR VALUE NAMES OR THEIR INITIAL VALUES.
     *
     * Anything labeled var instead of val is expected to be changed in the functions but DO NOT
     * alter their initial values declared here, this could cause the app to not function properly.
     */
    private val LEMONADE_STATE = "LEMONADE_STATE"
    private val LEMON_SIZE = "LEMON_SIZE"
    private val SQUEEZE_COUNT = "SQUEEZE_COUNT"
    private val LEMON_TREE_COUNT = "LEMON_TREE_COUNT"
    // SELECT represents the "pick lemon" state
    private val SELECT = "select"
    // SQUEEZE represents the "squeeze lemon" state
    private val SQUEEZE = "squeeze"
    // DRINK represents the "drink lemonade" state
    private val DRINK = "drink"
    // RESTART represents the state where the lemonade has be drunk and the glass is empty
    private val RESTART = "restart"
    // Default the state to select
    private var lemonadeState = "select"
    // Default lemonSize to -1
    private var lemonSize = -1
    // Default the squeezeCount to -1
    private var squeezeCount = -1
    // Default  the lemonTreeCount
    private var lemonTreeCount = 3

    private var lemonTree = LemonTree()
    private var lemonImage: ImageView? = null
    private var restartButton: Button? = null





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //val threeImage: ImageView = findViewById(R.id.imageViewThree)
        //threeImage.setOnClickListener{ clickLemonImage() }

        // === DO NOT ALTER THE CODE IN THE FOLLOWING IF STATEMENT ===
        if (savedInstanceState != null) {
            lemonadeState = savedInstanceState.getString(LEMONADE_STATE, "select")
            lemonSize = savedInstanceState.getInt(LEMON_SIZE, -1)
            squeezeCount = savedInstanceState.getInt(SQUEEZE_COUNT, -1)
            lemonTreeCount = savedInstanceState.getInt(LEMON_TREE_COUNT , 3)
        }
        // === END IF STATEMENT ===

        lemonImage = findViewById(R.id.image_lemon_state)
        setViewElements()
        lemonImage!!.setOnClickListener {
            // TODO: call the method that handles the state when the image is clicked
            clickLemonImage()
        }
        lemonImage!!.setOnLongClickListener {
            // TODO: replace 'false' with a call to the function that shows the squeeze count
            showSnackbar()
        }
        restartButton = findViewById(R.id.restartTreeButton)
        restartButton!!.setOnClickListener{
            if (lemonadeState == SELECT)
                restartLemonsTree()
        }


    }

    /**
     * === DO NOT ALTER THIS METHOD ===
     *
     * This method saves the state of the app if it is put in the background.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(LEMONADE_STATE, lemonadeState)
        outState.putInt(LEMON_SIZE, lemonSize)
        outState.putInt(SQUEEZE_COUNT, squeezeCount)
        outState.putInt(LEMON_TREE_COUNT, lemonTreeCount)
        super.onSaveInstanceState(outState)
    }

    /**
     * Clicking will elicit a different response depending on the state.
     * This method determines the state and proceeds with the correct action.
     */
    private fun clickLemonImage() {
        // TODO: use a conditional statement like 'if' or 'when' to track the lemonadeState
        //  when the the image is clicked we may need to change state to the next step in the
        //  lemonade making progression (or at least make some changes to the current state in the
        //  case of squeezing the lemon). That should be done in this conditional statement

        // TODO: When the image is clicked in the SELECT state, the state should become SQUEEZE
        //  - The lemonSize variable needs to be set using the 'pick()' method in the LemonTree class
        //  - The squeezeCount should be 0 since we haven't squeezed any lemons just yet.

        if (lemonadeState == SELECT) {
            if (lemonTreeCount == 0){
                showNoLemons()
            }
            else {
                lemonadeState = SQUEEZE
                lemonSize = lemonTree.pick()
                squeezeCount = 0
                lemonTreeCount -= 1
                setViewElements()
            }
        }

        // TODO: When the image is clicked in the SQUEEZE state the squeezeCount needs to be
        //  INCREASED by 1 and lemonSize needs to be DECREASED by 1.
        //  - If the lemonSize has reached 0, it has been juiced and the state should become DRINK
        //  - Additionally, lemonSize is no longer relevant and should be set to -1

        else if (lemonadeState == SQUEEZE) {
            squeezeCount += 1
            lemonSize -= 1
            if(lemonSize == 0){
                lemonadeState = DRINK
                setViewElements()
            }
            showSnackbar()
        }

        // TODO: When the image is clicked in the DRINK state the state should become RESTART

        else if (lemonadeState == DRINK) {
            lemonadeState = RESTART
            lemonSize = -1
            setViewElements()
        }

        // TODO: When the image is clicked in the RESTART state the state should become SELECT

        else {//RESTART
            lemonadeState = SELECT
            setViewElements()
        }

        // TODO: lastly, before the function terminates we need to set the view elements so that the
        //  UI can reflect the correct state

    }

    /**
     * Set up the view elements according to the state.
     */
    private fun setViewElements() {
        val textAction: TextView = findViewById(R.id.text_action)
        // TODO: set up a conditional that tracks the lemonadeState

        val state = when(lemonadeState){
            SELECT -> {
                val lemons = when(lemonTreeCount){
                    3 -> lemonImage?.setImageResource(R.drawable.lemon_tree)
                    2 -> lemonImage?.setImageResource(R.drawable.tree_with_2_lemons)
                    1 -> lemonImage?.setImageResource(R.drawable.tree_with_1_lemons)
                    else -> lemonImage?.setImageResource(R.drawable.tree_with_no_lemons)
                }
                textAction.setText("Click to select a lemon!")
            }
            SQUEEZE ->  {
                lemonImage?.setImageResource(R.drawable.lemon_squeeze)
                textAction.setText("Click to juice the lemon!")
                showSnackbar()
            }
            DRINK -> {
                lemonImage?.setImageResource(R.drawable.lemon_drink)
                textAction.setText("Click to drink your lemonade!")
            }
            else -> {
                lemonImage?.setImageResource(R.drawable.lemon_restart)
                textAction.setText("Click to start again!")
            }

        }

        // TODO: for each state, the textAction TextView should be set to the corresponding string from
        //  the string resources file. The strings are named to match the state

        // TODO: Additionally, for each state, the lemonImage should be set to the corresponding
        //  drawable from the drawable resources. The drawables have the same names as the strings
        //  but remember that they are drawables, not strings.
    }

    /**
     * === DO NOT ALTER THIS METHOD ===
     *
     * Long clicking the lemon image will show how many times the lemon has been squeezed.
     */
    private fun showSnackbar(): Boolean {
        if (lemonadeState != SQUEEZE) {
            return false
        }
        val squeezeText = getString(R.string.squeeze_count, squeezeCount)
        Snackbar.make(
            findViewById(R.id.constraint_Layout),
            squeezeText,
            Snackbar.LENGTH_SHORT
        ).show()
        return true
    }

    private fun showNoLemons(){
        val alertText = getString(R.string.alert_no_lemons)
        Snackbar.make(findViewById(R.id.constraint_Layout),
            alertText,Snackbar.LENGTH_SHORT).show()
    }

    private fun restartLemonsTree(){
        lemonTreeCount = 3
        lemonImage?.setImageResource(R.drawable.lemon_tree)
    }
}

/**
 * A Lemon tree class with a method to "pick" a lemon. The "size" of the lemon is randomized
 * and determines how many times a lemon needs to be squeezed before you get lemonade.
 */
class LemonTree {
    fun pick(): Int {
        return (2..6).random()
    }
}
