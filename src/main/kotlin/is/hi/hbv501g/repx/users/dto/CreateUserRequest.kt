package `is`.hi.hbv501g.repx.users.dto

data class CreateUserRequest(
    val email: String,
    val password: String,
    val displayName: String
)
