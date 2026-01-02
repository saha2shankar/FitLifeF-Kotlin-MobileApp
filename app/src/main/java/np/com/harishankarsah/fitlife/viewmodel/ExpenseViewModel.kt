package np.com.harishankarsah.fitlife.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import np.com.harishankarsah.fitlife.model.ExpenseModel
import np.com.harishankarsah.fitlife.repository.ExpenseRepository

data class ExpenseState(
    val expenses: List<ExpenseModel> = emptyList(),
    val totalExpense: Double = 0.0,
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)

class ExpenseViewModel : ViewModel() {
    private val repository = ExpenseRepository()
    
    private val _state = MutableStateFlow(ExpenseState())
    val state: StateFlow<ExpenseState> = _state.asStateFlow()

    init {
        // Collect real-time updates and calculate total
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            repository.getExpensesFlow()
                .map { expenses ->
                    val total = expenses.sumOf { it.amount }
                    expenses to total
                }
                .catch { e ->
                    _state.value = _state.value.copy(isLoading = false, error = e.message)
                }
                .collect { (expenses, total) ->
                    _state.value = _state.value.copy(
                        expenses = expenses,
                        totalExpense = total,
                        isLoading = false,
                        error = null
                    )
                }
        }
    }

    fun createExpense(expense: ExpenseModel) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null, successMessage = null)
            val result = repository.addExpense(expense)
            result.onSuccess {
                _state.value = _state.value.copy(
                    isLoading = false,
                    successMessage = "Expense added successfully"
                )
            }.onFailure { exception ->
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = exception.message ?: "Failed to add expense"
                )
            }
        }
    }

    fun updateExpense(expense: ExpenseModel) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null, successMessage = null)
            val result = repository.updateExpense(expense)
            result.onSuccess {
                _state.value = _state.value.copy(
                    isLoading = false,
                    successMessage = "Expense updated successfully"
                )
            }.onFailure { exception ->
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = exception.message ?: "Failed to update expense"
                )
            }
        }
    }

    fun deleteExpense(expenseId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null, successMessage = null)
            val result = repository.deleteExpense(expenseId)
            result.onSuccess {
                _state.value = _state.value.copy(
                    isLoading = false,
                    successMessage = "Expense deleted successfully"
                )
            }.onFailure { exception ->
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = exception.message ?: "Failed to delete expense"
                )
            }
        }
    }

    fun clearMessages() {
        _state.value = _state.value.copy(error = null, successMessage = null)
    }
}




