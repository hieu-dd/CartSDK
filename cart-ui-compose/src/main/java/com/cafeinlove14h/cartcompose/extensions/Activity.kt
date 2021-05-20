package com.cafeinlove14h.cartcompose.extensions

import android.app.Activity
import android.content.Context
import androidx.compose.ui.graphics.Color
import vn.teko.android.core.util.instancesmanager.AppIdentifier
import vn.teko.apollo.ApolloTheme
import vn.teko.apollo.extension.getApolloTheme

fun Activity.getAppName() = (this as AppIdentifier).appIdentifier
    ?: throw Throwable("Cannot get app name")

fun Context.getAppName() = (this as AppIdentifier).appIdentifier
    ?: throw Throwable("Cannot get app name")

fun Int.toColor() = Color(this)

fun Context.getPrimaryColor() = this.getApolloTheme().getPrimaryColor()
fun Context.getNeutralColor() = this.getApolloTheme().getNeutralColor()
fun Context.getErrorColor() = this.getApolloTheme().getErrorColor()
fun Context.getLinkColor() = this.getApolloTheme().getLinkColor()
fun Context.getPendingColor() = this.getApolloTheme().getPendingColor()
fun Context.getSuccessColor() = this.getApolloTheme().getSuccessColor()
fun Context.getTextPrimaryColor() = this.getNeutralColor().primaryTextColor.toColor()
fun Context.getSecondaryTextColor() = this.getNeutralColor().secondaryTextColor.toColor()
