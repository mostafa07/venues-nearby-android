package com.example.venuesnearby.ui.main

import android.app.Application
import android.content.SharedPreferences
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import com.example.venuesnearby.R
import com.example.venuesnearby.data.model.Venue
import com.example.venuesnearby.data.model.app.CustomMessage
import com.example.venuesnearby.data.repository.VenuesRepository
import com.example.venuesnearby.exception.BusinessException
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import timber.log.Timber

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _currentUserLocation: MutableLiveData<Location> = MutableLiveData()
    private val _lastUpdatedUserLocation: MutableLiveData<Location> = MutableLiveData()
    val lastUpdatedUserLocation: LiveData<Location>
        get() = _lastUpdatedUserLocation

    private val _venuesList: MutableLiveData<List<Venue>> = MutableLiveData()
    val venuesList: LiveData<List<Venue>>
        get() = _venuesList

    private val _selectedVenue: MutableLiveData<Venue> = MutableLiveData()
    val selectedVenue: LiveData<Venue>
        get() = _selectedVenue

    private val _successMessage: MutableLiveData<CustomMessage> = MutableLiveData()
    val successMessage: LiveData<CustomMessage>
        get() = _successMessage

    private val _errorMessage: MutableLiveData<CustomMessage> = MutableLiveData()
    val errorMessage: LiveData<CustomMessage>
        get() = _errorMessage

    private val _isContentLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isContentLoading: LiveData<Boolean>
        get() = _isContentLoading

    private val mSharedPreferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(application.applicationContext)
    }

    init {
        _isContentLoading.value = true
    }

    fun updateUserLocation(location: Location) {
        Timber.d("updateUserLocation")

        _currentUserLocation.value = location

        if (_lastUpdatedUserLocation.value == null) {
            _lastUpdatedUserLocation.value = location
            retrieveVenuesList(location.latitude, location.longitude, location.altitude.toInt())
            return
        }

        val distanceDifference = location.distanceTo(_lastUpdatedUserLocation.value)
        Timber.d("Distance Difference: $distanceDifference")

        if (distanceDifference >= DISTANCE_DIFFERENCE_TO_UPDATE) {
            _lastUpdatedUserLocation.value = location
            retrieveVenuesList(location.latitude, location.longitude, location.altitude.toInt())
        }
    }

    private fun retrieveVenuesList(latitude: Double, longitude: Double, altitude: Int) {
        Timber.d("retrieveVenuesList")

        showLoading()
        val resultsLimit = mSharedPreferences.getInt(RESULTS_LIMIT_SHARED_PREF_KEY, 5)
        VenuesRepository.getVenuesWithPhotos(latitude, longitude, altitude, resultsLimit)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ venuesList ->
                _venuesList.value = venuesList.sortedBy { it.location.distance }
                hideLoading()
            }) { throwable ->
                setErrorMessage(throwable)
                hideLoading()
            }
    }

    fun clearAndHideSelectedVenue() {
        _selectedVenue.value = null
    }

    fun setSelectedVenue(venue: Venue) {
        _selectedVenue.value = venue
    }

    private fun setSuccessMessage(message: CustomMessage) {
        _successMessage.value = message
    }

    private fun setErrorMessage(errorMessage: CustomMessage) {
        _errorMessage.value = errorMessage
    }

    private fun setErrorMessage(t: Throwable) {
        if (t is BusinessException) {
            setErrorMessage(t.businessMessage)
        } else {
            t.printStackTrace()
            setErrorMessage(CustomMessage(R.string.error_operation_failed))
        }
    }

    private fun showLoading() {
        _isContentLoading.value = true
    }

    private fun hideLoading() {
        _isContentLoading.value = false
    }


    companion object {
        private const val DISTANCE_DIFFERENCE_TO_UPDATE = 500

        private const val RESULTS_LIMIT_SHARED_PREF_KEY = "results_limit"
    }
}