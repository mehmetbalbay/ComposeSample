package com.example.composesample1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composesample1.ui.theme.ComposeSample1Theme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeSample1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainCompose(innerPadding)
                }
            }
        }
    }
}

@Composable
fun MainCompose(innerPadding: PaddingValues? = null) {
    ShoppingCartApp()
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        color = Color.White,
        modifier = modifier.background(Color.Blue)
    )
}

@Composable
fun SimpleButton() {
    Button(onClick = {
        /* Handle Click */
    }) {
        Text("Tıklayın")
    }
}

@Composable
fun ShowImage() {
    Image(
        painter = painterResource(id = R.drawable.ic_launcher_foreground),
        contentDescription = "Logo"
    )
}

@Composable
fun ColumnLayout() {
    Column {
        Text("Birinci satır")
        Text("İkinci satır")
    }
}

@Composable
fun RowLayout() {
    Row {
        Text("Sol")
        Text("Sağ")
    }
}

@Composable
fun ClickCounter() {
    var count by remember { mutableStateOf(0) }

    Column {
        Text(text = "Butona $count kere tıkladınız.")
        Button(onClick = { count++ }) {
            Text("Tıkla")
        }
    }
}

data class Product(val id: Int, val name: String, val price: Double)

@Composable
fun ShoppingCartApp() {
    val products = listOf(
        Product(1, "Elma", 2.0),
        Product(2, "Armut", 3.5),
        Product(3, "Üzüm", 4.0)
    )

    val cartItems by remember { mutableStateOf(mutableStateMapOf<Product, Int>()) }
    val totalPrice = cartItems.entries.sumOf { it.key.price * it.value }

    val favoriteItems by remember { mutableStateOf(mutableStateListOf<Product>()) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Fiyat filtresi state'i
    var maxPrice by remember { mutableStateOf(10f) }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                Text(text = "Ürün Listesi", fontSize = 24.sp)
                Spacer(modifier = Modifier.height(16.dp))

                // Ürün listesi

                Text(
                    text = "Max Fiyat: ${maxPrice.toInt()} TL",
                    fontSize = MaterialTheme.typography.h4.fontSize
                )
                Slider(
                    value = maxPrice,
                    onValueChange = { maxPrice = it },
                    valueRange = 1f..10f,
                    steps = 9
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Filtrelenmiş ürün listesi
                val filteredProducts = products.filter { it.price <= maxPrice }

                filteredProducts.forEach { product ->
                    ProductItemWithSliderAndFavorite(
                        product = product,
                        cartItemCount = cartItems[product] ?: 0,
                        isFavorite = favoriteItems.contains(product),
                        onQuantityChange = { selectedProduct, newQuantity ->
                            if (newQuantity > 0) {
                                cartItems[selectedProduct] = newQuantity
                            } else {
                                cartItems.remove(selectedProduct)
                            }
                        },
                        onFavoriteToggle = { selectedProduct ->
                            if (favoriteItems.contains(selectedProduct)) {
                                favoriteItems.remove(selectedProduct)
                            } else {
                                favoriteItems.add(selectedProduct)
                            }
                        },
                        onAddToCart = {
                            cartItems[product] = (cartItems[product] ?: 0) + 1
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("${product.name} sepete eklendi!")
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Sepet bilgisi
                Text(text = "Sepetiniz", fontSize = 24.sp)
                cartItems.forEach { (product, count) ->
                    if (count > 0) {
                        Text(text = "${product.name} x $count - ${product.price * count} TL")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Toplam fiyat
                Text(text = "Toplam: $totalPrice TL", fontSize = 20.sp, color = Color.Red)
            }
        }
    )
}

@Composable
fun ProductItem(
    product: Product,
    cartItemCount: Int,
    onAddToCart: (Product) -> Unit,
    onRemoveFromCart: (Product) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = product.name, fontSize = 18.sp)
        Text(text = "${product.price} TL", fontSize = 18.sp)

        Row {
            Button(onClick = { onAddToCart(product) }) {
                Text("Sepete Ekle", maxLines = 1, fontSize = 10.sp)
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { onRemoveFromCart(product) },
                enabled = cartItemCount > 0
            ) {
                Text("Sepetten Çıkar", maxLines = 1, fontSize = 10.sp)
            }
        }
    }
}

@Composable
fun ProductItemWithSlider(
    product: Product,
    cartItemCount: Int,
    onQuantityChange: (Product, Int) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = product.name, fontSize = 18.sp)
        Text(text = "${product.price} TL", fontSize = 18.sp)

        // Slider ile ürün adedi seçimi
        Column {
            Text(text = "Adet: $cartItemCount")
            Slider(
                value = cartItemCount.toFloat(),
                onValueChange = { onQuantityChange(product, it.toInt()) },
                valueRange = 0f..10f,
                steps = 10
            )
        }
    }
}

@Composable
fun ProductItemWithSliderAndFavorite(
    product: Product,
    cartItemCount: Int,
    isFavorite: Boolean,
    onQuantityChange: (Product, Int) -> Unit,
    onFavoriteToggle: (Product) -> Unit,
    onAddToCart: (Product) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = product.name, fontSize = 18.sp)
        Text(text = "${product.price} TL", fontSize = 18.sp)

        // Slider ile ürün adedi seçimi
        Column {
            Text(text = "Adet: $cartItemCount")
            Slider(
                value = cartItemCount.toFloat(),
                onValueChange = { onQuantityChange(product, it.toInt()) },
                valueRange = 0f..10f,
                steps = 10
            )

            IconButton(onClick = { onFavoriteToggle(product) }) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = if (isFavorite) "Favori" else "Favorilere Ekle"
                )
            }

            Button(onClick = { onAddToCart(product) }) {
                Text(text = "Sepete Ekle")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeSample1Theme {
        MainCompose()
    }
}