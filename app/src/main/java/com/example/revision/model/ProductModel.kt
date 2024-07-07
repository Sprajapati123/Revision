package com.example.revision.model

import android.os.Parcel
import android.os.Parcelable

data class ProductModel(
    var id : String = "",
    var productName : String = "",
    var price : Int = 0,
    var description : String = "",
    var categoryName : String = "",
    var imageName : String = "",
    var imageUrl : String = "",
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt() ?: 0,
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(productName)
        parcel.writeInt(price)
        parcel.writeString(description)
        parcel.writeString(categoryName)
        parcel.writeString(imageName)
        parcel.writeString(imageUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductModel> {
        override fun createFromParcel(parcel: Parcel): ProductModel {
            return ProductModel(parcel)
        }

        override fun newArray(size: Int): Array<ProductModel?> {
            return arrayOfNulls(size)
        }
    }
}