package com.aqrlei.litepermission

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

/**
 * created by AqrLei on 2020/2/29
 */
object PermissionHelper {

    fun checkPermission(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun checkPermission(context: Context, vararg permissions: String): Boolean {
        for (permission in permissions) {
            if (!checkPermission(context, permission))
                return false
        }
        return true
    }

    fun checkPermission(context: Context, permissions: List<String>): Boolean {
        return checkPermission(context, *permissions.toTypedArray())
    }


    fun requestPermission(fragment: Fragment, vararg permissions: String, requestCode: Int) {
        fragment.requestPermissions(permissions, requestCode)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun requestPermission(activity: AppCompatActivity, vararg permissions: String, requestCode: Int){
        activity.requestPermissions(permissions,requestCode)
    }

}