package com.videoapp.testingvideoapp.data.repository.login

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.videoapp.testingvideoapp.BuildConfig
import com.videoapp.testingvideoapp.infrasctructure.Result
import com.videoapp.testingvideoapp.data.repository.user.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.UUID
import java.util.concurrent.CompletableFuture

class LoginRepositoryImpl(
    private val context: Context,
    private val userRepository: UserRepository
) : LoginRepository {

    override fun signInWithGoogle(): CompletableFuture<Result> {

        val future = CompletableFuture<Result>()

        val credentialManager = CredentialManager.create(context)

        val webId = BuildConfig.WEB_CLIENT_ID

        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(serverClientId = webId)
            .setAutoSelectEnabled(autoSelectEnabled = true)
            .setNonce(nonce = getNonce())
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(credentialOption = googleIdOption)
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = context,
                )

                val credential = result.credential

                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

                val id = googleIdTokenCredential.id
                val displayName = googleIdTokenCredential.displayName ?: "new user"
                val profilePictureUri = googleIdTokenCredential.profilePictureUri ?: ""

                val savingResult = userRepository.saveUserData(
                    googleId = id,
                    displayName = displayName,
                    photoUrl = profilePictureUri.toString()
                ).get()

                future.complete(savingResult)

            } catch (e: GetCredentialException) {
                future.complete(Result.Failure(e.message))
            }
        }
        return future
    }

    override fun signOut(): CompletableFuture<Result> {
        return userRepository.deleteUserData()
    }

    private fun getNonce(): String {
        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")

        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }
}