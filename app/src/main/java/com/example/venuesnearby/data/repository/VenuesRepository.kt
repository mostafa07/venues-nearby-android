package com.example.venuesnearby.data.repository

import com.example.venuesnearby.BuildConfig
import com.example.venuesnearby.data.model.domain.Venue
import com.example.venuesnearby.data.model.source.remote.PhotoNetworkModel
import com.example.venuesnearby.data.model.source.remote.VenueNetworkModel
import com.example.venuesnearby.webservice.VenuesWebService
import com.example.venuesnearby.webservice.builder.RetrofitServiceBuilder
import rx.Observable
import java.text.SimpleDateFormat
import java.util.*

object VenuesRepository {

    private const val RADIUS = 500
    private const val CATEGORY_ID = "4d4b7105d754a06374d81259"
    private const val DATE_FORMAT = "yyyyMMdd"

    private val mVenuesWebService: VenuesWebService =
        RetrofitServiceBuilder.buildService(VenuesWebService::class.java)


    fun getVenues(
        latitude: Double,
        longitude: Double,
        altitude: Int,
        limit: Int
    ): Observable<List<Venue>> {
        return searchVenues(latitude, longitude, altitude, limit)
            .flatMapIterable { it }
            .map { it.toVenue() }
            .toList()
    }

    fun getVenuesWithPhotos(
        latitude: Double,
        longitude: Double,
        altitude: Int,
        limit: Int
    ): Observable<List<Venue>> {
        return searchVenues(latitude, longitude, altitude, limit)
            .flatMapIterable { it }
            .flatMap { venue ->
                getVenuePhotos(venue.id)
                    .map { retrievedPhotos ->
                        venue.photos = retrievedPhotos
                        return@map venue
                    }
            }
            .map { it.toVenue() }
            .toList()
    }


    private fun searchVenues(
        latitude: Double,
        longitude: Double,
        altitude: Int,
        limit: Int
    ): Observable<List<VenueNetworkModel>> {
        val versionDate = SimpleDateFormat(DATE_FORMAT).format(Date())

        return mVenuesWebService.searchVenues(
            "$latitude,$longitude",
            altitude,
            RADIUS,
            CATEGORY_ID,
            limit,
            versionDate,
            BuildConfig.FOURSQUARE_CLIENT_ID,
            BuildConfig.FOURSQUARE_CLIENT_SECRET
        ).map { it.response.venues }
    }

    private fun getVenuePhotos(venueId: String): Observable<List<PhotoNetworkModel>> {
        return mVenuesWebService.getVenuePhotos(
            venueId = venueId,
            version = SimpleDateFormat(DATE_FORMAT).format(Date()),
            group = null,
            limit = 1,
            offset = null,
            clientId = BuildConfig.FOURSQUARE_CLIENT_ID,
            clientSecret = BuildConfig.FOURSQUARE_CLIENT_SECRET
        ).map { it.response.photos.items }
    }
}