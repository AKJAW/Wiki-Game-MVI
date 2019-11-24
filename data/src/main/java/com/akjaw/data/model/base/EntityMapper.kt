package com.akjaw.data.model.base

interface EntityMapper <E, D> {

    fun mapFromEntity(entity: E): D

    fun mapToEntity(domain: D): E
}