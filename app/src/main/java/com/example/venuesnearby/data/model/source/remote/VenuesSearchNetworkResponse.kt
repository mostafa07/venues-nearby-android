package com.example.venuesnearby.data.model.source.remote

data class VenuesSearchApiResponse(val meta: Meta, val response: VenuesResponse)

data class Meta(val code: Number, val requestId: String)

data class VenuesResponse(val venues: List<VenueNetworkModel>)

data class Icon(val prefix: String, val suffix: String)

data class LabeledLatLngs(val label: String, val lat: Number, val lng: Number)

data class VenuePage(val id: String)