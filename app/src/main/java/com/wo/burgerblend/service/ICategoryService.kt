package com.wo.burgerblend.service

import com.wo.burgerblend.domain.Category

interface ICategoryService {
    fun categories(callback: (List<Category>) -> Unit)
}