@file:Suppress("ktlint", "MatchingDeclarationName")

package com.restaurant.be.user.presentation.dto

import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Pattern

data class UpdatePasswordRequest(
    @field:NotEmpty(message = "이메일은 필수 값 입니다.")
    @field:Email(message = "유효하지 않는 이메일 입니다.")
    @ApiModelProperty(value = "이메일", example = "test@test.com", required = true)
    val email: String,
    @field:NotEmpty(message = "비밀번호는 필수 값 입니다.")
    @field:Pattern(
        regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[\\W_])[A-Za-z\\d\\W_]{8,16}\$",
        message = "영소문자, 영대문자, 숫자, 특수문자가 포함된 8~16자 비밀번호"
    )
    @ApiModelProperty(value = "비밀번호", example = "Test1234!", required = true)
    val password: String,
    @field:NotEmpty(message = "변경 토큰은 필수 값 입니다.")
    @ApiModelProperty(
        value = "변경 토큰",
        example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVIjoxNjIzNTEyNzAwfQ.7J1"
    )
    val token: String
)
