package com.frankdaza.getsunset

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    protected fun getSunSet(view: View) {
        var city: String = etCityName.text.toString().trim()
        val url: String = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22" + city + "%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys"
        MyAsyncTask().execute(url)
    }


    inner class MyAsyncTask : AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg p0: String?): String {
            val url = URL(p0[0])
            val urlConnect = url.openConnection() as HttpURLConnection
            urlConnect.connectTimeout = 7000

            var inString = convertSteamToString(urlConnect.inputStream)
            // Cannot access to UI
            publishProgress(inString)

            return ""
        }

        override fun onProgressUpdate(vararg values: String?) {
            try {
                println("Tamaño values: " + values.size)
                var json = JSONObject(values[0])
                val query = json.getJSONObject("query")
                val results = query.getJSONObject("results")
                val channel = results.getJSONObject("channel")
                val astronomy = channel.getJSONObject("astronomy")
                val sunrise = astronomy.getString("sunrise")
                tvSunSet.text = "Sunrise time is: $sunrise"
            } catch (e: Exception) {
                println(e.message)
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
        }

        fun convertSteamToString(inputStream: InputStream) : String {
            val bufferReader = BufferedReader(InputStreamReader(inputStream))
            var line: String
            var allString = ""

            try {
                do {
                    line = bufferReader.readLine()

                    if (line != null)
                        allString += line
                } while (line != null)
            } catch (e: Exception) {
                println(e.message)
            }
            return allString
        }

    }

}
