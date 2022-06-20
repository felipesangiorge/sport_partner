package com.find_sport_partner.find_sport_partner.extensions

import android.view.View

var View.toVisibility: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }