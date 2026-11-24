package com.travelplanner.ui.base;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.travelplanner.data.repository.Resource;

public abstract class BaseViewModel extends ViewModel {
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<String> successMessage = new MutableLiveData<>();

    protected void showLoading() {
        loading.setValue(true);
    }

    protected void hideLoading() {
        loading.setValue(false);
    }

    protected void showError(String message) {
        errorMessage.setValue(message);
    }

    protected void showSuccess(String message) {
        successMessage.setValue(message);
    }

    protected <T> void handleResource(Resource<T> resource, OnResourceHandled<T> callback) {
        if (resource.isLoading()) {
            showLoading();
        } else {
            hideLoading();
            if (resource.isSuccess()) {
                callback.onSuccess(resource.getData());
            } else if (resource.isError()) {
                showError(resource.getMessage());
                callback.onError(resource.getMessage());
            }
        }
    }

    public LiveData<Boolean> isLoading() {
        return loading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<String> getSuccessMessage() {
        return successMessage;
    }

    public interface OnResourceHandled<T> {
        void onSuccess(T data);

        default void onError(String message) {
        }
    }
}
