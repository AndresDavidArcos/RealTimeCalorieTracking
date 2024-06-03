package com.app.realtimecalorietracking.view.fragment

import android.app.AlertDialog
import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.app.realtimecalorietracking.BuildConfig
import com.app.realtimecalorietracking.R
import com.app.realtimecalorietracking.databinding.FragmentEvaluateFoodBinding
import com.app.realtimecalorietracking.model.Food
import com.microsoft.azure.storage.CloudStorageAccount
import com.microsoft.azure.storage.blob.CloudBlobClient
import com.microsoft.azure.storage.blob.CloudBlobContainer
import com.microsoft.azure.storage.blob.CloudBlockBlob
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import com.app.realtimecalorietracking.model.FoodResponse
import com.app.realtimecalorietracking.network.RetrofitInstance
import com.google.firebase.firestore.FirebaseFirestore
import okhttp3.Call as OkHttpCall
import okhttp3.Callback as OkHttpCallback
import okhttp3.Response as OkHttpResponse
import retrofit2.Call as RetrofitCall
import retrofit2.Callback as RetrofitCallback
import retrofit2.Response as RetrofitResponse
import android.content.Context

import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class evaluateFood : Fragment() {

    companion object {
        private const val TAG = "MAIN_TAG"
        private const val APP_ID = "d672cbed"
        private const val APP_KEY = "5bbd80e51d5d55f97365f0d16e7d1649"
    }

    private lateinit var mCaptureBtn: Button
    private lateinit var mCalculateCaloriesBtn: Button
    private lateinit var mImageView: ImageView

    private var image_uri: Uri? = null

    private lateinit var binding: FragmentEvaluateFoodBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_evaluate_food, container, false)
        mImageView = view.findViewById(R.id.imageView)
        mCaptureBtn = view.findViewById(R.id.captureButton)
        mCalculateCaloriesBtn = view.findViewById(R.id.calculateCaloriesButton)

        //button click
        mCaptureBtn.setOnClickListener(View.OnClickListener {
            //Camera is clicked we need to check if we have permission of Camera, Storage before launching Camera to Capture image
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                //Device version is TIRAMISU or above. We only need Camera permission
                val cameraPermissions = arrayOf(Manifest.permission.CAMERA)
                requestCameraPermissions.launch(cameraPermissions)
            } else {
                //Device version is below TIRAMISU. We need Camera & Storage permissions
                val cameraPermissions =
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestCameraPermissions.launch(cameraPermissions)
            }
        })
        // Button click to calculate calories
        mCalculateCaloriesBtn.setOnClickListener(View.OnClickListener {
            // Add functionality to calculate calories
            mCaptureBtn.isEnabled = false
            mCalculateCaloriesBtn.isEnabled = false
            Toast.makeText(requireContext(), "Calculating calories...", Toast.LENGTH_SHORT).show()
            uploadImage()
        })
        return view
    }


    private val requestCameraPermissions =
        registerForActivityResult<Array<String>, Map<String, Boolean>>(
            ActivityResultContracts.RequestMultiplePermissions(),
            ActivityResultCallback<Map<String, Boolean>> { result ->
                Log.d(TAG, "onActivityResult: ")
                Log.d(TAG, "onActivityResult: $result")

                //let's check if permissions are granted or not
                var areAllGranted = true
                for (isGranted in result.values) {
                    areAllGranted = areAllGranted && isGranted
                }
                if (areAllGranted) {
                    //All Permissions Camera, Storage are granted, we can now launch camera to capture image
                    pickImageCamera()
                } else {
                    //Camera or Storage or Both permissions are denied, Can't launch camera to capture image
                    Toast.makeText(
                        requireContext(),
                        "Camera or Storage or both permissions denied...",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )

    private fun pickImageCamera() {
        Log.d(TAG, "pickImageCamera: ")
        //Setup Content values, MediaStore to capture high quality image using camera intent
        val contentValues = ContentValues()
        contentValues.put(MediaStore.Images.Media.TITLE, "TEMPORARY_IMAGE")
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "TEMPORARY_IMAGE_DESCRIPTION")
        //Uri of the image to be captured from camera
        image_uri = requireContext().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
        //Intent to launch camera
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        cameraActivityResultLauncher.launch(intent)
    }

    private val cameraActivityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Log.d(TAG, "onActivityResult: ")
        //Check if image is picked or not
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            //no need to get image uri here we will have it in pickImageCamera() function
            Log.d(TAG, "onActivityResult: imageUri: $image_uri")
            // Display the image
            mImageView.setImageURI(image_uri)
            // Show the calculate calories button
            mCalculateCaloriesBtn.visibility = View.VISIBLE
        } else {
            //Cancelled
            Toast.makeText(requireContext(), "Cancelled...!", Toast.LENGTH_SHORT).show()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun uploadImage() {
        GlobalScope.launch(Dispatchers.IO) {
            // Credenciales de Azure Storage (reemplaza con tus propias credenciales)
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

                // Convierte la imagen en un array de bytes
                val imageStream: InputStream? =
                    requireContext().contentResolver.openInputStream(image_uri!!)
                val outputStream = ByteArrayOutputStream()
                imageStream?.copyTo(outputStream)
                val imageBytes: ByteArray = outputStream.toByteArray()

                // Sube la imagen al blob
                blob.uploadFromByteArray(imageBytes, 0, imageBytes.size)

                // Notifica al usuario que la imagen se ha subido con éxito
                // withContext(Dispatchers.Main) {
                //    Toast.makeText(
                //        requireContext(),
                //        "Imagen subida exitosamente",
                //        Toast.LENGTH_SHORT
                //    ).show()
                // }

                // Llama a la función para calcular las calorías
                calculateCalories()

            } catch (e: Exception) {
                // Maneja cualquier error
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error al subir la imagen", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun calculateCalories() {
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

        client.newCall(request).enqueue(object : OkHttpCallback {
            override fun onFailure(call: OkHttpCall, e: IOException) {
                e.printStackTrace()
                requireActivity().runOnUiThread {
                    Toast.makeText(
                        requireContext(),
                        "Error al calcular las calorías",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onResponse(call: OkHttpCall, response: OkHttpResponse) {
                if (!response.isSuccessful) {
                    requireActivity().runOnUiThread {
                        Toast.makeText(
                            requireContext(),
                            "Error en la respuesta de la API",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    throw IOException("Unexpected code $response")
                } else {
                    val responseData = response.body?.string()
                    try {
                        val json = responseData?.let { JSONObject(it) }
                        val captionResult = json?.getJSONObject("captionResult")
                        val caption = captionResult?.getString("text")

                        requireActivity().runOnUiThread {
                            Toast.makeText(
                                requireContext(),
                                "Comida: $caption",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        caption?.let { showCaloriesForDish(it) }

                    } catch (e: Exception) {
                        e.printStackTrace()
                        requireActivity().runOnUiThread {
                            Toast.makeText(
                                requireContext(),
                                e.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        })
    }

    private fun getCaloriesForDish(dish: String, callback: (Float?) -> Unit) {
        val call = RetrofitInstance.api.getFoodCalories(dish, APP_ID, APP_KEY)
        call.enqueue(object : RetrofitCallback<FoodResponse> {
            override fun onResponse(call: RetrofitCall<FoodResponse>, response: RetrofitResponse<FoodResponse>) {
                if (response.isSuccessful) {
                    val calories = response.body()?.parsed?.firstOrNull()?.food?.nutrients?.ENERC_KCAL
                    callback(calories)
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: RetrofitCall<FoodResponse>, t: Throwable) {
                callback(null)
            }
        })
    }

    private fun showCaloriesForDish(dish: String) {
        getCaloriesForDish(dish) { calories ->
            if (calories != null) {
                val message = "Food $dish has been identified  and has approximately $calories kcal. ¿Want to add it?"
                AlertDialog.Builder(requireContext())
                    .setTitle("Food calories")
                    .setMessage(message)
                    .setPositiveButton("OK") { dialog, _ ->
                        saveDishToFirebase(dish, calories)
                        dialog.dismiss()
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            } else {
                Toast.makeText(requireContext(), "Could not get calories for $dish.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveDishToFirebase(dish: String, calories: Float) {
        val db = FirebaseFirestore.getInstance()
        val sharedPref = requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val email = sharedPref.getString("email", null)

        val food = Food(
            user_id = email!!,
            date = Date(),
            name = dish,
            calories = calories.toInt()
        )

        db.collection("foods")
            .add(food)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                Toast.makeText(requireContext(), "Food added correctly.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
                Toast.makeText(requireContext(), "Error when adding food.", Toast.LENGTH_SHORT).show()
            }
    }
}
