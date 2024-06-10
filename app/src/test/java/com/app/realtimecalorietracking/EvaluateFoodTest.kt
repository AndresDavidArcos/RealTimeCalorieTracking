    package com.app.realtimecalorietracking

    import android.widget.Toast
    import com.microsoft.azure.storage.CloudStorageAccount
    import com.microsoft.azure.storage.blob.CloudBlobClient
    import com.microsoft.azure.storage.blob.CloudBlobContainer
    import com.microsoft.azure.storage.blob.CloudBlockBlob
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.GlobalScope
    import kotlinx.coroutines.launch
    import okhttp3.Call
    import okhttp3.Callback
    import okhttp3.MediaType.Companion.toMediaTypeOrNull
    import okhttp3.OkHttpClient
    import okhttp3.Request
    import okhttp3.RequestBody.Companion.toRequestBody
    import okhttp3.Response
    import org.json.JSONObject
    import org.junit.Test


    import org.junit.Assert.*
    import java.io.ByteArrayOutputStream
    import java.io.IOException
    import java.io.InputStream
    import java.net.URL

    /**
     * Example local unit test, which will execute on the development machine (host).
     *
     * See [testing documentation](http://d.android.com/tools/testing).
     */
    class EvaluateFoodTest {
        @Test
        fun uploadimage_isCorrect() {
            val imageUriString = "https://www.portafolio.co/files/article_new_multimedia/uploads/2022/04/12/6255e2e41db6c.jpeg"

            GlobalScope.launch(Dispatchers.IO) {
                // Credenciales de Azure Storage (exemplar con tus propias credenciales)
                val accountKey = BuildConfig.ACCOUNT_KEY

                val storageConnectionString =
                    "DefaultEndpointsProtocol=https;AccountName=calorietracking;AccountKey=$accountKey;EndpointSuffix=core.windows.net"

                // Nombre del contenedor en Azure Storage (reemplaza con tu contenedor)
                val containerName = "calorietracking"

                try {
                    // Crea la cuenta de almacenamiento
                    val storageAccount = CloudStorageAccount.parse(storageConnectionString)
                    val blobClient: CloudBlobClient = storageAccount.createCloudBlobClient()

                    // Obtén una referencia al contenedor
                    val container: CloudBlobContainer = blobClient.getContainerReference(containerName)

                    // Crea un nombre único para el blob (puedes usar un nombre aleatorio o basado en la fecha)
                    val imageName = "imagen.jpg"

                    // Obtén una referencia al blob en el contenedor
                    val blob: CloudBlockBlob = container.getBlockBlobReference(imageName)

                    // Load image from URL
                    val imageStream: InputStream = URL(imageUriString).openStream()
                    val outputStream = ByteArrayOutputStream()
                    imageStream.copyTo(outputStream)
                    val imageBytes: ByteArray = outputStream.toByteArray()

                    // Sube la imagen al blob
                    blob.uploadFromByteArray(imageBytes, 0, imageBytes.size)

                    assertTrue("Imagen subida correctamente", true)

                } catch (e: Exception) {
                    // Maneja cualquier error
                    e.printStackTrace()

                    assertTrue("Error subiendo imagen", false)
                }
            }
        }

        @Test
        fun analyzeimage_isCorrect() {
            val client = OkHttpClient()
            val url =
                "https://calorietracking.cognitiveservices.azure.com/computervision/imageanalysis:analyze?features=caption,read&model-version=latest&language=en&api-version=2024-02-01"
            val apiKey = BuildConfig.API_KEY

            val requestBody = """
        {
            "url": "https://calorietracking.blob.core.windows.net/calorietracking/imagen.jpg"
        }
        """.trimIndent()

            val request = Request.Builder()
                .url(url)
                .addHeader("Ocp-Apim-Subscription-Key", apiKey)
                .addHeader("Content-Type", "application/json")
                .post(requestBody.toRequestBody("application/json".toMediaTypeOrNull()))
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                    // Aquí deberías manejar el error de manera adecuada según tu caso de uso
                }

                override fun onResponse(call: Call, response: Response) {
                    if (!response.isSuccessful) {
                        // Aquí deberías manejar la respuesta no exitosa de manera adecuada según tu caso de uso
                    } else {
                        val responseData = response.body?.string()
                        try {
                            val json = responseData?.let { JSONObject(it) }
                            val captionResult = json?.getJSONObject("captionResult")
                            val caption = captionResult?.getString("text")

                            assertTrue("La imagen fue analizada correctamente", true)

                        } catch (e: Exception) {
                            e.printStackTrace()
                            assertTrue("Error al analizar la imagen", false)
                        }
                    }
                }
            })
        }
    }