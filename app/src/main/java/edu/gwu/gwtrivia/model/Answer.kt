package edu.gwu.gwtrivia.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by jared on 9/18/17.
 */

@Parcelize
data class Answer(val answer: String, val correct: Boolean): Parcelable