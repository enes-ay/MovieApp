package com.enesay.movieapp.ui.views
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.enesay.movieapp.R

sealed class BottomNavItem(val route: String, @DrawableRes val iconId: Int, @StringRes val label : Int) {
    object Profile : BottomNavItem("profile",  R.drawable.ic_favorite, R.string.item_favorite)
    object Home : BottomNavItem("home",  R.drawable.ic_movie, R.string.item_movie)
    object List : BottomNavItem("cart", R.drawable.ic_cart, R.string.item_cart)
}