package com.aqrlei.litepermission

/**
 * created by AqrLei on 2020/2/29
 */
interface IPermissionCallback {

    fun onGranted(permissionsNeededModule: PermissionsNeededModule?)

    fun onDenied(permissionsNeededModule: PermissionsNeededModule)

    fun onDeniedCannotShowPermissionRationale(permissionsNeededModule: PermissionsNeededModule)

}