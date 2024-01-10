package com.example.dalportal

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.dalportal.databinding.ActivityMainBinding
import com.example.dalportal.util.UserData
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var adminPortalMenuItem: MenuItem
    private lateinit var assignmentMenuItem: MenuItem
    private lateinit var availabilityTa: MenuItem
    private lateinit var ratingAdmin: MenuItem
    private lateinit var ratingUser: MenuItem
    private lateinit var availabilityProf: MenuItem
    private lateinit var assignmentReviewMenuItem: MenuItem
    private lateinit var contentMenuItem: MenuItem
    private lateinit var taPortalMenuItem: MenuItem
    private lateinit var professorPortalMenuItem: MenuItem
    private lateinit var jobPortalMenuItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)
        val navView: NavigationView = binding.navView
        val headerView = navView.getHeaderView(0)
        val currentEmailTextView = headerView.findViewById<TextView>(R.id.current_email)
        val currentNameTextView = headerView.findViewById<TextView>(R.id.current_user)
        val currentRoleTextView = headerView.findViewById<TextView>(R.id.current_role)

        //Nav items
        adminPortalMenuItem = navView.menu.findItem(R.id.nav_admin_portal)
        assignmentMenuItem = navView.menu.findItem(R.id.nav_assignment)
        availabilityTa = navView.menu.findItem(R.id.nav_availability_calendar)
        ratingAdmin = navView.menu.findItem(R.id.nav_rating_admin)
        ratingUser = navView.menu.findItem(R.id.nav_rating)
        availabilityProf = navView.menu.findItem(R.id.nav_availability_calendar_prof)
        assignmentReviewMenuItem = navView.menu.findItem(R.id.nav_assignment_review)
        contentMenuItem = navView.menu.findItem(R.id.nav_content)
        taPortalMenuItem = navView.menu.findItem(R.id.nav_ta_portal)
        professorPortalMenuItem = navView.menu.findItem(R.id.nav_professor_portal)
        jobPortalMenuItem = navView.menu.findItem(R.id.nav_jobListing)
        hideNavItems()

        // Set the text to user's email
        currentEmailTextView.text = UserData.email ?: "John@gmail.com"
        currentNameTextView.text = UserData.name ?: "John Doe"
        currentRoleTextView.text = UserData.role ?: "Professor"
        val drawerLayout: DrawerLayout = binding.drawerLayout

        val navController = findNavController(R.id.nav_host_fragment_content_main)


        // Set up NavigationItemSelectedListener
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_feedback_form -> {
                    // Navigate to FeedbackFragment
                    navController.navigate(R.id.nav_feedback_form)
                    drawerLayout.closeDrawers()
                    true
                }
                // Handle other menu items...
                else -> false
            }
        }
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(

                R.id.nav_dashboard,
                R.id.nav_ta_portal,
                R.id.nav_slideshow,
                R.id.nav_availability_calendar,
                R.id.nav_professor_portal,
                R.id.nav_event,
                R.id.nav_availability_calendar_prof,
                R.id.nav_rating,
                R.id.nav_rating_admin,
                R.id.nav_feedback_form,
                R.id.nav_jobListing,
                R.id.nav_content,
                R.id.nav_assignment_review,
                R.id.nav_discussion
            ), drawerLayout
        )

        val logoutMenuItem = navView.menu.findItem(R.id.logout)
        logoutMenuItem.setOnMenuItemClickListener {
            logoutUser()
            true
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun logoutUser() {
        // Clear user data
        UserData.clear()

        // Redirect to LoginActivity
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Close the current activity
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun hideNavItems() {
        assignmentReviewMenuItem.isVisible = false
        contentMenuItem.isVisible = false
        availabilityTa.isVisible = false
        taPortalMenuItem.isVisible = false
        availabilityProf.isVisible = false
        professorPortalMenuItem.isVisible = false
        assignmentMenuItem.isVisible = false
        adminPortalMenuItem.isVisible = false
        ratingAdmin.isVisible = false
        ratingUser.isVisible = false
        jobPortalMenuItem.isVisible = true

        when (UserData.role) {
            "TA" -> {
                assignmentReviewMenuItem.isVisible = true
                contentMenuItem.isVisible = true
                availabilityTa.isVisible = true
                taPortalMenuItem.isVisible = true
                ratingUser.isVisible = true
            }

            "Professor" -> {
                availabilityProf.isVisible = true
                professorPortalMenuItem.isVisible = true
                assignmentReviewMenuItem.isVisible = true
                contentMenuItem.isVisible = true
                ratingUser.isVisible = true
            }

            "Student" -> {
                assignmentMenuItem.isVisible = true
                ratingUser.isVisible = true
            }

            "admin" -> {
                adminPortalMenuItem.isVisible = true
                ratingAdmin.isVisible = true
            }
        }
    }

    override fun onBackPressed() {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        when (navController.currentDestination?.id) {
            R.id.nav_assignment,
            R.id.nav_event,
            R.id.nav_discussion,
            R.id.nav_ta_portal,
            R.id.nav_availability_calendar,
            R.id.nav_assignment_review,
            R.id.nav_content,
            R.id.nav_professor_portal,
            R.id.nav_availability_calendar_prof,
            R.id.nav_admin_portal,
            R.id.nav_jobListing,
            R.id.nav_dashboard -> navController.navigate(R.id.nav_dashboard)

            else -> super.onBackPressed()
        }

    }
}