package my.orange.fedresurs.domain

data class AuthRequest(val login: String, val passwordHash: String)

data class AuthResponse(val jwt: String)