package com.example.venuesnearby.ui.main

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.venuesnearby.R
import com.example.venuesnearby.data.model.Venue
import com.example.venuesnearby.data.model.app.CustomMessage
import com.example.venuesnearby.data.repository.VenuesRepository
import com.example.venuesnearby.exception.BusinessException
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class MainViewModel : ViewModel() {

    private val mCurrentUserLocationMutableLiveData: MutableLiveData<Location> = MutableLiveData()
    private val mLastUpdatedUserLocationMutableLiveData: MutableLiveData<Location> = MutableLiveData()

    private val mVenuesListMutableLiveData: MutableLiveData<List<Venue>> = MutableLiveData()
    private val mSelectedVenueMutableLiveData: MutableLiveData<Venue?> = MutableLiveData(null)

    private val mSuccessMessageMutableLiveData: MutableLiveData<CustomMessage> = MutableLiveData()
    private val mErrorMessageMutableLiveData: MutableLiveData<CustomMessage> = MutableLiveData()
    private val mIsContentLoadingMutableLiveData: MutableLiveData<Boolean> = MutableLiveData(true)

    fun updateUserLocation(location: Location) {
        mCurrentUserLocationMutableLiveData.value = location

        if (mLastUpdatedUserLocationMutableLiveData.value == null) {
            mLastUpdatedUserLocationMutableLiveData.value = location
            retrieveVenuesList(location.latitude, location.longitude, location.altitude.toInt())
            return
        }

        val distanceDifference = location.distanceTo(mLastUpdatedUserLocationMutableLiveData.value)
        Log.d(TAG, "Distance: $distanceDifference")

        if (distanceDifference >= DISTANCE_DIFFERENCE_TO_UPDATE) {
            mLastUpdatedUserLocationMutableLiveData.value = location

            retrieveVenuesList(location.latitude, location.longitude, location.altitude.toInt())
        }
    }

    private fun retrieveVenuesList(latitude: Double, longitude: Double, altitude: Int) {
        Log.d(TAG, "retrieveVenuesList")

        showLoading()
        VenuesRepository.getVenuesWithPhotos(latitude, longitude, altitude)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ venuesList ->
                mVenuesListMutableLiveData.value = venuesList.sortedBy { it.location.distance }
                hideLoading()
            }) { throwable ->
                setErrorMessage(throwable)
                throwable.printStackTrace()
                hideLoading()
            }
    }

    fun getCurrentUserLocationLiveData(): LiveData<Location> {
        return mCurrentUserLocationMutableLiveData
    }

    fun getLastUpdatedUserLocationLiveData(): LiveData<Location> {
        return mLastUpdatedUserLocationMutableLiveData
    }

    fun getVenuesListLiveData(): LiveData<List<Venue>> {
        return mVenuesListMutableLiveData
    }

    fun getSelectedVenueLiveData(): LiveData<Venue?> {
        return mSelectedVenueMutableLiveData
    }

    fun setSelectedVenueLocationLiveData(venue: Venue?) {
        mSelectedVenueMutableLiveData.value = venue
    }

    fun clearAndHideSelectedVenueLocationLiveData() {
        mSelectedVenueMutableLiveData.value = null
    }

    fun getSuccessMessageLiveData(): LiveData<CustomMessage> {
        return mSuccessMessageMutableLiveData
    }

    private fun setSuccessMessage(message: CustomMessage) {
        mSuccessMessageMutableLiveData.value = message
    }

    fun getErrorMessageLiveData(): LiveData<CustomMessage> {
        return mErrorMessageMutableLiveData
    }

    private fun setErrorMessage(errorMessage: CustomMessage) {
        mErrorMessageMutableLiveData.value = errorMessage
    }

    private fun setErrorMessage(t: Throwable) {
        if (t is BusinessException) {
            setErrorMessage(t.businessMessage)
        } else {
            t.printStackTrace()
            setErrorMessage(CustomMessage(R.string.error_operation_failed))
        }
    }

    fun getIsContentLoadingMutableLiveData(): LiveData<Boolean> {
        return mIsContentLoadingMutableLiveData
    }

    private fun showLoading() {
        mIsContentLoadingMutableLiveData.value = true
    }

    private fun hideLoading() {
        mIsContentLoadingMutableLiveData.value = false
    }


    companion object {
        private const val TAG = "MainViewModel"

        private const val DISTANCE_DIFFERENCE_TO_UPDATE = 500
    }
}