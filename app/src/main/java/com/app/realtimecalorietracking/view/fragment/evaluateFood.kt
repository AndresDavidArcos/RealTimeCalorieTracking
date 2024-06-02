package com.app.realtimecalorietracking.view.fragment

import android.net.Uri
import android.Manifest
import android.os.Build
import android.util.Log
import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.Button
import android.content.Intent
import android.view.ViewGroup
import android.widget.ImageView
import android.view.LayoutInflater
import android.provider.MediaStore
import android.content.ContentValues
import androidx.fragment.app.Fragment
import com.app.realtimecalorietracking.R
import androidx.activity.result.ActivityResult
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.app.realtimecalorietracking.databinding.FragmentEvaluateFoodBinding
import com.app.realtimecalorietracking.model.FoodResponse
import com.app.realtimecalorietracking.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class evaluateFood : Fragment() {

    companion object {
        private const val TAG = "MAIN_TAG"
        private const val APP_ID = "d672cbed"
        private const val APP_KEY = "5bbd80e51d5d55f97365f0d16e7d1649"
    }

    private lateinit var mCaptureBtn: Button
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
                    Toast.makeText(requireContext(), "Camera or Storage or both permissions denied...", Toast.LENGTH_SHORT).show()
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
        image_uri = requireContext().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
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
        } else {
            //Cancelled
            Toast.makeText(requireContext(), "Cancelled...!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCaloriesForDish(dish: String, callback: (Float?) -> Unit) {
        val call = RetrofitInstance.api.getFoodCalories(dish, APP_ID, APP_KEY)
        call.enqueue(object : Callback<FoodResponse> {
            override fun onResponse(call: Call<FoodResponse>, response: Response<FoodResponse>) {
                if (response.isSuccessful) {
                    val calories = response.body()?.parsed?.firstOrNull()?.food?.nutrients?.ENERC_KCAL
                    callback(calories)
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<FoodResponse>, t: Throwable) {
                callback(null)
            }
        })
    }

    private fun showCaloriesForDish(dish: String) {
        getCaloriesForDish(dish) { calories ->
            val message = if (calories != null) {
                "The dish $dish has approximately $calories kcal."
            } else {
                "Could not retrieve calories for $dish."
            }
            Log.i("FOOD", "se recibio $dish y resulto tener $calories kcal")
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        }
    }



}
