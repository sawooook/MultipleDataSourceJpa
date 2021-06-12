package com.example.multiple.datasource.example.user.repository

import com.example.multiple.datasource.example.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, Long>