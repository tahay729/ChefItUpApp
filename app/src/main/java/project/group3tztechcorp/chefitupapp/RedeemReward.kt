package project.group3tztechcorp.chefitupapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import project.group3tztechcorp.chefitupapp.adapter.RewardsAdapter
import project.group3tztechcorp.chefitupapp.databinding.ActivityRedeemRewardBinding
import project.group3tztechcorp.chefitupapp.databinding.ActivityRewardsPageBinding
import project.group3tztechcorp.chefitupapp.splash.SplashScreens

class RedeemReward : AppCompatActivity() {

    private lateinit var reference: DatabaseReference
    private lateinit var reference2: DatabaseReference
    private lateinit var rewardRecyclerView: RecyclerView
    private lateinit var rewardsArrayList: ArrayList<Reward>
    lateinit var binding: ActivityRedeemRewardBinding
    lateinit var username: String
    lateinit var fullName: String
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    private var user: UserInformation = UserInformation()
    private var reward: String = "none"
    private var maxReached: Boolean = false

    private final val myPreferences: String = "MyPref"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_redeem_reward)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_redeem_reward)

        var intent = getIntent()
        reward = intent.getStringExtra("reward").toString().trim()

        sharedPreferences = getSharedPreferences(myPreferences, Context.MODE_PRIVATE)
        username = sharedPreferences.getString("username", "").toString()
        fullName = sharedPreferences.getString("fullName", "").toString()

        editor = sharedPreferences.edit()

        rewardRecyclerView = binding.rewardsRecycler
        rewardRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rewardRecyclerView.setHasFixedSize(true)

        rewardsArrayList = arrayListOf<Reward>()

        getRewards()

        binding.backButton.setOnClickListener {
            editor.putString("transition", (1).toString())
            editor.commit()
            var intent: Intent = Intent(this@RedeemReward, RewardsPage::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }

        binding.redeemBtn.setOnClickListener {
            AlertDialog.Builder(this@RedeemReward)
                .setTitle("Redeem?")
                .setMessage("Are you sure you want to redeem your reward?")
                .setPositiveButton("Yes") { dialogInterface, i ->
                    resetRewards()
                    editor.putString("transition", (1).toString())
                    editor.commit()
                    var intent: Intent =
                        Intent(this@RedeemReward, RewardsPage::class.java)
                    intent.putExtra("username", username)
                    startActivity(intent)
                }
                .setNegativeButton("No", null)
                .show()
        }
    }

    fun getRewards(){
        reference = FirebaseDatabase.getInstance().getReference("Rewards")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for(rewardSnapshot in snapshot.children){
                        if(rewardSnapshot.child("name").getValue().toString().equals(reward)) {
                            val reward = rewardSnapshot.getValue(Reward::class.java)
                            rewardsArrayList.add(reward!!)
                        }
                    }
                    rewardRecyclerView.adapter = RewardsAdapter(rewardsArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun resetRewards(){
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

                    user.resetRewards()

                    reference.child(username).child("rewards").setValue(user.rewards)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}