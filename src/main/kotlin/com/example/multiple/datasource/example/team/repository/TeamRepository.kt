package com.example.multiple.datasource.example.team.repository

import com.example.multiple.datasource.example.team.entity.Team
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TeamRepository : JpaRepository<Team, Long>