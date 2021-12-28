package com.example.testing_kotlin_samples.tradeapp.mypage

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.testing_kotlin_samples.R
import com.example.testing_kotlin_samples.databinding.FragmentMypageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MyPageFragment : Fragment(R.layout.fragment_mypage) {
    private lateinit var binding: FragmentMypageBinding

    //    private var email:String? = null;
//    private var password:String? = null
    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMypageBinding.bind(view)

        binding.signOutButton.setOnClickListener {
            activity?.let {
                binding?.let { binding ->
                    val email = binding.emailET.text.toString()
                    val password = binding.passwordET.text.toString()

                    if (auth.currentUser == null) {
                        auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(requireActivity()) { task ->
                                if (task.isSuccessful) {
                                    successSignIn()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "로그인에 실패했습니다. 이메일 도는 비밀번호를 확인해주세요",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            }

                    } else {
                        auth.signOut()
                        binding.emailET.text.clear()
                        binding.emailET.isEnabled = true
                        binding.passwordET.text.clear()
                        binding.passwordET.isEnabled = true

                        binding.signOutButton.text = "로그인"
                        binding.signOutButton.isEnabled = false
                        binding.signUpButton.isEnabled = false
                    }
                }
            }


        }

        binding.signUpButton.setOnClickListener {
            binding?.let { binding->
                val email = binding.emailET.text.toString()
                val password = binding.passwordET.text.toString()

                auth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(requireActivity()) { task->
                        if(task.isSuccessful){
                            Toast.makeText(context,"회원가입에 성공했습니다. 로그인 버튼을 눌러주세요.",Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(context,"회원가입에 실패했습니다.",Toast.LENGTH_SHORT).show()
                        }
                    }

            }
        }

        binding.emailET.setOnClickListener {
            binding?.let {
                val enable =
                    binding.emailET.text.isNotEmpty() && binding.passwordET.text.isNotEmpty()
                binding.signOutButton.isEnabled = enable
                binding.signUpButton.isEnabled = enable
            }
        }
        binding.passwordET.setOnClickListener {
            binding?.let {
                val enable =
                    binding.emailET.text.isNotEmpty() && binding.passwordET.text.isNotEmpty()
                binding.signOutButton.isEnabled = enable
                binding.signUpButton.isEnabled = enable
            }
        }


    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser == null) {
            binding?.let { binding ->
                binding.emailET.text.clear()
                binding.emailET.isEnabled = true
                binding.passwordET.text.clear()
                binding.passwordET.isEnabled = true

                binding.signOutButton.text = "로그인"
                binding.signOutButton.isEnabled = false
                binding.signUpButton.isEnabled = false
            }
        } else {
            binding?.let { binding ->
                binding.emailET.setText(auth.currentUser!!.email)
                binding.emailET.isEnabled = false
                binding.passwordET.setText("**********")
                binding.passwordET.isEnabled = false

                binding.signOutButton.text = "로그아웃"
                binding.signOutButton.isEnabled = true
                binding.signUpButton.isEnabled = false
            }
        }
    }

    private fun successSignIn() {
        if (auth.currentUser == null) {
            Toast.makeText(context, "로그인에 실패했습니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show()
            return
        }

        binding?.emailET?.isEnabled = false
        binding?.passwordET?.isEnabled = false
        binding?.signUpButton?.isEnabled = false
        binding?.signOutButton?.text = "로그아웃"
    }
}