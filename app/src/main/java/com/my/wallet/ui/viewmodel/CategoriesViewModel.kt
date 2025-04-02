package com.my.wallet.ui.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.wallet.data.repository.WalletRepository
import com.my.wallet.model.CategoryIcon
import com.my.wallet.model.TransactionCategory
import com.my.wallet.model.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val repository: WalletRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoriesUiState())
    val uiState: StateFlow<CategoriesUiState> = _uiState.asStateFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        // TODO: Загрузка категорий из репозитория
        // Пока используем тестовые данные
        _uiState.update { it.copy(
            categories = listOf(
                CategoryUiModel(
                    category = TransactionCategory(
                        name = "Продукты",
                        type = TransactionType.EXPENSE,
                        iconName = CategoryIcon.FOOD.iconName
                    ),
                    color = Color(0xFF4CAF50)
                )
                // Добавьте другие категории по умолчанию
            )
        )}
    }

    fun addCategory(
        name: String,
        type: TransactionType,
        icon: CategoryIcon,
        color: Color
    ) {
        val newCategory = CategoryUiModel(
            category = TransactionCategory(
                name = name,
                type = type,
                iconName = icon.iconName
            ),
            color = color
        )
        
        _uiState.update { 
            it.copy(categories = it.categories + newCategory)
        }
        // TODO: Сохранение в репозиторий
    }

    fun deleteCategory(category: CategoryUiModel) {
        _uiState.update { 
            it.copy(categories = it.categories - category)
        }
        // TODO: Удаление из репозитория
    }

    data class CategoriesUiState(
        val categories: List<CategoryUiModel> = emptyList(),
        val error: String? = null
    )

    data class CategoryUiModel(
        val category: TransactionCategory,
        val color: Color
    )
} 