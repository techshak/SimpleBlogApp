package com.olamachia.simpleblogappwithdatabinding.utils

import android.util.Log
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

object Connectivity {

    fun hasConnectivity():Boolean{
        return try{
            val socket = Socket()
            socket.connect(InetSocketAddress("8.8.8.8",53),1500)
            socket.close()
            Log.d("Connection","PING Success")
            true
        }catch (e:IOException){
            Log.d("Connection","PING Error.${e}")
            false
        }
        }

}