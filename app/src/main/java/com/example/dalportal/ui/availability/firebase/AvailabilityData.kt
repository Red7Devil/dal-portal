package com.example.dalportal.ui.availability.firebase

import android.widget.Button
import com.example.dalportal.R
import com.google.firebase.database.Exclude
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.QueryDocumentSnapshot

data class ButtonPair(
    val first: String = "",
    val second: String = ""
)

data class AvailabilityData(
    @PropertyName("documentId")
    val documentId: String = "",

    @PropertyName("timeRangeButtonsMap")
    val timeRangeButtonsMap: Map<String, List<ButtonPair>> = emptyMap(),

    @PropertyName("selectedStartDate")
    val selectedStartDate: String = "",

    @PropertyName("selectedEndDate")
    val selectedEndDate: String = "",

    @PropertyName("comment")
    val comment: String = ""
){
    // Exclude this property from serialization and deserialization
    @get:Exclude
    val toMap: Map<String, Any?> = mapOf(
        "documentId" to documentId,
        "timeRangeButtonsMap" to timeRangeButtonsMap,
        "selectedStartDate" to selectedStartDate,
        "selectedEndDate" to selectedEndDate,
        "comment" to comment
    )

    companion object {
        fun fromSnapshot(snapshot: QueryDocumentSnapshot): AvailabilityData {
            return snapshot.toObject(AvailabilityData::class.java)
        }
    }
}

// Function to convert a list of button pairs to a list of pairs of their texts
fun convertButtonPairsToStrings(buttonPairs: MutableMap<String, MutableList<Pair<Button, Button>>>): Map<String, List<ButtonPair>> {
    return buttonPairs.mapValues { (_, pairs) ->
        pairs.map { ButtonPair(it.first.text.toString(), it.second.text.toString()) }.toMutableList()
    }.toMap()
}

