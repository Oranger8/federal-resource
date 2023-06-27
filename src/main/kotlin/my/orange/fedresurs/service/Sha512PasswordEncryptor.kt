package my.orange.fedresurs.service

import org.springframework.stereotype.Service
import java.security.MessageDigest


@Service
class Sha512PasswordEncryptor : PasswordEncryptor {
    
    override fun encrypt(password: String): String {
        val messageDigest = MessageDigest.getInstance("SHA-512")
        val bytes = messageDigest.digest(password.toByteArray())
        return buildString { 
            bytes.map { 
                it.mapToString()
            }.forEach { 
                append(it)
            }
        }
    }
    
    private fun Byte.mapToString() = ((toInt() and 0xff) + 0x100).toString(16).substring(1)
}
