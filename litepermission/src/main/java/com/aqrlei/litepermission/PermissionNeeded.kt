package com.aqrlei.litepermission

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * created by AqrLei on 2020/2/29
 */

@Parcelize
class PermissionNeeded(
    val permission: String,
    val desc: String = "",
    var isGranted: Boolean = false,
    var shouldShowRationale:Boolean = true) : Parcelable