package np.com.harishankarsah.fitlife.viewmodel

import android.os.Message
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModel: ViewModel() {
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private  val _authState = MutableLiveData<AuthState>()
    var authState: LiveData<AuthState> = _authState
    init {
        checkAuthStatus()
    }

    fun checkAuthStatus(){
        if(auth.currentUser ==null){
            _authState.value = AuthState.Unauthenticated
         }else{
             _authState.value = AuthState.Authenticated
        }
    }
    fun login(email:String, password: String){
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    _authState.value = AuthState.Authenticated
                } else{
                    _authState.value = AuthState.Error(task.exception?.message?:"Something went wrong")
                }
            }
    }

    fun register(name: String, email: String, mobile: String, password: String) {
        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid
                    if (uid != null) {
                        val data = hashMapOf(
                            "uid" to uid,
                            "name" to name,
                            "email" to email,
                            "mobile" to mobile,
                            "createdAt" to System.currentTimeMillis()
                        )
                        FirebaseFirestore.getInstance()
                            .collection("users")
                            .document(uid)
                            .set(data)
                            .addOnSuccessListener {
                                _authState.value = AuthState.Authenticated
                            }
                            .addOnFailureListener { e ->
                                _authState.value = AuthState.Error(e.message ?: "Failed to save user data")
                            }
                    } else {
                        _authState.value = AuthState.Error("User ID not found")
                    }
                } else {
                    _authState.value = AuthState.Error(task.exception?.message?:"Something went wrong")
                }
            }
    }

    fun logout() {
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }

    fun resetPassword(email: String) {
        _authState.value = AuthState.Loading
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Unauthenticated
                } else {
                    _authState.value = AuthState.Error(task.exception?.message?:"Something went wrong")
                }
            }
    }
}
sealed class AuthState{
    object  Authenticated: AuthState()
    object  Unauthenticated : AuthState()
    object  Loading: AuthState()
    data class Error(val message: String): AuthState()
}
