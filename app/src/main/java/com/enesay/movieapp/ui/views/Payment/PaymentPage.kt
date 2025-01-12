import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.enesay.movieapp.R
import com.enesay.movieapp.data.model.Card
import com.enesay.movieapp.data.model.MovieCart
import com.enesay.movieapp.data.model.Order
import com.enesay.movieapp.ui.theme.PrimaryBlack
import com.enesay.movieapp.ui.views.Auth.Login.LoginViewModel
import com.enesay.movieapp.ui.views.Cart.CartItem
import com.enesay.movieapp.ui.views.Order.OrderState
import com.enesay.movieapp.ui.views.Payment.PaymentViewModel
import com.enesay.movieapp.utils.Constants.IMAGE_BASE_URL
import com.google.gson.Gson
import com.skydoves.landscapist.glide.GlideImage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentPage(navController: NavHostController, cartItems: List<MovieCart>) {
    val paymentViewModel = hiltViewModel<PaymentViewModel>()
    val loginViewModel = hiltViewModel<LoginViewModel>()
    val cards by paymentViewModel.cards.collectAsState()
    val currentUser = loginViewModel.currentUser.value
    val isCardSelected = remember { mutableStateOf(false) }
    val totalPrice = cartItems.sumOf { it.price * it.orderAmount }
    val orderState by paymentViewModel.orderState.collectAsState()

    val groupedMovies by remember {
        derivedStateOf {
            cartItems.groupBy { it.name }.mapValues { entry ->
                mapOf(
                    "amount" to entry.value.sumOf { it.orderAmount },
                    "ids" to entry.value.map { it.cartId }
                )
            }
        }
    }

    val movieCartList = groupedMovies.map { (name, details) ->
        // Get all movies with the same name
        val moviesInGroup = cartItems.filter { it.name == name }

        // Use the first movie as a representative and aggregate the necessary fields
        val representativeMovie = moviesInGroup.first()

        MovieCart(
            cartId = representativeMovie.cartId, // Combine cart IDs into a unique hash
            name = representativeMovie.name,
            image = representativeMovie.image,
            price = representativeMovie.price,
            category = representativeMovie.category,
            rating = representativeMovie.rating,
            year = representativeMovie.year,
            director = representativeMovie.director,
            description = representativeMovie.description,
            orderAmount = details["amount"] as Int, // Use the aggregated amount
            userName = representativeMovie.userName
        )
    }

    Log.d("PaymentPage", "movieCartList: $movieCartList")


    // Fetch user data on first render
    LaunchedEffect(Unit) {
        currentUser?.let {
            paymentViewModel.fetchCards(it)
            paymentViewModel.fetchOrders(it)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Payment",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp)
    ) { paddingValues ->

        when (orderState) {
            is OrderState.Success -> {
                navController.navigate("orders")
            }
            is OrderState.Error -> {
                // Show an error message (optional)
            }
            is OrderState.Loading -> {
                CircularProgressIndicator()
            }
            is OrderState.Idle -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    // Section: Saved Cards
                    Text(
                        text = stringResource(R.string.txt_saved_cards),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start
                    )

                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (cards.isNotEmpty()) {
                            items(cards) { card ->
                                CardView(card)
                            }
                        } else {
                            item {
                                Text(
                                    text = "No saved cards.",
                                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
                                )
                            }
                        }
                        item {
                            AddCardButton {
                                navController.navigate("addCard")
                            }
                        }
                    }

                    HorizontalDivider()

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        Text(
                            text = stringResource(R.string.txt_your_items),
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start
                        )
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(2f)
                        ) {
                            items(
                                groupedMovies.keys.toList(),
                                key = { movieName -> movieName.hashCode() }) { movieName ->
                                val movieInfo = groupedMovies[movieName]
                                val orderAmount = movieInfo?.get("amount") as? Int ?: 0
                                val cartIds = movieInfo?.get("ids") as? List<Int> ?: emptyList()

                                val movie = cartItems.first { it.name == movieName }

                                OrderItem(
                                    movie = movie.copy(orderAmount = orderAmount, cartId = cartIds.first()),
                                )
                            }
                        }

                        HorizontalDivider()

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .weight(0.6f),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Total price
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    text = stringResource(R.string.txt_total) + ":",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 24.sp,
                                )
                                Text(
                                    text = "$totalPrice₺",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 24.sp,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                            // Section: Confirm Payment Button
                            Button(
                                onClick = {
                                    // Handle order confirmation logic here
                                    // Format the current timestamp
                                    val dateFormatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                                    val formattedDate = dateFormatter.format(Date(System.currentTimeMillis()))

                                    val order = Order(orderContents = movieCartList, orderDate = formattedDate, totalPrice = totalPrice)
                                    if (currentUser != null) {
                                        paymentViewModel.addOrder(currentUser, order)
                                    }

                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onPrimary)
                            ) {
                                Text(
                                    text = stringResource(R.string.txt_confirm_payment),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

    }
}

@Composable
fun CardView(card: Card) {
    Card(
        modifier = Modifier
            .size(180.dp, 100.dp)
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = card.cardName,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 18.sp
            )
            Text(
                text = "•••• •••• ••••  ${card.cardNumber.takeLast(4)}",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun AddCardButton(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .size(180.dp, 100.dp)
            .clickable(onClick = onClick),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimary),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Card",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
fun OrderItem(movie: MovieCart) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 10.dp)
            .background(Color.LightGray.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        GlideImage(
            modifier = Modifier
                .size(90.dp, 110.dp)
                .clip(RoundedCornerShape(8.dp)),
            imageModel = "$IMAGE_BASE_URL${movie.image}",
            contentScale = ContentScale.Crop
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = movie.name,
                fontWeight = FontWeight.Bold,
                fontSize = 21.sp
            )
            Text(
                text = "Quantity: ${movie.orderAmount}",
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                fontSize = 15.sp
            )
        }

        Column(
            modifier = Modifier.padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${movie.price * movie.orderAmount}₺",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 20.sp
            )
        }
    }
}
