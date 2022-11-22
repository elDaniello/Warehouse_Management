package put.dkotynski.warehouse.management.data

import okhttp3.*
import put.dkotynski.warehouse.management.data.model.LoggedInUser
import okhttp3.RequestBody.Companion.toRequestBody

import java.io.IOException
import java.util.concurrent.CompletableFuture

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {
    val apiURL = "http://localhost:5000"
    fun login(username: String, password: String):Result<LoggedInUser> {
        try {
            // TODO: handle loggedInUser authentication
            val okHttpClient = OkHttpClient()

            val formBody = FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build()

            val request = Request.Builder()
                .url("$apiURL/login")
                .post(formBody)
                .build()

            val call = okHttpClient.newCall(request)
            val response = call.execute()

            return if (response.code == 200)
                Result.Success(LoggedInUser(java.util.UUID.randomUUID().toString(), username))
            else
                Result.Error(Exception("bad password or idk"))



        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
   }

    fun logout() {
        // TODO: revoke authentication
        val admin = """{"username": "admin@co.m", password":"admin123"}"""
        val antyadmin = """{"username":"null@nu.ll", "password": "qwerty123"}"""
    }
}
