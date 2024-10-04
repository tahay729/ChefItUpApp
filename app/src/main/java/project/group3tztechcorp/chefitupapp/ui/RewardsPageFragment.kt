package project.group3tztechcorp.chefitupapp.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import org.json.JSONObject
import project.group3tztechcorp.chefitupapp.R
import project.group3tztechcorp.chefitupapp.Reward
import project.group3tztechcorp.chefitupapp.RewardsPage
import project.group3tztechcorp.chefitupapp.ScannerActivity
import project.group3tztechcorp.chefitupapp.adapter.RewardsAdapter
import project.group3tztechcorp.chefitupapp.databinding.FragmentRewardsPageBinding
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.URL
import java.net.URLEncoder
import javax.net.ssl.HttpsURLConnection

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RewardsPageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RewardsPageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var reference: DatabaseReference
    private lateinit var reference2: DatabaseReference
    private lateinit var rewardRecyclerView: RecyclerView
    private lateinit var rewardsArrayList: ArrayList<Reward>
    lateinit var binding: FragmentRewardsPageBinding
    private var params: JSONObject? = null
    private var resultFinal:String? = null
    private var max: Int = 0
    private var min: Int = 0
    private var percent: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_rewards_page, container, false)
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_rewards_page, container, false)

        rewardsArrayList = arrayListOf<Reward>()

        rewardRecyclerView = binding.rewardsRecycler
        rewardRecyclerView.layoutManager = LinearLayoutManager(container?.context, LinearLayoutManager.HORIZONTAL, false)
        rewardRecyclerView.setHasFixedSize(true)

        getProgress()
        getRewards()

        binding.button.setOnClickListener {

            /*
            val url = URL("https://api.ocr.space/parse/image")
            val con = url.openConnection() as HttpsURLConnection
            Log.d("myTag", "part1");

            con.requestMethod = "POST"
            con.setRequestProperty("User-Agent", "Mozilla/5.0")
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5")

            val postDataParams = JSONObject()

            postDataParams.put("apikey", "6be08db4d888957")
            postDataParams.put("isOverlayRequired", true)
            postDataParams.put("url", "https://cdn.filestackcontent.com/iYfZsZ8mSEWP85T8gHve")
            postDataParams.put("language", "eng")
            Log.d("myTag", "part2");
            con.doOutput = true
            val wr = DataOutputStream(con.outputStream)
            Log.d("myTag", "part3");
            wr.writeBytes(getPostDataString(postDataParams))
            Log.d("myTag", "part4");
            wr.flush()
            Log.d("myTag", "part5");
            wr.close()
            Log.d("myTag", "part6");

            val `in` = BufferedReader(InputStreamReader(con.inputStream))
            var inputLine: String?
            val response = StringBuilder()

            while (`in`.readLine().also { inputLine = it } != null) {
                response.append(inputLine)
            }
            `in`.close()
            print(3)
            Log.d("OCR.Space", response.toString())
            val jsonData = JSONObject(response.toString())

            print(4)
            if (jsonData.has("ParsedResults")) {
                params= jsonData.getJSONArray("ParsedResults").getJSONObject(0)
            } else if (jsonData.has("IsErroredOnProcessing") && jsonData.getBoolean("IsErroredOnProcessing")) {
                params= jsonData
            }
            val result = StringBuilder()
            val itr = params!!.keys()
            print(5)

            var first = true
            while (itr.hasNext()) {
                val key = itr.next()
                val value = params!![key]
                if (first) first = false else result.append("&")
                result.append(URLEncoder.encode(key, "UTF-8"))
                result.append("=")
                result.append(URLEncoder.encode(value.toString(), "UTF-8"))
                print(6)
            }
            resultFinal=result.toString()
            print(resultFinal)

             */
            startActivity(Intent(context, ScannerActivity::class.java))
        }

        return binding.root
    }

    fun getProgress(){
        var fullname = arguments?.getString("fullName").toString().trim()
        var username = arguments?.getString("userName").toString().trim()

        var reference2: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("userInformation")

        var checkUser: Query = reference2.orderByChild("username").equalTo(username)

        checkUser.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    //get data from database
                    var levelFromDB = snapshot.child(username).child("level").getValue()
                    var experienceFromDB = snapshot.child(username).child("experience").getValue()

                    if (levelFromDB.toString().toInt() == 1) {
                        min = 0
                        max = 300
                        percent = experienceFromDB.toString().toInt() - min
                        binding.experienceBar.max = max - min

                        binding.experienceBar.progress = percent
                    } else if (levelFromDB.toString().toInt() == 2) {
                        min = 301
                        max = 701
                        percent = experienceFromDB.toString().toInt() - min
                        binding.experienceBar.max = max - min

                        binding.experienceBar.progress = percent
                    } else if (levelFromDB.toString().toInt() == 3) {
                        min = 701
                        max = 1301
                        percent = experienceFromDB.toString().toInt() - min
                        binding.experienceBar.max = max - min

                        binding.experienceBar.progress = percent
                    } else if (levelFromDB.toString().toInt() == 4) {
                        min = 1301
                        max = 2101
                        percent = experienceFromDB.toString().toInt() - min
                        binding.experienceBar.max = max - min

                        binding.experienceBar.progress = percent
                    } else if (levelFromDB.toString().toInt() == 5) {
                        min = 2101
                        max = 2101
                        percent = 0
                        binding.experienceBar.progress = percent
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun getRewards(){
        reference = FirebaseDatabase.getInstance().getReference("Rewards")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for(rewardSnapshot in snapshot.children){
                        val reward = rewardSnapshot.getValue(Reward::class.java)
                        rewardsArrayList.add(reward!!)
                    }
                    rewardRecyclerView.adapter = RewardsAdapter(rewardsArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    @Throws(java.lang.Exception::class)
    private fun getPostDataString(params: JSONObject): String? {
        val result = StringBuilder()
        val itr = params.keys()
        var first = true
        while (itr.hasNext()) {
            val key = itr.next()
            val value = params[key]
            if (first) first = false else result.append("&")
            result.append(URLEncoder.encode(key, "UTF-8"))
            result.append("=")
            result.append(URLEncoder.encode(value.toString(), "UTF-8"))
        }
        return result.toString()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RewardsPageFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RewardsPageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}