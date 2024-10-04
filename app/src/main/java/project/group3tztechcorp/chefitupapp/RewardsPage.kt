package project.group3tztechcorp.chefitupapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import project.group3tztechcorp.chefitupapp.adapter.RewardsAdapter
import project.group3tztechcorp.chefitupapp.databinding.ActivityRewardsPageBinding

class RewardsPage : AppCompatActivity() {

    private lateinit var reference: DatabaseReference
    private lateinit var reference2: DatabaseReference
    private lateinit var rewardRecyclerView: RecyclerView
    private lateinit var rewardsArrayList: ArrayList<Reward>
    lateinit var binding: ActivityRewardsPageBinding
    lateinit var username: String
    lateinit var fullName: String
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    private var reward: String = "none"
    private var maxReached: Boolean = false

    private final val myPreferences: String = "MyPref"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_rewards_page)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rewards_page)

        sharedPreferences = getSharedPreferences(myPreferences, Context.MODE_PRIVATE)
        username = sharedPreferences.getString("username", "").toString()
        fullName = sharedPreferences.getString("fullName", "").toString()

        editor = sharedPreferences.edit()

        rewardRecyclerView = binding.rewardsRecycler
        rewardRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rewardRecyclerView.setHasFixedSize(true)

        rewardsArrayList = arrayListOf<Reward>()

        getUserRewards()
        getRewards()

        binding.backButton.setOnClickListener {
            editor.putString("transition", (1).toString())
            editor.commit()
            var intent: Intent = Intent(this@RewardsPage, UserInterface::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }

        binding.rewardPresent.setOnClickListener{
            if(maxReached){
                rewardsArrayList.shuffle()
                reward = rewardsArrayList[0].name.toString()

                var intent: Intent = Intent(this@RewardsPage, RedeemReward::class.java)
                intent.putExtra("reward", reward)
                startActivity(intent)

            }else{
                Toast.makeText(this, "You need 3 reward points to get a reward.", Toast.LENGTH_SHORT).show()
            }
        }
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

    fun getUserRewards(){
        reference2 = FirebaseDatabase.getInstance().getReference("userInformation")
        var checkUser: Query = reference2.orderByChild("username").equalTo(username)

        checkUser.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    //get data from database
                    var rewardsNumFromDB =
                        snapshot.child(username).child("rewards").getValue()

                    binding.rewardsNumber.text = rewardsNumFromDB.toString() + "/3"
                    if(rewardsNumFromDB.toString().toInt() == 3){
                        maxReached = true
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}