package com.akjaw.base

interface EntityMapper <E, D> {

    fun mapFromEntity(entity: E): D

    fun mapToEntity(domain: D): E
}