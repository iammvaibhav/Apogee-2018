package bitspilani.dvm.apogee2016.data.firebase.model

/**
 * Created by Vaibhav on 09-02-2018.
 */

enum class ShowBy {
    DATE, CATEGORY
}

class FilterEvents {
    var showBy = ShowBy.DATE
    var filterByOngoing = false
    var filterByFavourites = false
    var excludeVenue = mutableListOf<String>()
    var excludeCategory = mutableListOf<String>()

    fun clearAll() {
        showBy = ShowBy.DATE
        filterByOngoing = false
        filterByFavourites = false
        excludeVenue.clear()
        excludeCategory.clear()
    }

    fun copy(): FilterEvents {
        val new = FilterEvents()
        new.showBy = this.showBy
        new.filterByFavourites = this.filterByFavourites
        new.filterByOngoing = this.filterByOngoing
        this.excludeVenue.forEach { new.excludeVenue.add(it) }
        this.excludeCategory.forEach { new.excludeCategory.add(it) }
        return new
    }

    override fun toString(): String {
        return "showBy: $showBy ongoing : $filterByOngoing, fav : $filterByFavourites, venue: $excludeVenue, category: $excludeCategory"
    }
}