package com.example.tphci.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.tphci.R

enum class AppDestinations(
    @param:StringRes val label: Int,
    @param:DrawableRes val icon: Int,
    @param:StringRes val contentDescription: Int
) {
    SHOPPING_LISTS(R.string.shopping_lists, R.drawable.shopping_cart_24px, R.string.shopping_lists),
    PRODUCTS(R.string.products, R.drawable.package_2_24px, R.string.products),
    PROFILE(R.string.profile, R.drawable.person_24px, R.string.profile),
}
