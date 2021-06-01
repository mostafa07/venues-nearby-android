package com.example.venuesnearby.data.repository

import com.example.venuesnearby.BuildConfig
import com.example.venuesnearby.data.model.Photo
import com.example.venuesnearby.data.model.Venue
import com.example.venuesnearby.webservice.VenuesWebService
import com.example.venuesnearby.webservice.builder.RetrofitServiceBuilder
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

object VenuesRepository {

    private const val TAG = "VenuesRepository"

    private const val RADIUS = 1000
    private const val CATEGORY_ID = "4d4b7105d754a06374d81259"
    private const val LIMIT = 10
    private const val DATE_FORMAT = "yyyyMMdd"

    private val mVenuesWebService: VenuesWebService =
        RetrofitServiceBuilder.buildService(VenuesWebService::class.java)


    fun getVenuesWithPhotos(
        latitude: Double,
        longitude: Double,
        altitude: Int
    ): Observable<List<Venue>> {
        return searchVenues(latitude, longitude, altitude)
                // FIXME:   The photos API is a premium Foursquare feature;
                //      Just uncomment this block to test it out
//            .flatMapIterable {
//                it
//            }
//            .flatMap {
//                    venue ->
//                getVenuePhotos(venue.id)
//                    .map { retrievedPhotos ->
//                        venue.photos = retrievedPhotos
//                        return@map venue
//                    }
//            }
//            .toList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }


    private fun searchVenues(
        latitude: Double,
        longitude: Double,
        altitude: Int
    ): Observable<List<Venue>> {
        val versionDate = SimpleDateFormat(DATE_FORMAT).format(Date())

        return mVenuesWebService.searchVenues(
            "$latitude,$longitude",
            altitude,
            RADIUS,
            CATEGORY_ID,
            LIMIT,
            versionDate,
            BuildConfig.FOURSQUARE_CLIENT_ID,
            BuildConfig.FOURSQUARE_CLIENT_SECRET
        ).subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .map { it.response.venues }
    }

    private fun getVenuePhotos(venueId: String): Observable<List<Photo>> {
        return mVenuesWebService.getVenuePhotos(
            venueId = venueId,
            version = SimpleDateFormat(DATE_FORMAT).format(Date()),
            group = null,
            limit = LIMIT,
            offset = null,
            clientId = BuildConfig.FOURSQUARE_CLIENT_ID,
            clientSecret = BuildConfig.FOURSQUARE_CLIENT_SECRET
        ).subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .map { it.response.photos.items }
    }
}