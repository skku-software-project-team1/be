package com.restaurant.be.restaurant.presentation.controller

import com.restaurant.be.common.response.CommonResponse
import com.restaurant.be.restaurant.presentation.dto.GetRestaurantRequest
import com.restaurant.be.restaurant.presentation.dto.GetRestaurantResponse
import com.restaurant.be.restaurant.presentation.dto.GetRestaurantsResponse
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@Api(tags = ["02. Restaurant Info"], description = "음식점 서비스")
@RestController
@RequestMapping("/api/v1/restaurants")
class GetRestaurantController {

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "음식점 조회 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공",
        content = [Content(schema = Schema(implementation = GetRestaurantsResponse::class))]
    )
    fun getRestaurants(
        principal: Principal,
        @ModelAttribute request: GetRestaurantRequest,
        pageable: Pageable
    ): CommonResponse<GetRestaurantsResponse> {
        return CommonResponse.success()
    }

    @GetMapping("/{restaurantId}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "음식점 상세 조회 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공",
        content = [Content(schema = Schema(implementation = GetRestaurantResponse::class))]
    )
    fun getRestaurant(
        principal: Principal,
        @PathVariable restaurantId: String
    ): CommonResponse<GetRestaurantResponse> {
        return CommonResponse.success()
    }
}