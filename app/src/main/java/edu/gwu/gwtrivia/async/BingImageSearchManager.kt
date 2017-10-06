package edu.gwu.gwtrivia.async

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.ImageView
import com.gwu.trivia.Constants
import com.gwu.trivia.Utilities
import com.koushikdutta.async.future.FutureCallback
import com.koushikdutta.ion.Ion

/**
 * Created by hoho on 2017-09-25.
 */

class BingImageSearchManager(val context: Context, val imageView: ImageView) {
    private val TAG = "BingImageSearchManager"
    var imageSearchCompletionListener: ImageSearchCompletionListener? = null

    interface ImageSearchCompletionListener {
        fun imageLoaded()
        fun imageNotLoaded()
    }

    fun search(query: String) {
        Ion.with(context).load(Constants.BING_SEARCH_URL)
                .addHeader("Ocp-Apim-Subscription-Key", Constants.BING_SEARCH_API_TOKEN)
                .addQuery("q", query)
                .addQuery("safeSearch", "Strict")
                .addQuery("mkt", "en-us")
                .asJsonObject()
                .setCallback(FutureCallback { error, result ->
                    error?.let {
                        Log.e(TAG, it.message)
                        //fail -network request to bing failed
                        imageSearchCompletionListener?.imageNotLoaded()
                    }
                    result?.let {
                        val orientation = context.resources.configuration.orientation
                        val url = Utilities.parseURLFromBingJSON(it, orientation)

                        if(url != null) {
                            Ion.with(imageView).load(url.toString()).setCallback {error, result ->
                                if(error !=null) {
                                    //fail - tried to load image from internet and failed
                                    imageSearchCompletionListener?.imageNotLoaded()

                                }else {
                                    //success
                                    Log.e("imageload","success")
                                    imageSearchCompletionListener?.imageLoaded()
                                }
                            }
                        }else {
                            //fail - no suitable url found
                            imageSearchCompletionListener?.imageNotLoaded()

                        }
                    }

                })
    }
}