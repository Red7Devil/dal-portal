package com.example.dalportal.ui.event_calendar

import java.util.*

data class Event(val name: String, val date: String, val monthYear: String)

object EventData {
    val events: MutableList<Event> = mutableListOf()

    init {
        // Add events with specific information
        addEvent("University closed in lieu of New Year's Day", "January 2, 2023", "January 2023")
        addEvent("Residences open - traditional and non-traditional", "January 8, 2023", "January 2023")
        addEvent("Classes begin, winter term", "January 9, 2023", "January 2023")
        addEvent("Fees due for winter term", "January 20, 2023", "January 2023")
        addEvent("Last day to add winter term courses", "January 20, 2023", "January 2023")
        // ... add more events ...

        // February 2023 events
        addEvent("April Exam Schedule is Posted", "February 1, 2023", "February 2023")
        addEvent("Last day to drop multi-term courses with a 'W'", "February 2, 2023", "February 2023")
        addEvent("Munro Day - University closed", "February 3, 2023", "February 2023")
        addEvent("Last day to drop winter term course without a 'W'", "February 6, 2023", "February 2023")
        // ... add more events ...

        // March 2023 events
        addEvent("2023/24 Academic Calendar available for viewing", "March 1, 2023", "March 2023")
        addEvent("Fall/Winter 2023/24 timetable available", "March 2, 2023", "March 2023")
        addEvent("Advising Week", "March 2 - 9, 2023", "March 2023")
        addEvent("Last day to drop winter term classes with a 'W'", "March 13, 2023", "March 2023")
        addEvent("Registration opens for Graduate students, Faculties of Health, Engineering and Agriculture starting at 7:30am", "March 20, 2023", "March 2023")
        // ... add more events ...

        // April 2023 events
        addEvent("Seat reservations removal from most fall/winter courses", "April 4, 2023", "April 2023")
        addEvent("Good Friday - university closed", "April 7, 2023", "April 2023")
        addEvent("Monday April 10 - Friday classes will be held", "April 10, 2023", "April 2023")
        addEvent("Classes end, Winter and Multi-Term", "April 11, 2023", "April 2023")
        // ... add more events ...

        // May - August 2023 events
        addEvent("Summer term begins", "May 1, 2023", "May 2023")
        addEvent("Grades due for courses with formal exams", "May 2, 2023", "May 2023")
        addEvent("Senate awards degrees/diplomas. Transcripts updated.", "May 16, 2023", "May 2023")
        addEvent("Victoria Day, University closed", "May 22, 2023", "May 2023")
        // ... add more events ...

        // June - August 2023 events
        addEvent("Registration opens for third/fourth year and part-time Law/Combined", "June 7, 2023", "June 2023")
        addEvent("Fall 2023/Winter 2024 registration opens for first-year students", "June 10, 2023", "June 2023")
        addEvent("Summer Break for Full-Term Courses (except students in Co-op, Clinicals, or Internships)", "June 12 - 16, 2023", "June 2023")
        addEvent("Canada Day, University closed", "July 1, 2023", "July 2023")
        // ... add more events ...

        // September 2023 events
        addEvent("Labour Day - University closed", "September 4, 2023", "September 2023")
        addEvent("Opening Ceremony - Halifax Campus", "September 2, 2023", "September 2023")
        addEvent("Classes begin, fall term", "September 5, 2023", "September 2023")
        addEvent("Residence fees due for fall term", "September 15, 2023", "September 2023")
        // ... add more events ...

        // October 2023 events
        addEvent("December Exam Schedule posted", "October 1, 2023", "October 2023")
        addEvent("National Day for Truth and Reconciliation, University closed", "October 2, 2023", "October 2023")
        addEvent("Fall Convocation", "October 3 & 4, 2023", "October 2023")
        addEvent("Last day to change fall term courses from audit to credit (and vice versa)", "October 4, 2023", "October 2023")
        // ... add more events ...

        // November 2023 events
        addEvent("Last day to drop Fall term courses with a 'W'", "November 2, 2023", "November 2023")
        addEvent("Fall Study Break (except students in co-op, clinics or internships)", "November 13-17, 2023", "November 2023")
        addEvent("In lieu of Remembrance Day, University closed", "November 13, 2023", "November 2023")
        // ... add more events ...

        // December 2023 events
        addEvent("Last day to Apply to Graduate for Spring 2024 Convocation via Dal Online", "December 1, 2023", "December 2023")
        addEvent("Account Detail (tuition & fees) for winter term available via Dal Online", "December 1 (approximately)", "December 2023")
        addEvent("Tuesday, December 5 - Monday classes will be held", "December 5, 2023", "December 2023")
        addEvent("Classes end, fall term", "December 6, 2023", "December 2023")
        // ... add more events ...

        // January 2024 events
        addEvent("New Year's Day, University Closed", "January 1, 2024", "January 2024")
        addEvent("Residences open - traditional and non-traditional", "January 7, 2024", "January 2024")
        addEvent("Classes begin, winter term", "January 8, 2024", "January 2024")
        addEvent("Fees due for winter term", "January 22, 2024", "January 2024")
        // ... add more events ...

        // February 2024 events
        addEvent("April Exam Schedule is Posted", "February TBD, 2024", "February 2024")
        addEvent("Munro Day - University closed", "February 2, 2024", "February 2024")
        addEvent("Last day to drop multi-term courses with a 'W'", "February 6, 2024", "February 2024")
        addEvent("Last day to drop winter term course without a 'W'", "February 6, 2024", "February 2024")
        // ... add more events ...

        // March 2024 events
        addEvent("2023/24 Academic Calendar available for viewing", "March TBD, 2024", "March 2024")
        addEvent("Fall/Winter 2023/24 timetable available", "March TBD, 2024", "March 2024")
        addEvent("Advising Week", "March TBD, 2024", "March 2024")
        addEvent("Last day to drop winter term classes with a 'W'", "March 6, 2024", "March 2024")
        // ... add more events ...

        // April 2024 events
        addEvent("Seat reservations removal from most fall/winter courses", "April TBD, 2024", "April 2024")
        addEvent("Monday April 8 - Friday classes will be held", "April 8, 2024", "April 2024")
        addEvent("Classes end, Winter and Multi-Term", "April 9, 2024", "April 2024")
        addEvent("Break before exams", "April 10, 2024", "April 2024")
        // ... add more events ...

        // May - August 2024 events
        addEvent("Grades due for courses with formal exams", "May 2, 2024", "May 2024")
        addEvent("Summer term begins", "May 6, 2024", "May 2024")
        addEvent("Senate awards degrees/diplomas. Transcripts updated.", "May TBD, 2024", "May 2024")
        addEvent("Victoria Day, University closed", "May 20, 2024", "May 2024")
        // ... add more events ...

        // June - August 2024 events
        addEvent("Registration opens for third/fourth year and part-time Law/Combined", "June TBD, 2024", "June 2024")
        addEvent("Registration opens for second year and transfer Law", "June TBD, 2024", "June 2024")
        addEvent("Fall 2023/Winter 2024 registration opens for first-year students", "June TBD, 2024", "June 2024")
        addEvent("Summer Break for Full-Term Courses (except students in Co-op, Clinicals, or Internships)", "June 17 - 21, 2024", "June 2024")
        // ... add more events ...
    }

    private fun addEvent(name: String, date: String, monthYear: String) {
        events.add(Event(name, date, monthYear))
    }
}
