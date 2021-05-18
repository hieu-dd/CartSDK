package com.cafeinlove14h.cartcompose.extensions

import android.app.Activity
import android.content.Context
import vn.teko.android.core.util.instancesmanager.AppIdentifier

fun Activity.getAppName() = (this as AppIdentifier).appIdentifier
    ?: throw Throwable("Cannot get app name")

fun Context.getAppName() = (this as AppIdentifier).appIdentifier
    ?: throw Throwable("Cannot get app name")