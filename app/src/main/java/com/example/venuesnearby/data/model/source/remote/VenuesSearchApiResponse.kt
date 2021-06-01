package com.example.venuesnearby.data.model.source.remote

import com.example.venuesnearby.data.model.Venue

data class VenuesSearchApiResponse(val meta: Meta, val response: VenuesResponse)

data class VenuesResponse(val venues: List<Venue>)

data class Icon(val prefix: String, val suffix: String)

data class LabeledLatLngs(val label: String, val lat: Number, val lng: Number)

data class VenuePage(val id: String)