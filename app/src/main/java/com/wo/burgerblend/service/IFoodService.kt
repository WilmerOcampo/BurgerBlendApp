package com.wo.burgerblend.service

import com.wo.burgerblend.domain.Food

interface IFoodService {
    fun foods(callback: (List<Food>) -> Unit)
}