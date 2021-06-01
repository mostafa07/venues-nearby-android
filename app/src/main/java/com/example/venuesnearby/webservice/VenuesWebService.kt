package com.example.venuesnearby.webservice

import com.example.venuesnearby.data.model.source.remote.VenuesPhotosApiResponse
import com.example.venuesnearby.data.model.source.remote.VenuesSearchApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import rx.Observable

interface VenuesWebService {

    @GET(VENUES_SEARCH_END_POINT)
    fun searchVenues(
        @Query(LATITUDE_LONGITUDE_QUERY_PARAM) latitudeLongitude: String,
        @Query(ALTITUDE_QUERY_PARAM) altitude: Int,
        @Query(RADIUS_QUERY_PARAM) radius: Int,
        @Query(CATEGORY_ID_QUERY_PARAM) categoryId: String,
        @Query(LIMIT_QUERY_PARAM) limit: Int,
        @Query(VERSION_QUERY_PARAM) version: String,
        @Query(CLIENT_ID_QUERY_PARAM) clientId: String,
        @Query(CLIENT_SECRET__QUERY_PARAM) clientSecret: String
    ): Observable<VenuesSearchApiResponse>

    @GET(VENUE_PHOTOS_END_POINT)
    fun getVenuePhotos(
        @Path(VENUE_ID_PATH_PARAM) venueId: String,
        @Query(VERSION_QUERY_PARAM) version: String,
        @Query(GROUP_QUERY_PARAM) group: String?,
        @Query(LIMIT_QUERY_PARAM) limit: Int?,
        @Query(OFFSET_QUERY_PARAM) offset: Int?,
        @Query(CLIENT_ID_QUERY_PARAM) clientId: String,
        @Query(CLIENT_SECRET__QUERY_PARAM) clientSecret: String
    ): Observable<VenuesPhotosApiResponse>

    companion object {
        // Venues Search
        const val VENUES_SEARCH_END_POINT = "venues/search"

        const val LATITUDE_LONGITUDE_QUERY_PARAM = "ll"
        const val ALTITUDE_QUERY_PARAM = "alt"
        const val CATEGORY_ID_QUERY_PARAM = "categoryId"
        const val RADIUS_QUERY_PARAM = "radius"
        const val LIMIT_QUERY_PARAM = "limit"

        const val VERSION_QUERY_PARAM = "v"
        const val CLIENT_ID_QUERY_PARAM = "client_id"
        const val CLIENT_SECRET__QUERY_PARAM = "client_secret"

        // Venues Photos
        const val VENUE_ID_PATH_PARAM = "VENUE_ID"
        const val VENUE_PHOTOS_END_POINT = "venues/{$VENUE_ID_PATH_PARAM}/photos"

        const val GROUP_QUERY_PARAM = "group"
        const val OFFSET_QUERY_PARAM = "offset"
    }
}
