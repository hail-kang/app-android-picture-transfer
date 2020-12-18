package com.example.app_android_picture_transfer.data

import android.util.Log
import com.example.app_android_picture_transfer.data.model.LoggedInUser
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(username: String, password: String): Result<LoggedInUser> {
        try {
            // TODO: handle loggedInUser authentication
            val url = URL("http://172.30.1.32:8080/login")
            val data = """
                {
                    "username" : "${username}",
                    "password" : "${password}"
                }
            """.trimIndent()

            with(url.openConnection() as HttpURLConnection){
                requestMethod = "POST"
                outputStream.write(data.toByteArray())
                doOutput = true

                BufferedReader(InputStreamReader(inputStream)).use {
                    val response = StringBuffer()

                    var inputLine = it.readLine()
                    while (inputLine != null) {
                        response.append(inputLine)
                        inputLine = it.readLine()
                    }
                    it.close()

                    val json = JSONObject(response.toString())
                    if(json.get("state") == "success"){
                        Log.d("STATE", "success")
                        val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), username)
                        return Result.Success(fakeUser)
                    }
                    else{
                        Log.d("STATE", "fail")
                        val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), "unknown")
                        return Result.Success(fakeUser)
                    }
                }
            }

        } catch (e: Throwable) {
            Log.d("STATE", e.message)

            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}