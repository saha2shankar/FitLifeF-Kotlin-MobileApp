package np.com.harishankarsah.fitlife.ui.utils
object Validation {

    fun validateEmail(email: String): String? {
        return when {
            email.isEmpty() -> null
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                "Invalid email address"
            else -> null
        }
    }

    fun validatePassword(password: String): String? {
        return when {
            password.isEmpty() -> null
            password.length < 8 ->
                "Password must be at least 8 characters"
            !password.any { it.isUpperCase() } ->
                "Must contain at least one uppercase letter"
            !password.any { it.isDigit() } ->
                "Must contain at least one number"
            !password.any { !it.isLetterOrDigit() } ->
                "Must contain at least one special character"
            else -> null
        }
    }

    fun validateName(name: String): String? {
        return when {
            name.isEmpty() -> null
            name.length < 3 -> "Name must be at least 3 characters"
            else -> null
        }
    }

    fun validatePhone(phone: String): String? {
        return when {
            phone.isEmpty() -> null
            phone.length < 10 -> "Invalid phone number"
            else -> null
        }
    }
}
