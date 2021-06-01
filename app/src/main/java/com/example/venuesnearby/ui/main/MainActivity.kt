package com.example.venuesnearby.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.venuesnearby.BR
import com.example.venuesnearby.BuildConfig
import com.example.venuesnearby.R
import com.example.venuesnearby.data.model.app.CustomMessage
import com.example.venuesnearby.databinding.ActivityMainBinding
import com.example.venuesnearby.ui.adapter.VenueAdapter
import com.example.venuesnearby.ui.dialog.ErrorAlert
import com.example.venuesnearby.ui.dialog.SuccessAlert
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mMainViewModel: MainViewModel
    private lateinit var mGoogleMap: GoogleMap
    private lateinit var mVenueAdapter: VenueAdapter

    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setupRecyclerView()
        setupViewModel()
        setupViewModelObservations()

        initMap()

        initLocationData()
    }

    override fun onBackPressed() {
        mMainViewModel.clearAndHideSelectedVenueLocationLiveData()
        val userLocation = mMainViewModel.getUserLocationLiveData().value
        moveMapToLocation(LatLng(userLocation!!.latitude, userLocation.longitude))
    }

    private fun setupRecyclerView() {
        mVenueAdapter = VenueAdapter { dataItem, _ ->
            mMainViewModel.setSelectedVenueLocationLiveData(dataItem)
            moveMapToLocation(LatLng(dataItem.location.lat, dataItem.location.lng))
        }
        mBinding.venuesRecyclerView.adapter = mVenueAdapter
    }

    private fun setupViewModel() {
        mMainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        mBinding.setVariable(BR.viewModel, mMainViewModel)
        mBinding.lifecycleOwner = this
        mBinding.executePendingBindings()
    }

    private fun setupViewModelObservations() {
        mMainViewModel.getSuccessMessageLiveData().observe(this) { message: CustomMessage ->
            this.showSuccessDialog(message)
        }

        mMainViewModel.getErrorMessageLiveData().observe(this) { message: CustomMessage ->
            this.showErrorDialog(message)
        }

        mMainViewModel.getIsContentLoadingMutableLiveData().observe(this) { isLoading ->
            if (isLoading) {
                mBinding.shimmerLayout.shimmerFrameLayout.startShimmer()
                disableUserInteraction()
            } else {
                mBinding.shimmerLayout.shimmerFrameLayout.stopShimmer()
                reEnableUserInteraction()
            }
        }

        mMainViewModel.getUserLocationLiveData().observe(this, {
            moveMapToLocation(LatLng(it.latitude, it.longitude))
        })

        mMainViewModel.getVenuesListLiveData().observe(this, {
            mVenueAdapter.dataList = it
            mBinding.venuesRecyclerView.smoothScrollToPosition(0)
        })
    }

    private fun initMap() {
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync {
            mGoogleMap = it

            mGoogleMap.setOnCameraMoveStartedListener { reason ->
                if (REASON_GESTURE == reason) {
                    mMainViewModel.setCurrentCameraLocationLiveData(mGoogleMap.cameraPosition.target)
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun initLocationData() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(5)
            fastestInterval = TimeUnit.SECONDS.toMillis(0)
            maxWaitTime = TimeUnit.SECONDS.toMillis(10)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            numUpdates = 1
        }
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                mMainViewModel.setUserLocationLiveData(locationResult.lastLocation)
            }
        }

        if (isForegroundPermissionApproved()) {
            mFusedLocationProviderClient.lastLocation.addOnCompleteListener {
                val location = it.result
                if (location == null) {
                    mFusedLocationProviderClient =
                        LocationServices.getFusedLocationProviderClient(this)
                    mFusedLocationProviderClient.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        Looper.getMainLooper()
                    )
                } else {
                    mMainViewModel.setUserLocationLiveData(it.result)
                }
            }
        } else {
            requestForegroundPermissions()
        }
    }

    private fun moveMapToLocation(latLng: LatLng, title: String = "") {
        mGoogleMap.clear()
        mGoogleMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .title(title)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        )
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, GOOGLE_MAP_ZOOM_PREF))
    }

    private fun isForegroundPermissionApproved(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    private fun requestForegroundPermissions() {
        val provideRationale = isForegroundPermissionApproved()

        // If the user denied a previous request, but didn't check "Don't ask again", provide additional rationale.
        if (provideRationale) {
            Snackbar.make(mBinding.root, R.string.permission_rationale, Snackbar.LENGTH_LONG)
                .setAction(R.string.ok) {
                    // Request permission
                    ActivityCompat.requestPermissions(
                        this@MainActivity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
                    )
                }
                .show()
        } else {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
            )
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE -> when {
                grantResults.isEmpty() -> {
                    // If user interaction was interrupted, the permission request is cancelled and you receive empty arrays.
                    Log.d(TAG, "User interaction was cancelled.")
                }
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                    // Permission was granted.
                    mFusedLocationProviderClient.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        Looper.getMainLooper()
                    )
                }
                else -> {
                    // Permission denied.
                    Snackbar.make(
                        mBinding.root,
                        R.string.permission_denied_explanation,
                        Snackbar.LENGTH_LONG
                    )
                        .setAction(R.string.settings) {
                            openSettingScreen()
                        }
                        .show()
                }
            }
        }
    }

    private fun openSettingScreen() {
        // Build intent that displays the App settings screen.
        val intent = Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    }

    private fun disableUserInteraction() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    private fun reEnableUserInteraction() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun showSuccessDialog(message: CustomMessage) {
        val successAlert = SuccessAlert(this, message)
        successAlert.show()
    }

    private fun showErrorDialog(message: CustomMessage) {
        val errorAlert = ErrorAlert(this, message)
        errorAlert.show()
    }


    companion object {
        private const val TAG = "MainActivity"
        private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34
        private const val GOOGLE_MAP_ZOOM_PREF = 15F
    }
}