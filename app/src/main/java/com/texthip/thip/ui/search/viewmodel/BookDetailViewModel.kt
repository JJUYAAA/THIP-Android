package com.texthip.thip.ui.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.R
import com.texthip.thip.data.model.book.response.BookDetailResponse
import com.texthip.thip.data.provider.StringResourceProvider
import com.texthip.thip.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDetailViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    private val stringResourceProvider: StringResourceProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookDetailUiState())
    val uiState: StateFlow<BookDetailUiState> = _uiState.asStateFlow()

    fun loadBookDetail(isbn: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            bookRepository.getBookDetail(isbn)
                .onSuccess { bookDetail ->
                    _uiState.value = _uiState.value.copy(
                        bookDetail = bookDetail,
                        isLoading = false,
                        error = null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: stringResourceProvider.getString(R.string.error_book_detail_load_failed)
                    )
                }
        }
    }

    fun saveBook(isbn: String, type: Boolean) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true, error = null)
            
            bookRepository.saveBook(isbn, type)
                .onSuccess { saveResponse ->
                    saveResponse?.let {
                        // 책 상세 정보의 isSaved 상태 업데이트
                        val updatedBookDetail = _uiState.value.bookDetail?.copy(isSaved = it.isSaved)
                        _uiState.value = _uiState.value.copy(
                            bookDetail = updatedBookDetail,
                            isSaving = false,
                            error = null
                        )
                    }
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        error = exception.message ?: stringResourceProvider.getString(R.string.error_book_save_failed)
                    )
                }
        }
    }
}

data class BookDetailUiState(
    val bookDetail: BookDetailResponse? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null
)