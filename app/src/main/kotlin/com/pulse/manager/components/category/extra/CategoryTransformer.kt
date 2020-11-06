package com.pulse.manager.components.category.extra

import com.pulse.manager.components.category.model.Category

val List<Category>.flattenCategories
    get(): List<Category> {
        fun mapCategoryToList(category: Category): List<Category> {
            val nestedCategories = mutableListOf<Category>()
            if (category.code.length == 1) nestedCategories.add(category)
            category.nodes?.onEach { nestedCategories.addAll(mapCategoryToList(it)) }?.let(nestedCategories::addAll)
            return nestedCategories
        }

        return map { mapCategoryToList(it) }.flatten().onEach {
            it.nestedCategories = it.nodes?.map { it.code }
        }
    }