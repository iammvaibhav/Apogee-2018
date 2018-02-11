package bitspilani.dvm.apogee2016.data.firebase.model

data class Event(var id: Int = 0,
                 var name: String = "",
                 var category: String = "",
                 var day: Int = 0,
                 var startTime: String = "",
                 var endTime: String = "",
                 var venue: String = "",
                 var description: String = "",
                 var rules: String = "")