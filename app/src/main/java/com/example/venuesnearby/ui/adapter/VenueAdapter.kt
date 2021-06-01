package com.example.venuesnearby.ui.adapter

import com.example.venuesnearby.BR
import com.example.venuesnearby.R
import com.example.venuesnearby.data.model.Venue
import com.example.venuesnearby.databinding.ItemVenueBinding
import com.example.venuesnearby.ui.adapter.base.BaseRecyclerViewAdapter

class VenueAdapter(onItemClickListener: OnItemClickListener<Venue>) :
    BaseRecyclerViewAdapter<Venue, ItemVenueBinding>(onItemClickListener) {

    override fun getItemLayoutId(): Int {
        return R.layout.item_venue
    }

    override fun getViewBindingVariableId(): Int {
        return BR.venue
    }

    override fun onViewHolderBinding(
        viewDataBinding: ItemVenueBinding,
        item: Venue?,
        position: Int
    ) {
    }
}