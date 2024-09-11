package com.kitahara.scamdtek.common

import android.content.Context
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT

fun Context.toast(text: String) = Toast.makeText(this, text, LENGTH_SHORT).show()