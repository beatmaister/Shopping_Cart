package com.example.project.network

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.project.data.model.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

interface IItemsFetcher {
    suspend fun fetchItems(): List<Item>
}

class ItemsFetcher: IItemsFetcher{
    // URL of github-hosted API
    private val URL = "https://my-json-server.typicode.com/beatmaister/class-api/groceries"

    // storing okHTTP client in variable to access API
    val client: OkHttpClient = OkHttpClient()

    // function to fetch all items from the API for display
    override suspend fun fetchItems(): List<Item> = withContext(Dispatchers.IO) {

        // attempt to fetch items list from the API
        try {
            // building a request and storing the response using the client
            val request: Request = Request.Builder().url(URL).get().build()
            val response: Response = client.newCall(request).execute()

            if(response.isSuccessful){
                // on successful responses, stringify the data
                // and use Gson library to convert to a list of Item types
                val body = response.body?.string()
                if(body!=null){
                    val listItemType = object:TypeToken<List<Item>>() {}.type
                    val items = Gson().fromJson<List<Item>>(body, listItemType)
                    return@withContext items
                }else{
                    // throw an exception if the response is null
                    throw Exception("Body is Null")
                }
            }else{
                // throw an exception if the response fails
                throw Exception("Response not successful")
            }
        } catch(e: Exception ){
            // Handle all exceptions thrown by the request by storing an empty list
            return@withContext emptyList()
        }
    }

}
