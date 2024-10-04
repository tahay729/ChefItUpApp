package project.group3tztechcorp.chefitupapp.ui

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.google.firebase.database.*
import project.group3tztechcorp.chefitupapp.*
import project.group3tztechcorp.chefitupapp.R
import project.group3tztechcorp.chefitupapp.databinding.FragmentProfileBinding
import project.group3tztechcorp.chefitupapp.splash.SplashScreens

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentProfileBinding
    lateinit var user: UserInformation
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        getData()

        binding.accountBtn.setOnClickListener { view: View ->
            //view.findNavController().navigate(R.id.action_profileFragment_to_accountInformationFragment)
            val intent = Intent(context, SplashScreens::class.java)
            intent.putExtra("fullName", "name")
            intent.putExtra("username", "username")
            intent.putExtra("savedDate", "date")
            intent.putExtra("splash", 2)

            startActivity(intent)
        }

        binding.rewardsGainedBtn.setOnClickListener{
            startActivity(Intent(context, RewardsPage::class.java))
        }

        binding.recipesCompletedBtn.setOnClickListener {
            startActivity(Intent(context, RecipesCompleted::class.java))
        }

        binding.achievementsBtn.setOnClickListener {

            val intent = Intent(context, SplashScreens::class.java)
            intent.putExtra("fullName", "name")
            intent.putExtra("username", "username")
            intent.putExtra("savedDate", "date")
            intent.putExtra("splash", 3)

            startActivity(intent)
        }



        return binding.root
        //return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun getData() {

        var fullname = arguments?.getString("fullName").toString().trim()
        var username = arguments?.getString("userName").toString().trim()

        var reference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("userInformation")

        var checkUser: Query = reference.orderByChild("username").equalTo(username)

        checkUser.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    //get data from database
                    var nameFromDB = snapshot.child(username).child("fullName").getValue()
                    var levelFromDB = snapshot.child(username).child("level").getValue()
                    var experienceFromDB = snapshot.child(username).child("experience").getValue()
                    var rewardsNumFromDB = snapshot.child(username).child("rewards").getValue()
                    var recipiesNumFromDB =
                        snapshot.child(username).child("recipesCompleted").getValue()
                    var achivementsNumFromDB =
                        snapshot.child(username).child("achievementsCompleted").getValue()

                    user = UserInformation(
                        username,
                        fullname,
                        levelFromDB.toString().toInt(),
                        experienceFromDB.toString().toInt(),
                        rewardsNumFromDB.toString().toInt(),
                        recipiesNumFromDB.toString().toInt(),
                        achivementsNumFromDB.toString().toInt()
                    )

                    //set the data
                    binding.userName.text = username
                    binding.fullName.text = nameFromDB.toString()
                    binding.rewardsNum.text = rewardsNumFromDB.toString()
                    binding.recipesCompletedNum.text = recipiesNumFromDB.toString()
                    binding.achievementsNum.text = achivementsNumFromDB.toString()
                    binding.level.text = "Level: " + levelFromDB.toString()
                    binding.experienceUser.text = "Experience: "

                    if(rewardsNumFromDB.toString().toInt() == 3){
                        binding.rewardsNum.setTextColor(resources.getColor(R.color.gold))
                        binding.rewardsDesc.setTextColor(resources.getColor(R.color.gold))
                    }

                    if (levelFromDB.toString().toInt() == 1) {
                        min = 0
                        max = 300
                        percent = experienceFromDB.toString().toInt() - min
                        binding.experienceBar.max = max - min

                        startAnimationr(0, percent)
                    } else if (levelFromDB.toString().toInt() == 2) {
                        min = 301
                        max = 701
                        percent = experienceFromDB.toString().toInt() - min
                        binding.experienceBar.max = max - min

                        startAnimationr(0, percent)
                    } else if (levelFromDB.toString().toInt() == 3) {
                        min = 701
                        max = 1301
                        percent = experienceFromDB.toString().toInt() - min
                        binding.experienceBar.max = max - min

                        startAnimationr(0, percent)
                    } else if (levelFromDB.toString().toInt() == 4) {
                        min = 1301
                        max = 2101
                        percent = experienceFromDB.toString().toInt() - min
                        binding.experienceBar.max = max - min

                        startAnimationr(0, percent)
                    } else if (levelFromDB.toString().toInt() == 5) {
                        min = 2101
                        max = 2101
                        percent = 0
                        binding.experienceUser.text = "Max Level Reached"
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun startAnimationr(start: Int, end: Int) {
        var animator: ValueAnimator = ValueAnimator.ofInt(start, end)
        animator.startDelay = 500
        animator.setDuration(2000)
        animator.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
            override fun onAnimationUpdate(animation: ValueAnimator?) {
                binding.experienceUserNumber.setText(
                    animation!!.getAnimatedValue().toString() + "" + "/" + (max - min)
                )
                binding.experienceBar.setProgress(animation.getAnimatedValue().toString().toInt())
            }

        })
        animator.start()

    }
}