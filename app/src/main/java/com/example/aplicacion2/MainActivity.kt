package com.example.aplicacion2

import android.widget.Button
import android.os.Bundle
import android.widget.ListView
import android.widget.ArrayAdapter
import android.content.Intent
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity(){
    private var hasSelectedProduct = false
    private lateinit var products: List<Producto>
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        products = listOf(
            Producto(1, "Smartphone X", "Último modelo con cámara de 108MP", 799.99, R.drawable.celular),
            Producto(2, "Laptop Pro", "16GB RAM, 512GB SSD", 1299.99, R.drawable.laptop),
            Producto(3, "Audífonos Premium", "Cancelación de ruido activa", 299.99, R.drawable.audifonos),
            Producto(4, "Tableta Ultra", "10.5 pulgadas, 128GB", 499.99, R.drawable.tablet)
        )

        onBackPressedDispatcher.addCallback(this, object :  OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                mostrarConfirmacionSalida()
            }

        })
        listView = findViewById(R.id.lvProductos)
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            products.map { it.nombre }
        )
        listView.adapter = adapter

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            hasSelectedProduct = true
            val productoSeleccionado = products[position]
            mostrarDetallesProducto(productoSeleccionado)
        }

        val btnDetalles = findViewById<Button>(R.id.btnDetalles)
        btnDetalles.setOnClickListener {
            if (hasSelectedProduct) {
                mostrarMensaje("Producto seleccionado", "¡Has seleccionado un producto!")
            } else {
                mostrarMensaje("Aviso", "Por favor, selecciona un producto primero")
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!hasSelectedProduct) {
                    mostrarConfirmacionSalida()
                } else {
                    finish()
                }
            }
        })
    }

    private fun mostrarDetallesProducto(producto: Producto) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_producto_detalle, null)

        val imageView = dialogView.findViewById<ImageView>(R.id.ivProductoDetalle)
        val descripcionView = dialogView.findViewById<TextView>(R.id.tvDescripcionDetalle)
        val precioView = dialogView.findViewById<TextView>(R.id.tvPrecioDetalle)

        imageView.setImageResource(producto.imagenResourceId)
        descripcionView.text = "Descripción: ${producto.descripcion}"
        precioView.text = "Precio: $${producto.precio}"

        AlertDialog.Builder(this)
            .setTitle(producto.nombre)
            .setView(dialogView)
            .setPositiveButton("Seleccionar") { _, _ ->
                Toast.makeText(this, "Seleccionando producto...", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cerrar", null)
            .show()
    }

    private fun mostrarMensaje(titulo: String, mensaje: String) {
        AlertDialog.Builder(this)
            .setTitle(titulo)
            .setMessage(mensaje)
            .setPositiveButton("Ok", null)
            .show()
    }

    private fun mostrarConfirmacionSalida() {
        AlertDialog.Builder(this)
            .setTitle("Confirmación")
            .setMessage("¿Seguro que quieres salir sin seleccionar un producto?")
            .setPositiveButton("Sí") { _, _ -> finish() }
            .setNegativeButton("No", null)
            .show()
    }
}