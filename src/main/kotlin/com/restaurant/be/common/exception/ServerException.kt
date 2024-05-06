package com.restaurant.be.common.exception

sealed class ServerException(
    val code: Int,
    override val message: String
) : RuntimeException(message)

data class InvalidTokenException(
    override val message: String = "유효하지 않은 토큰입니다."
) : ServerException(401, message)

data class InvalidPasswordException(
    override val message: String = "패스워드가 일치 하지 않습니다."
) : ServerException(401, message)

data class NotFoundUserEmailException(
    override val message: String = "존재 하지 않는 유저 이메일 입니다."
) : ServerException(400, message)

data class WithdrawalUserException(
    override val message: String = "탈퇴한 유저 입니다."
) : ServerException(400, message)

data class DuplicateUserEmailException(
    override val message: String = "이미 존재 하는 이메일 입니다."
) : ServerException(400, message)

data class DuplicateUserNickNameException(
    override val message: String = "이미 존재 하는 닉네임 입니다."
) : ServerException(400, message)

data class SendEmailException(
    override val message: String = "이메일 전송에 실패 했습니다."
) : ServerException(500, message)