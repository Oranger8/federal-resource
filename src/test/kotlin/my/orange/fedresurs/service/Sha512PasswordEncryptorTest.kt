package my.orange.fedresurs.service

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Sha512PasswordEncryptorTest {

    @Test
    fun encrypt() {
        val encryptor = Sha512PasswordEncryptor()
        val password = "Ax!761BN"
        val expectedHash = "d76628160e1642fcb5e855a02ba7674ff8d4aeab52a43c69ad20cc9cf936b3db4f926e4800ad28b4b70054bfad476516aeff5bb8cf0a331b19e1c532113f3bd8" 
        
        assertEquals(expectedHash, encryptor.encrypt(password))
    }
}