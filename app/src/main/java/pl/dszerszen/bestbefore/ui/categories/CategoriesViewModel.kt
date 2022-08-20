package pl.dszerszen.bestbefore.ui.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.dszerszen.bestbefore.domain.product.interactor.AddCategoryUseCase
import pl.dszerszen.bestbefore.domain.product.interactor.DeleteCategoryUseCase
import pl.dszerszen.bestbefore.domain.product.interactor.GetCategoriesUseCase
import pl.dszerszen.bestbefore.domain.product.model.Category
import pl.dszerszen.bestbefore.domain.product.model.CategoryIcon
import pl.dszerszen.bestbefore.util.runWhenSuccess
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val addCategoryUseCase: AddCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase
) : ViewModel() {
    private val _viewState = MutableStateFlow(CategoriesViewState())
    val viewState = _viewState.asStateFlow()

    private var editedCategoryId: String? = null

    init {
        viewModelScope.launch {
            getCategoriesUseCase().collect { response ->
                response.runWhenSuccess { categories ->
                    _viewState.update { it.copy(categories = categories) }
                }
            }
        }
    }

    fun onUiIntent(intent: CategoriesScreenUiIntent) {
        when (intent) {
            CategoriesScreenUiIntent.OnAddClicked -> editCategory(null)
            is CategoriesScreenUiIntent.OnEditClicked -> editCategory(intent.category)
            is CategoriesScreenUiIntent.OnCategoryDeleted -> deleteCategory(intent.category)
            is CategoriesScreenUiIntent.OnCategorySaved -> addCategory()
            is CategoriesScreenUiIntent.OnNameEdited -> {
                _viewState.update { it.copy(editedCategoryName = intent.name) }
            }
            is CategoriesScreenUiIntent.OnIconEdited -> {
                _viewState.update { it.copy(editedCategoryIcon = intent.icon) }

            }
        }
    }

    private fun editCategory(category: Category?) {
        editedCategoryId = category?.id
        _viewState.update {
            it.copy(
                isInEditMode = true,
                editedCategoryName = category?.name.orEmpty(),
                editedCategoryIcon = category?.icon
            )
        }
    }

    private fun addCategory() {
        val categoryToAdd = with(_viewState.value) {
            Category(
                id = editedCategoryId ?: generateId(),
                name = editedCategoryName,
                icon = editedCategoryIcon
            )
        }

        viewModelScope.launch { addCategoryUseCase(categoryToAdd) }
        _viewState.update {
            it.copy(
                isInEditMode = false,
                editedCategoryName = "",
                editedCategoryIcon = null
            )
        }
    }

    private fun deleteCategory(category: Category) {
        viewModelScope.launch { deleteCategoryUseCase(category) }
    }

    private fun generateId(): String {
        return UUID.randomUUID().toString()
    }
}

data class CategoriesViewState(
    val categories: List<Category> = emptyList(),
    val isInEditMode: Boolean = false,
    val editedCategoryName: String = "",
    val editedCategoryIcon: CategoryIcon? = null
)

sealed class CategoriesScreenUiIntent {
    object OnAddClicked : CategoriesScreenUiIntent()
    class OnEditClicked(val category: Category) : CategoriesScreenUiIntent()
    class OnNameEdited(val name: String) : CategoriesScreenUiIntent()
    class OnIconEdited(val icon: CategoryIcon) : CategoriesScreenUiIntent()
    class OnCategoryDeleted(val category: Category) : CategoriesScreenUiIntent()
    object OnCategorySaved : CategoriesScreenUiIntent()
}