package com.example.venuesnearby.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.*
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.preference.PreferenceManager
import com.example.venuesnearby.BR
import com.example.venuesnearby.BuildConfig
import com.example.venuesnearby.R
import com.example.venuesnearby.data.model.app.CustomMessage
import com.example.venuesnearby.databinding.ActivityMainBinding
import com.example.venuesnearby.service.location.ForegroundOnlyLocationService
import com.example.venuesnearby.ui.adapter.VenueAdapter
import com.example.venuesnearby.ui.settings.SettingsActivity
import com.example.venuesnearby.util.toText
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mMainViewModel: MainViewModel
    private lateinit var mVenueAdapter: VenueAdapter
    private var mGoogleMap: GoogleMap? = null

    private var mIsForegroundOnlyLocationServiceBound = false
    private var mForegroundOnlyLocationService: ForegroundOnlyLocationService? = null
    private var mForegroundOnlyBroadcastReceiver: ForegroundOnlyBroadcastReceiver? = null

    private val mSharedPreferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(applicationContext)
    }

    // Monitors connection to the service
    private val mForegroundOnlyServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            Timber.d("onServiceConnected")

            val binder = service as ForegroundOnlyLocationService.LocalBinder
            mForegroundOnlyLocationService = binder.service
            mIsForegroundOnlyLocationServiceBound = true

            initLocationData()
        }

        override fun onServiceDisconnected(name: ComponentName) {
            Timber.d("onServiceDisconnected")

            mForegroundOnlyLocationService = null
            mIsForegroundOnlyLocationServiceBound = false
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("onCreate")
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setupRecyclerView()
        setupViewModel()
        setupViewModelObservations()

        initMap()

        mForegroundOnlyBroadcastReceiver = ForegroundOnlyBroadcastReceiver()
    }

    override fun onStart() {
        Timber.d("onStart")
        super.onStart()

        val serviceIntent = Intent(this@MainActivity, ForegroundOnlyLocationService::class.java)
        bindService(serviceIntent, mForegroundOnlyServiceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onResume() {
        Timber.d("onResume")
        super.onResume()

        LocalBroadcastManager.getInstance(this).registerReceiver(
            mForegroundOnlyBroadcastReceiver!!,
            IntentFilter(ForegroundOnlyLocationService.ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST)
        )
    }

    override fun onPause() {
        Timber.d("onPause")

        LocalBroadcastManager.getInstance(this)
            .unregisterReceiver(mForegroundOnlyBroadcastReceiver!!)

        super.onPause()
    }

    override fun onStop() {
        Timber.d("onStop")

        if (mIsForegroundOnlyLocationServiceBound) {
            unbindService(mForegroundOnlyServiceConnection)
            mIsForegroundOnlyLocationServiceBound = false
        }

        super.onStop()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.item_settings) {
            val intent = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(intent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    private fun setupRecyclerView() {
        mVenueAdapter = VenueAdapter { venue, _ ->
            mMainViewModel.setSelectedVenue(venue)
            moveMapToLocation(LatLng(venue.location.lat, venue.location.lng))
        }
        mBinding.venuesRecyclerView.adapter = mVenueAdapter
    }

    private fun setupViewModel() {
        mMainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        mBinding.setVariable(BR.viewModel, mMainViewModel)
        mBinding.lifecycleOwner = this@MainActivity
        mBinding.executePendingBindings()
    }


    private fun setupViewModelObservations() {
        mMainViewModel.successMessage.observe(this, { showSnackbar(it, true) })
        mMainViewModel.errorMessage.observe(this, { showSnackbar(it, false) })
        mMainViewModel.isContentLoading.observe(this) { isLoading ->
            mBinding.shimmerLayout.shimmerFrameLayout.showShimmer(isLoading)

            if (isLoading) {
                disableUserInteraction()
            } else {
                reEnableUserInteraction()
            }
        }

        mMainViewModel.lastUpdatedUserLocation.observe(this, {
            moveMapToLocation(LatLng(it.latitude, it.longitude))
            mMainViewModel.clearAndHideSelectedVenue()
        })

        mMainViewModel.venuesList.observe(this, {
            mVenueAdapter.dataList = it
            mBinding.venuesRecyclerView.smoothScrollToPosition(0)
        })
    }

    private fun initMap() {
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync { mGoogleMap = it }
    }

    private fun initLocationData() {
        Timber.d("initLocationData")

        if (isForegroundPermissionApproved()) {
            subscribeToLocationUpdatesBasedOnMode()
        } else {
            requestForegroundPermissions()
        }
    }

    private fun moveMapToLocation(latLng: LatLng, title: String = "") {
        mGoogleMap?.clear()
        mGoogleMap?.addMarker(
            MarkerOptions()
                .position(latLng)
                .title(title)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        )
        mGoogleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, GOOGLE_MAP_ZOOM_PREF))
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
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE -> when {
                grantResults.isEmpty() -> {
                    // If user interaction was interrupted, the permission request is cancelled and you receive empty arrays.
                    Timber.d("User interaction was cancelled.")
                }
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                    // Permission was granted.
                    subscribeToLocationUpdatesBasedOnMode()
                }
                else -> {
                    // Permission denied.
                    Snackbar.make(
                        mBinding.root, R.string.permission_denied_explanation,
                        Snackbar.LENGTH_LONG
                    )
                        .setAction(R.string.settings) { openAppSettingsScreen() }
                        .show()
                }
            }
        }
    }

    private fun subscribeToLocationUpdatesBasedOnMode() {
        val isRealTimeMode = mSharedPreferences.getBoolean(REALTIME_SHARED_PREF_KEY, false)
        mForegroundOnlyLocationService?.subscribeToLocationUpdates(isRealTimeMode)
            ?: Timber.d("Service Not Bound")
    }

    private fun openAppSettingsScreen() {
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

    private fun showSnackbar(message: CustomMessage, successFlag: Boolean) {
        val messageString = getString(message.messageResourceId, message.params)

        Snackbar.make(mBinding.root, messageString, Snackbar.LENGTH_LONG)
            .setBackgroundTint(
                resources.getColor(
                    if (successFlag) android.R.color.holo_green_dark else android.R.color.holo_red_dark
                )
            )
            .setTextColor(resources.getColor(android.R.color.white))
            .show()
    }

    private inner class ForegroundOnlyBroadcastReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            Timber.d(" ForegroundOnlyBroadcastReceiver : onReceive")

            val location = intent.getParcelableExtra<Location>(
                ForegroundOnlyLocationService.EXTRA_LOCATION
            )

            Timber.wtf("Foreground location Received: ${location.toText()}")
            location?.let { mMainViewModel.updateUserLocation(it) }
        }
    }


    companion object {
        private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34
        private const val GOOGLE_MAP_ZOOM_PREF = 15F

        private const val REALTIME_SHARED_PREF_KEY = "realtime"
    }
}