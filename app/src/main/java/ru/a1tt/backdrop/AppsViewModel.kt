package ru.a1tt.backdrop

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AppsViewModel: ViewModel() {
    var data: MutableLiveData<List<TargetApplication>> = MutableLiveData()
}