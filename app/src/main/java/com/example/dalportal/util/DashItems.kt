package com.example.dalportal.util

object DashItems {
    operator fun get(role: String?): List<String> {
        return when (role) {
            "TA" -> taItems
            "Professor" -> professorItems
            "admin" -> adminItems
            else -> studentItems
        }
    }

    private val studentItems: List<String> =
        arrayListOf("assignments_submit", "job_portal", "events", "forum")
    private val taItems: List<String> =
        arrayListOf(
            "ta_portal",
            "update_availability",
            "assignments_review",
            "job_portal",
            "content",
            "events",
            "forum"
        )
    private val professorItems: List<String> = arrayListOf(
        "prof_portal",
        "review_availability",
        "assignments_review", "job_portal",
        "content",
        "events",
        "forum"
    )
    private val adminItems: List<String> =
        arrayListOf("admin_portal", "job_portal", "events", "forum")
}