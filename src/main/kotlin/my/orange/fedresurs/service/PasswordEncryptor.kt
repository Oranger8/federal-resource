package my.orange.fedresurs.service

interface PasswordEncryptor {
    
    fun encrypt(password: String): String
}
