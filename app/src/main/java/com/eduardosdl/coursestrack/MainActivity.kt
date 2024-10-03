package com.eduardosdl.coursestrack

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.eduardosdl.coursestrack.databinding.ActivityMainBinding
import com.eduardosdl.coursestrack.ui.dialogs.DeleteDialog
import com.eduardosdl.coursestrack.util.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.navHost.id) as NavHostFragment
        navController = navHostFragment.navController

        binding.navView.setupWithNavController(navController)

        val appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.logout_btn -> {
                    viewModel.logout {
                        navController.navigate(R.id.loginFragment)
                    }
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.delete_account_btn -> {
                    DeleteDialog("sua conta permanentemente ?") {
                        viewModel.deleteUser { state ->
                            when (state) {
                                is UiState.Loading -> {}
                                is UiState.Success -> {
                                    navController.navigate(R.id.loginFragment)
                                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                                }

                                is UiState.Failure -> {
                                    Toast.makeText(this, state.error, Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }.show(supportFragmentManager, "deleteDialog")
                    true
                }

                else -> {
                    NavigationUI.onNavDestinationSelected(menuItem, navController)
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment -> binding.toolbar.visibility = View.GONE
                R.id.registerFragment -> binding.toolbar.visibility = View.GONE
                else -> binding.toolbar.visibility = View.VISIBLE
            }
        }
    }

    fun updateToolbarTitle(title: String) {
        binding.toolbar.title = title
    }
}