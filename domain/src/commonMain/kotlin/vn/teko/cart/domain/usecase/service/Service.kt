package vn.teko.cart.domain.usecase.service

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import vn.teko.cart.domain.util.RFC3339DateTimeSerializer

@Serializable
data class Service(
    val id: Int,
    val name: String,
    val applyOn: String,
    val metaData: MetaData?,
    val groupId: Int,
    val feeOrigin: String,
    val displayInfo: DisplayInfo?,
    val delivery: Delivery?,
    val phongvuShowroomPickup: ShowRoomPickup?,
) {

    @Serializable
    data class MetaData(
        val providerServiceCode: String
    )

    @Serializable
    data class DisplayInfo(
        val desc: String,
        val notes: List<Note>
    ) {
        @Serializable
        data class Note(
            val name: String,
            val iconUrl: String
        )
    }

    @Serializable
    data class Delivery(
        val deliveryTimeSlots: List<DateRange>
    ) {

        @Serializable
        data class DateRange(
            @Serializable(with = RFC3339DateTimeSerializer::class)
            val since: Instant?,
            @Serializable(with = RFC3339DateTimeSerializer::class)
            val until: Instant?,
        )

    }

    @Serializable
    data class ShowRoomPickup(
        val showrooms: List<Showroom>,
    ) {

        @Serializable
        data class Showroom(
            val storeCode: String,
            val name: String,
            val location: Location?,
        ) {

            @Serializable
            data class Location(
                val region: String,
                val regionId: String,
                val cityId: String,
            )

        }

    }

}