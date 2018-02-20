package bitspilani.dvm.apogee2016.data.firebase.model

import java.io.Serializable

data class Event(var id: Int = 0,
                 var name: String = "",
                 var category: String = "",
                 var day: Int = 0,
                 var startTime: String = "",
                 var endTime: Double = 0.0,
                 var venue: String = "",
                 var description: String = "",
                 var rules: String = ""): Serializable