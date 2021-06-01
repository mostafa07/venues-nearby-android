package com.example.venuesnearby.exception

import com.example.venuesnearby.data.model.app.CustomMessage

class BusinessException(val businessMessage: CustomMessage) : Exception()