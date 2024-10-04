package project.group3tztechcorp.chefitupapp

import android.Manifest.permission.CAMERA
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.graphics.Rect
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.TextRecognizerOptions
import project.group3tztechcorp.chefitupapp.databinding.ActivityScannerBinding
import project.group3tztechcorp.chefitupapp.splash.SplashScreens
import java.io.IOException
import java.lang.Exception
import java.lang.StringBuilder
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class ScannerActivity : AppCompatActivity() {

    private lateinit var imageBitmap: Bitmap
    private lateinit var selectedImage: Uri
    lateinit var binding: ActivityScannerBinding
    private final var REQUEST_IMAGE_CAPTURE = 1
    private final var REQUEST_PHOTO_CAPTURE = 3
    private var optionSelected: Boolean = false
    private lateinit var scannedText: String
    private lateinit var date: String
    private lateinit var total: String
    private var user: UserInformation = UserInformation()
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    private lateinit var reference: DatabaseReference
    private var username: String = "user"
    private var check: Boolean = false

    private final val myPreferences: String = "MyPref"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_scanner)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_scanner)

        binding.progressBar2.visibility = View.INVISIBLE

        sharedPreferences = getSharedPreferences(myPreferences, Context.MODE_PRIVATE)
        username = sharedPreferences.getString("username", "").toString()

        editor = sharedPreferences.edit()

        binding.backButton.setOnClickListener {
            editor.putString("transition", (4).toString())
            editor.commit()
            var intent: Intent = Intent(this@ScannerActivity, UserInterface::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }

        binding.scannerBtnCamera.setOnClickListener {
            if (checkRewards()) {
                Toast.makeText(
                    this,
                    "You are at the max rewards! You must redeem your reward to get more",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                if (checkPermissions()) {
                    captureImage()
                } else {
                    requestPermission()
                }
                optionSelected = true
            }
        }

        binding.scannerBtnPhotos.setOnClickListener {
            if (checkRewards()) {
                Toast.makeText(
                    this,
                    "You are at the max rewards! You must redeem your reward to get more",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                if (checkPermissions()) {
                    var intent: Intent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(intent, REQUEST_PHOTO_CAPTURE)
                } else {
                    requestPermission()
                }
                optionSelected = false
            }
        }

        binding.scannerBtnScan.setOnClickListener {
            if (checkRewards()) {
                Toast.makeText(
                    this,
                    "You are at the max rewards! You must redeem your reward to get more",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                if (optionSelected) {
                    binding.progressBar2.visibility = View.VISIBLE
                    /*
                    detectText()
                     */
                    addRewards()
                    var intent: Intent =
                        Intent(this@ScannerActivity, SplashScreens::class.java)
                    intent.putExtra("username", username)
                    intent.putExtra("splash", 9)
                    startActivity(intent)
                } else {
                    binding.progressBar2.visibility = View.VISIBLE
                    detectTextPhoto()
                }
            }
        }

    }

    private fun detectText() {
        var image: InputImage = InputImage.fromBitmap(imageBitmap, 0)
        var recognizer: TextRecognizer =
            TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        var result: Task<Text> =
            recognizer.process(image).addOnSuccessListener(object : OnSuccessListener<Text> {
                override fun onSuccess(p0: Text) {
                    var results: StringBuilder = StringBuilder()
                    for (block: Text.TextBlock in p0.textBlocks) {
                        var blockText: String = block.text
                        var blockCornerPoint: Array<out Point>? = block.cornerPoints
                        var blockFrame: Rect = block.boundingBox!!
                        for (line: Text.Line in block.lines) {
                            var lineText: String = line.text
                            var lineCornerPoint: Array<out Point>? = line.cornerPoints
                            var lineRect: Rect = line.boundingBox!!
                            for (element: Text.Element in line.elements) {
                                var elementText: String = element.text
                                results.append(elementText)
                            }
                            binding.scannerText.text = results.toString()
                            scannedText = results.toString()
                        }
                    }
                    scannedText = scannedText.lowercase(Locale.getDefault())
                    if (scannedText.contains("receiptify")) {
                        val p: Pattern = Pattern.compile("\\w{3,9}?\\d{1,2}?,\\d{4}?")
                        val m: Matcher = p.matcher(scannedText)
                        while (m.find()) {
                            date = m.group()
                        }
                        val p1: Pattern = Pattern.compile("total\\d{1,3}?:\\d{2}?")
                        val m1: Matcher = p1.matcher(scannedText)
                        while (m1.find()) {
                            total = m1.group()
                        }
                        try {
                            /*Toast.makeText(
                                this@ScannerActivity,
                                date + "" + total,
                                Toast.LENGTH_SHORT
                            )
                                .show()

                             */
                            addRewards()
                            AlertDialog.Builder(this@ScannerActivity)
                                .setTitle("Success")
                                .setMessage("The receipt is scanable. The total on the receipt is: " + total + ", and the date on it is: " + date + ". Press Ok to continue scanning.")
                                .setPositiveButton("Ok") { dialogInterface, i ->
                                    editor.putString("transition", (1).toString())
                                    editor.commit()
                                    var intent: Intent =
                                        Intent(this@ScannerActivity, SplashScreens::class.java)
                                    intent.putExtra("username", username)
                                    intent.putExtra("splash", 9)
                                    startActivity(intent)
                                }
                                .show()
                        } catch (e: Exception) {
                            Toast.makeText(
                                this@ScannerActivity,
                                "Scan receipt again",
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.progressBar2.visibility = View.INVISIBLE
                        }
                    } else {
                        Toast.makeText(
                            this@ScannerActivity,
                            "incorrect receipt.Try again",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.progressBar2.visibility = View.INVISIBLE
                    }
                }
            }).addOnFailureListener(object : OnFailureListener {
                override fun onFailure(p0: Exception) {
                    Toast.makeText(
                        this@ScannerActivity,
                        "Failed to detect text",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.progressBar2.visibility = View.INVISIBLE
                }

            })
    }

    private fun detectTextPhoto() {
        var image: InputImage = InputImage.fromFilePath(applicationContext, selectedImage)
        var recognizer: TextRecognizer =
            TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        var result: Task<Text> =
            recognizer.process(image).addOnSuccessListener(object : OnSuccessListener<Text> {
                override fun onSuccess(p0: Text) {
                    var results: StringBuilder = StringBuilder()
                    for (block: Text.TextBlock in p0.textBlocks) {
                        var blockText: String = block.text
                        var blockCornerPoint: Array<out Point>? = block.cornerPoints
                        var blockFrame: Rect = block.boundingBox!!
                        for (line: Text.Line in block.lines) {
                            var lineText: String = line.text
                            var lineCornerPoint: Array<out Point>? = line.cornerPoints
                            var lineRect: Rect = line.boundingBox!!
                            for (element: Text.Element in line.elements) {
                                var elementText: String = element.text
                                results.append(elementText)
                            }
                            binding.scannerText.text = results.toString()
                            scannedText = results.toString()
                        }
                    }
                    scannedText = scannedText.lowercase(Locale.getDefault())
                    if (scannedText.contains("receiptify")) {
                        val p: Pattern = Pattern.compile("\\w{3,9}?\\d{1,2}?,\\d{4}?")
                        val m: Matcher = p.matcher(scannedText)
                        while (m.find()) {
                            date = m.group()
                        }
                        val p1: Pattern = Pattern.compile("total\\d{1,3}?:\\d{2}?")
                        val m1: Matcher = p1.matcher(scannedText)
                        while (m1.find()) {
                            total = m1.group()
                        }
                        try {
                            /*Toast.makeText(
                                this@ScannerActivity,
                                date + "" + total,
                                Toast.LENGTH_SHORT
                            )
                                .show()

                             */
                            addRewards()
                            var intent: Intent =
                                Intent(this@ScannerActivity, SplashScreens::class.java)
                            intent.putExtra("username", username)
                            intent.putExtra("splash", 9)
                            startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(
                                this@ScannerActivity,
                                "Scan receipt again",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@ScannerActivity,
                            "incorrect receipt.Try again",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.progressBar2.visibility = View.INVISIBLE
                    }
                }
            }).addOnFailureListener(object : OnFailureListener {
                override fun onFailure(p0: Exception) {
                    Toast.makeText(
                        this@ScannerActivity,
                        "Failed to detect text",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.progressBar2.visibility = View.INVISIBLE
                }

            })
    }

    private fun checkPermissions(): Boolean {
        var camerPermission: Int = ContextCompat.checkSelfPermission(applicationContext, CAMERA)
        return camerPermission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        var PERMISSION_CODE: Int = 200
        ActivityCompat.requestPermissions(this, arrayOf<String>(CAMERA), PERMISSION_CODE)
    }

    private fun captureImage() {
        var takePicture: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePicture.resolveActivity(packageManager) != null) {
            startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.size > 0) {
            var cameraPermission: Boolean = grantResults[0] == PackageManager.PERMISSION_GRANTED
            if (cameraPermission) {
                Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show()
                captureImage()
            } else {
                Toast.makeText(this, "Permission Denied..", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // calling on activity result method.
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // on below line we are getting
            // data from our bundles. .
            val extras = data?.extras
            imageBitmap = (extras!!["data"] as Bitmap?)!!
            // below line is to set the
            // image bitmap to our image.
            binding.scannerImage.setImageBitmap(imageBitmap)
        } else if (requestCode == REQUEST_PHOTO_CAPTURE && resultCode == RESULT_OK) {
            selectedImage = data?.data!!
            binding.scannerImage.setImageURI(selectedImage)
        }
    }

    private fun addRewards() {
        editor.putString("transition", (1).toString())
        editor.commit()
        reference = FirebaseDatabase.getInstance().getReference("userInformation")
        var checkUser: Query = reference.orderByChild("username").equalTo(username)

        checkUser.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    //get data from database
                    var nameFromDB =
                        snapshot.child(username).child("fullName").getValue()
                    var levelFromDB =
                        snapshot.child(username).child("level").getValue()
                    var experienceFromDB =
                        snapshot.child(username).child("experience").getValue()
                    var rewardsNumFromDB =
                        snapshot.child(username).child("rewards").getValue()
                    var recipiesNumFromDB =
                        snapshot.child(username).child("recipesCompleted")
                            .getValue()
                    var achivementsNumFromDB =
                        snapshot.child(username).child("achievementsCompleted")
                            .getValue()

                    user = UserInformation(
                        username,
                        nameFromDB.toString(),
                        levelFromDB.toString().toInt(),
                        experienceFromDB.toString().toInt(),
                        rewardsNumFromDB.toString().toInt(),
                        recipiesNumFromDB.toString().toInt(),
                        achivementsNumFromDB.toString().toInt()
                    )

                    user.addRewards()

                    reference.child(username).child("rewards").setValue(user.rewards)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun checkRewards(): Boolean {
        reference = FirebaseDatabase.getInstance().getReference("userInformation")
        var checkUser: Query = reference.orderByChild("username").equalTo(username)

        checkUser.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    //get data from database
                    var rewardsNumFromDB =
                        snapshot.child(username).child("rewards").getValue()

                    check = rewardsNumFromDB.toString().toInt() == 3
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        return check
    }
}