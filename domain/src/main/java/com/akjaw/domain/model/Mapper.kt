package com.akjaw.domain.model

interface Mapper <E, D> {

    fun mapFrom(entity: E): D

    fun mapTo(domain: D): E
}