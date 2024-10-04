package project.group3tztechcorp.chefitupapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import project.group3tztechcorp.chefitupapp.adapter.AchievementsAdapter
import project.group3tztechcorp.chefitupapp.databinding.ActivityAchievementsBinding

class Achievements : AppCompatActivity() {

    private lateinit var reference: DatabaseReference
    private lateinit var reference2: DatabaseReference
    private lateinit var reference3: DatabaseReference
    private lateinit var achievementRecyclerView: RecyclerView
    private lateinit var achievementArrayList: ArrayList<Achievement>
    private var completedList: ArrayList<String> = ArrayList<String>()
    lateinit var binding:ActivityAchievementsBinding
    private var username: String = "user"
    private var count: Int = 0
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    private final val myPreferences: String = "MyPref"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_achievements)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_achievements)

        achievementArrayList = arrayListOf<Achievement>()

        sharedPreferences = getSharedPreferences(myPreferences, Context.MODE_PRIVATE)
        username = sharedPreferences.getString("username", "").toString()

        editor = sharedPreferences.edit()

        var reference3: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("userInformation")

        var checkUser: Query = reference3.orderByChild("username").equalTo(username)

        achievementRecyclerView = binding.achievementsRecycleView
        achievementRecyclerView.layoutManager = GridLayoutManager(this, 3)
        achievementRecyclerView.setHasFixedSize(true)

        getAchievements()

        checkUser.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    //get data from database
                    var achivementsNumFromDB = snapshot.child(username).child("achievementsCompleted").getValue()
                    //set the data
                    binding.userName.text = username
                    binding.achievementsCompleted.text = "Completed: " + achivementsNumFromDB.toString() + "/" + count.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        binding.backButton.setOnClickListener {
            editor.putString("transition", (1).toString())
            editor.commit()
            var intent: Intent = Intent(this@Achievements, UserInterface::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }
    }

    fun getAchievements(){
        reference = FirebaseDatabase.getInstance().getReference("Achievements")
        reference2 = FirebaseDatabase.getInstance().getReference("userAchievements")
        var check: Query = reference2.orderByChild("username").equalTo(username)

        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    if(achievementArrayList.isEmpty()) {
                        for (achievementSnapshot in snapshot.children) {
                            val achievement = achievementSnapshot.getValue(Achievement::class.java)
                            achievementArrayList.add(achievement!!)
                        }
                        count = achievementArrayList.size
                    }
                    check.addValueEventListener(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if(snapshot.exists()){
                                snapshot.child("achievementsList").children.forEach {
                                    completedList.add(it.getValue().toString())
                                }
                                for(item in completedList){
                                    for (achievement in achievementArrayList){
                                        if(item.equals(achievement.name)){
                                            achievement.completed = true
                                        }
                                    }
                                }
                                achievementRecyclerView.adapter = AchievementsAdapter(achievementArrayList)
                            } else {
                                achievementRecyclerView.adapter = AchievementsAdapter(achievementArrayList)
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