package com.example.exam_communication_without_internet

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.CallSuper
import com.example.exam_communication_without_internet.databinding.ActivityMainBinding
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.ConnectionsClient
import com.google.android.gms.nearby.connection.Strategy
import java.util.Random

class MainActivity : AppCompatActivity() {

    private enum class GameChoice {
        ROCK, PAPER, SCISSORS;

        fun beats(other: GameChoice): Boolean {
            return (this == ROCK && other == SCISSORS)
                    || (this == SCISSORS && other == PAPER)
                    || (this == PAPER && other == ROCK)
        }
    }

    internal object CodenameGenerator {
        private val COLORS = arrayOf(
            "Red", "Orange", "Yellow", "Green", "Blue", "Indigo", "Violet", "Purple", "Lavender"
        )
        private val TREATS = arrayOf(
            "Cupcake", "Donut", "Eclair", "Froyo", "Gingerbread", "Honeycomb",
            "Ice Cream Sandwich", "Jellybean", "Kit Kat", "Lollipop", "Marshmallow", "Nougat",
            "Oreo", "Pie"
        )
        private val generator = Random()

        fun generate(): String {
            val color = COLORS[generator.nextInt(COLORS.size)]
            val treat = TREATS[generator.nextInt(TREATS.size)]
            return "$color $treat"
        }
    }

    private val STRATEGY = Strategy.P2P_STAR

    private lateinit var connectionsClient: ConnectionsClient

    private val REQUEST_CODE_REQUIRED_PERMISSIONS = 1

    private var opponentName: String? = null
    private var opponentEndpointId: String? = null
    private var opponentScore = 0
    private var opponentChoice: GameChoice? = null

    private var myName: String? = null
    private var myScore = 0
    private var myChoice: GameChoice? = null

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        connectionsClient = Nearby.getConnectionsClient(this)
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=  PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_REQUIRED_PERMISSIONS
            )
        }
    }

    @CallSuper
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val errMsg = "Cannot start without required permissions"
        if (requestCode == REQUEST_CODE_REQUIRED_PERMISSIONS) {
            grantResults.forEach {
                if (it == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this, errMsg, Toast.LENGTH_SHORT).show()
                    finish()
                    return
                }
            }
            recreate()
        }
    }
}