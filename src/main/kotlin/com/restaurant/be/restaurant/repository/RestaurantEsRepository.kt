package com.restaurant.be.restaurant.repository

import com.jillesvangurp.ktsearch.SearchClient
import com.jillesvangurp.ktsearch.parseHits
import com.jillesvangurp.ktsearch.search
import com.jillesvangurp.searchdsls.querydsl.ESQuery
import com.jillesvangurp.searchdsls.querydsl.SearchDSL
import com.jillesvangurp.searchdsls.querydsl.bool
import com.jillesvangurp.searchdsls.querydsl.exists
import com.jillesvangurp.searchdsls.querydsl.match
import com.jillesvangurp.searchdsls.querydsl.nested
import com.jillesvangurp.searchdsls.querydsl.range
import com.jillesvangurp.searchdsls.querydsl.terms
import com.restaurant.be.restaurant.presentation.dto.GetRestaurantsRequest
import com.restaurant.be.restaurant.repository.dto.RestaurantEsDocument
import kotlinx.coroutines.runBlocking
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class RestaurantEsRepository(
    private val client: SearchClient
) {

    private val searchIndex = "restaurant"

    fun searchRestaurants(request: GetRestaurantsRequest, pageable: Pageable): List<RestaurantEsDocument> {
        val dsl = SearchDSL()
        val termQueries: MutableList<ESQuery> = mutableListOf()
        if (!request.categories.isNullOrEmpty()) {
            termQueries.add(
                dsl.terms("category", *request.categories.toTypedArray())
            )
        }
        if (request.discountForSkku) {
            termQueries.add(
                dsl.exists("discount_content")
            )
        } else {
            termQueries.add(
                dsl.bool {
                    mustNot(
                        dsl.exists("discount_content")
                    )
                }
            )
        }
        if (request.rating != null) {
            termQueries.add(
                dsl.range("naver_rating") {
                    gte = request.rating
                }
            )
        }
        if (request.reviewCount != null) {
            termQueries.add(
                dsl.range("naver_review_count") {
                    gte = request.reviewCount
                }
            )
        }
        if (request.priceMax != null) {
            termQueries.add(
                dsl.nested {
                    path = "menus"
                    query = dsl.bool {
                        filter(
                            dsl.range("menus.price") {
                                lte = request.priceMax
                            }
                        )
                    }
                }
            )
        }
        if (request.priceMin != null) {
            termQueries.add(
                dsl.nested {
                    path = "menus"
                    query = dsl.bool {
                        filter(
                            dsl.range("menus.price") {
                                gte = request.priceMin
                            }
                        )
                    }
                }
            )
        }

        val result = runBlocking {
            client.search(
                target = searchIndex,
                block = {
                    query = bool {
                        filter(
                            termQueries
                        )
                        if (!request.query.isNullOrEmpty()) {
                            should(
                                match("name", request.query) {
                                    boost = 0.1
                                },
                                match("category", request.query) {
                                    boost = 0.03
                                },
                                match("original_category", request.query) {
                                    boost = 0.03
                                },
                                nested {
                                    path = "menus"
                                    query = bool {
                                        should(
                                            match("menus.menu_name", request.query) {
                                                boost = 0.01
                                            },
                                            match("menus.description", request.query) {
                                                boost = 0.01
                                            }
                                        )
                                    }
                                }
                            )
                            minimumShouldMatch(1)
                        }
                    }
                },
                size = pageable.pageSize,
                from = pageable.offset.toInt()
            ).parseHits<RestaurantEsDocument>()
        }

        return result
    }
}