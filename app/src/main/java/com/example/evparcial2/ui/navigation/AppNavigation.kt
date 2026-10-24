package com.example.evparcial2.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.evparcial2.ui.login.LoginScreen
import com.example.evparcial2.ui.menu.MenuScreen
import com.example.evparcial2.ui.venta.ListaVentasScreen
import com.example.evparcial2.ui.venta.VentaFormScreen

/**
 * Rutas de la app.
 * Secuencia: Login -> Menú -> ListaVentasScreen -> VentaFormScreen
 */
object Rutas {
    const val LOGIN = "login"
    const val MENU = "menu"
    const val LISTA = "lista"
    const val ARG_VENTA_ID = "ventaId"
    const val FORMULARIO = "formulario?$ARG_VENTA_ID={$ARG_VENTA_ID}"

    fun nuevaVenta(): String = "formulario"
    fun editarVenta(id: Long): String = "formulario?$ARG_VENTA_ID=$id"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Rutas.LOGIN
    ) {
        composable(Rutas.LOGIN) {
            LoginScreen(
                onLoginExitoso = {
                    navController.navigate(Rutas.MENU) {
                        // Al volver atrás desde el menú no se regresa al login
                        popUpTo(Rutas.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Rutas.MENU) {
            MenuScreen(
                onVerVentas = {
                    navController.navigate(Rutas.LISTA)
                },
                onNuevaVenta = {
                    // Pasa por la lista para que al guardar se vea la venta nueva
                    navController.navigate(Rutas.LISTA)
                    navController.navigate(Rutas.nuevaVenta())
                },
                onCerrarSesion = {
                    navController.navigate(Rutas.LOGIN) {
                        popUpTo(Rutas.MENU) { inclusive = true }
                    }
                }
            )
        }

        composable(Rutas.LISTA) {
            ListaVentasScreen(
                onNuevaVenta = {
                    navController.navigate(Rutas.nuevaVenta())
                },
                onSeleccionarVenta = { id ->
                    navController.navigate(Rutas.editarVenta(id))
                },
                onVolverMenu = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Rutas.FORMULARIO,
            arguments = listOf(
                navArgument(Rutas.ARG_VENTA_ID) {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong(Rutas.ARG_VENTA_ID) ?: -1L

            VentaFormScreen(
                ventaId = if (id > 0L) id else null,
                onVolver = {
                    navController.popBackStack()
                }
            )
        }
    }
}
