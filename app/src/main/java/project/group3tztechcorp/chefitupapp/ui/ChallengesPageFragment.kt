package project.group3tztechcorp.chefitupapp.ui

import android.content.Context
import android.content.QuickViewConstants
import android.content.SharedPreferences
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
import project.group3tztechcorp.chefitupapp.R
import project.group3tztechcorp.chefitupapp.Recipe
import project.group3tztechcorp.chefitupapp.UserChallenges
import project.group3tztechcorp.chefitupapp.UserInterface
import project.group3tztechcorp.chefitupapp.adapter.ChallengesAdapter
import project.group3tztechcorp.chefitupapp.adapter.CompletedRecipeAdapter
import project.group3tztechcorp.chefitupapp.databinding.FragmentChallengesPageBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private const val TAG = "MyActivity"

/**
 * A simple [Fragment] subclass.
 * Use the [ChallengesPageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChallengesPageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var reference: DatabaseReference
    private lateinit var reference2: DatabaseReference
    private lateinit var reference3: DatabaseReference
    private lateinit var reference4: DatabaseReference
    private lateinit var recipeRecyclerView: RecyclerView
    private lateinit var recipeArrayList: ArrayList<Recipe>
    lateinit var binding: FragmentChallengesPageBinding
    private lateinit var challengesList: ArrayList<Recipe>
    private lateinit var userChallengesList: ArrayList<String>
    lateinit var localDate: Calendar
    lateinit var dateFormat: SimpleDateFormat
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var date: String
    private var max: Int = 0
    private var min: Int = 0
    private var percent: Int = 0

    private final val myPreferences: String = "MyPref"

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_challenges_page, container, false)

        localDate = Calendar.getInstance()
        dateFormat = SimpleDateFormat("MM/dd/yyyy")
        date = dateFormat.format(localDate.time).toString().trim()

        sharedPreferences = this.requireActivity().getSharedPreferences(myPreferences, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        Log.i(TAG, "this is the date" + date)

        recipeRecyclerView = binding.recycleViewChallenges
        recipeRecyclerView.layoutManager = LinearLayoutManager(container?.context, LinearLayoutManager.VERTICAL, false)
        recipeRecyclerView.setHasFixedSize(true)

        recipeArrayList = arrayListOf<Recipe>()
        challengesList = arrayListOf<Recipe>()
        userChallengesList = arrayListOf<String>()

        getProgress()

        getChallengesRecipies()

        return binding.root
        //return inflater.inflate(R.layout.fragment_challenges_page, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChallengesPageFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChallengesPageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun getChallengesRecipies(){
        var username = arguments?.getString("userName").toString().trim()
        var savedDate = sharedPreferences.getString("date", "").toString().trim()

        Log.i(TAG, "The saved date is: " + savedDate)
        reference = FirebaseDatabase.getInstance().getReference("recipes")
        reference2 = FirebaseDatabase.getInstance().getReference("userInformation")
        reference3 = FirebaseDatabase.getInstance().getReference("userChallenges")

        Log.i(TAG, username)
        var check: Query = reference2.orderByChild("username").equalTo(username)
        var check3: Query = reference3.orderByChild("username").equalTo(username)

        check.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    //get data from database
                    var levelFromDB = snapshot.child(username).child("level").getValue()

                    Log.i(TAG, "user exists")
                    if(!savedDate.equals(date)) {
                        Log.i(TAG, "savedDate and date dont equal same")
                        Log.i(TAG, savedDate)
                        Log.i(TAG, date)
                        editor.putString("date", date)
                        editor.commit()
                        Log.i(TAG, "This is the new shared preferenece " + sharedPreferences.getString("date", "").toString())
                        reference.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {
                                    Log.i(TAG, "recipes exist")
                                    for (recipeSnapshot in snapshot.children) {
                                        if (levelFromDB.toString().toInt() == 1) {
                                            if (recipeSnapshot.child("Level").getValue().toString()
                                                    .equals("Easy") or recipeSnapshot.child("Level")
                                                    .getValue().toString().equals("Intermediate")
                                            ) {
                                                val recipe =
                                                    recipeSnapshot.getValue(Recipe::class.java)
                                                recipeArrayList.add(recipe!!)
                                                Log.i(TAG, "level 1 user")
                                            }
                                        } else if (levelFromDB.toString().toInt() == 2) {
                                            if (recipeSnapshot.child("Level").getValue().toString()
                                                    .equals("Easy") or recipeSnapshot.child("Level")
                                                    .getValue().toString().equals("Intermediate")
                                            ) {
                                                val recipe =
                                                    recipeSnapshot.getValue(Recipe::class.java)
                                                recipeArrayList.add(recipe!!)
                                                Log.i(TAG, "level 2 user")
                                            }
                                        } else if (levelFromDB.toString().toInt() == 3) {
                                            if (recipeSnapshot.child("Level").getValue().toString()
                                                    .equals("Intermediate") or recipeSnapshot.child(
                                                    "Level"
                                                ).getValue().toString().equals("Hard")
                                            ) {
                                                val recipe =
                                                    recipeSnapshot.getValue(Recipe::class.java)
                                                recipeArrayList.add(recipe!!)
                                                Log.i(TAG, "level 3 user")
                                            }
                                        } else if (levelFromDB.toString().toInt() == 4) {
                                            if (recipeSnapshot.child("Level").getValue().toString()
                                                    .equals("Intermediate") or recipeSnapshot.child(
                                                    "Level"
                                                ).getValue().toString().equals("Hard")
                                            ) {
                                                val recipe =
                                                    recipeSnapshot.getValue(Recipe::class.java)
                                                recipeArrayList.add(recipe!!)
                                                Log.i(TAG, "level 4 user")
                                            }
                                        } else if (levelFromDB.toString().toInt() == 5) {
                                            if (recipeSnapshot.child("Level").getValue().toString()
                                                    .equals("Hard")
                                            ) {
                                                val recipe =
                                                    recipeSnapshot.getValue(Recipe::class.java)
                                                recipeArrayList.add(recipe!!)
                                                Log.i(TAG, "level 5 user")
                                            }
                                        }
                                    }
                                    recipeArrayList.shuffle()
                                    if (challengesList.isEmpty()) {
                                        challengesList.add(recipeArrayList.get(0))
                                        challengesList.add(recipeArrayList.get(1))
                                    }

                                    userChallengesList.add(challengesList[0].Name.toString())
                                    userChallengesList.add(challengesList[1].Name.toString())
                                    check3.addListenerForSingleValueEvent(object : ValueEventListener{
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            if(snapshot.exists()){
                                                reference3.child(username).child("challengeList").setValue(userChallengesList)
                                            } else {
                                                val userChallenge = UserChallenges(username, userChallengesList)
                                                reference3.child(username).setValue(userChallenge)
                                            }
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            TODO("Not yet implemented")
                                        }

                                    })
                                    recipeRecyclerView.adapter = ChallengesAdapter(challengesList)
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                        })
                    } else {
                        Log.i(TAG, "savedDate and date equal same")
                        Log.i(TAG, savedDate)
                        Log.i(TAG, date)
                        check3.addListenerForSingleValueEvent(object : ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if(snapshot.exists()){
                                    Log.i(TAG, "User exists")
                                    Log.i(TAG, savedDate)
                                    Log.i(TAG, date)
                                    if(userChallengesList.isEmpty()) {
                                        snapshot.child(username)
                                            .child("challengeList").children.forEach {
                                            userChallengesList.add(it.getValue().toString())
                                        }
                                    }

                                    reference.addValueEventListener(object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            if (snapshot.exists()) {
                                                for (recipeSnapshot in snapshot.children) {
                                                    for (item in userChallengesList){
                                                        if (item.equals(
                                                                recipeSnapshot.child("Name").getValue().toString()
                                                                    .trim())){
                                                            val recipe = recipeSnapshot.getValue(Recipe::class.java)
                                                            challengesList.add(recipe!!)
                                                        }
                                                    }
                                                }
                                                recipeRecyclerView.adapter = ChallengesAdapter(challengesList)
                                            }
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            TODO("Not yet implemented")
                                        }

                                    })

                                } else {
                                    Log.i(TAG, "User doesnt exist")
                                    Log.i(TAG, savedDate)
                                    Log.i(TAG, date)
                                    reference.addValueEventListener(object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            if (snapshot.exists()) {
                                                Log.i(TAG, "recipes exist")
                                                for (recipeSnapshot in snapshot.children) {
                                                    if (levelFromDB.toString().toInt() == 1) {
                                                        if (recipeSnapshot.child("Level").getValue().toString()
                                                                .equals("Easy") or recipeSnapshot.child("Level")
                                                                .getValue().toString().equals("Intermediate")
                                                        ) {
                                                            val recipe =
                                                                recipeSnapshot.getValue(Recipe::class.java)
                                                            recipeArrayList.add(recipe!!)
                                                            Log.i(TAG, "level 1 user")
                                                        }
                                                    } else if (levelFromDB.toString().toInt() == 2) {
                                                        if (recipeSnapshot.child("Level").getValue().toString()
                                                                .equals("Easy") or recipeSnapshot.child("Level")
                                                                .getValue().toString().equals("Intermediate")
                                                        ) {
                                                            val recipe =
                                                                recipeSnapshot.getValue(Recipe::class.java)
                                                            recipeArrayList.add(recipe!!)
                                                            Log.i(TAG, "level 2 user")
                                                        }
                                                    } else if (levelFromDB.toString().toInt() == 3) {
                                                        if (recipeSnapshot.child("Level").getValue().toString()
                                                                .equals("Intermediate") or recipeSnapshot.child(
                                                                "Level"
                                                            ).getValue().toString().equals("Hard")
                                                        ) {
                                                            val recipe =
                                                                recipeSnapshot.getValue(Recipe::class.java)
                                                            recipeArrayList.add(recipe!!)
                                                            Log.i(TAG, "level 3 user")
                                                        }
                                                    } else if (levelFromDB.toString().toInt() == 4) {
                                                        if (recipeSnapshot.child("Level").getValue().toString()
                                                                .equals("Intermediate") or recipeSnapshot.child(
                                                                "Level"
                                                            ).getValue().toString().equals("Hard")
                                                        ) {
                                                            val recipe =
                                                                recipeSnapshot.getValue(Recipe::class.java)
                                                            recipeArrayList.add(recipe!!)
                                                            Log.i(TAG, "level 4 user")
                                                        }
                                                    } else if (levelFromDB.toString().toInt() == 5) {
                                                        if (recipeSnapshot.child("Level").getValue().toString()
                                                                .equals("Hard")
                                                        ) {
                                                            val recipe =
                                                                recipeSnapshot.getValue(Recipe::class.java)
                                                            recipeArrayList.add(recipe!!)
                                                            Log.i(TAG, "level 5 user")
                                                        }
                                                    }
                                                }
                                                recipeArrayList.shuffle()
                                                if (challengesList.isEmpty()) {
                                                    challengesList.add(recipeArrayList.get(0))
                                                    challengesList.add(recipeArrayList.get(1))
                                                }

                                                userChallengesList.add(challengesList[0].Name.toString())
                                                userChallengesList.add(challengesList[1].Name.toString())

                                                val userChallenge = UserChallenges(username, userChallengesList)
                                                reference3.child(username).setValue(userChallenge)

                                                recipeRecyclerView.adapter = ChallengesAdapter(challengesList)
                                            }
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            TODO("Not yet implemented")
                                        }

                                    })
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                        })

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun getProgress(){
        var fullname = arguments?.getString("fullName").toString().trim()
        var username = arguments?.getString("userName").toString().trim()

        var reference4: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("userInformation")

        var checkUser: Query = reference4.orderByChild("username").equalTo(username)

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
}