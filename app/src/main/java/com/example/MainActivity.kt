package com.example.evparcial2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.evparcial2.ui.theme.EvParcial2Theme
import com.example.ui.venta.ListaVentasScreen
import com.example.ui.venta.VentaFormScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            EvParcial2Theme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "lista"
                ) {
                    composable("lista") {
                        ListaVentasScreen(
                            onNuevaVenta = {
                                navController.navigate("formulario")
                            }
                        )
                    }

                    composable("formulario") {
                        VentaFormScreen(
                            onVolver = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}
