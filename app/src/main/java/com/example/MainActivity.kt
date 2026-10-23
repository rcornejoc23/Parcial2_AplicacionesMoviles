package com.example.evparcial2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.evparcial2.ui.theme.EvParcial2Theme
import com.example.ui.venta.VentaFormScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            EvParcial2Theme {
                VentaFormScreen()
            }
        }
    }
}