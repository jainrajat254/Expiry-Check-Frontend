package com.example.expirycheck.screens

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.expirycheck.R
import com.example.expirycheck.customs.CustomButton
import com.example.expirycheck.customs.CustomTextButton
import com.example.expirycheck.customs.CustomTextField
import com.example.expirycheck.models.LoginRequest
import com.example.expirycheck.navigation.Routes
import com.example.expirycheck.repository.PreferencesRepository
import com.example.expirycheck.viewmodel.AuthViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavController,
    vm: AuthViewModel = viewModel(),
    sharedPreferencesManager: PreferencesRepository,
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }

    val isUserLoggedIn = sharedPreferencesManager.isUserLoggedIn()

    val context = LocalContext.current
    val activity = LocalActivity.current as ComponentActivity

    val isLoading = vm.isLoading
    val loginResponse = vm.loginResponse
    val authError = vm.authError

    // Handle back press to finish activity
    BackHandler {
        activity.finish()
    }

    if (isUserLoggedIn) {
        LaunchedEffect(Unit) {
            navController.navigate(Routes.Home.routes) {
                popUpTo(0) { inclusive = true }
            }
        }
        return // Exit the function early
    }

    // If login is successful, save user and navigate
    LaunchedEffect(loginResponse) {
        loginResponse?.let { userResponse ->
            CoroutineScope(Dispatchers.IO).launch {
                sharedPreferencesManager.saveUser(userResponse)
            }
            navController.navigate(Routes.Home.routes) {
                popUpTo(0) { inclusive = true }
            }
            vm.clearState()
        }
    }

    // Show auth error as toast
    LaunchedEffect(authError) {
        authError?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            vm.clearState()
        }
    }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "App Logo",
                    modifier = Modifier.size(80.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Welcome to Expiry Check",
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Login to continue",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(24.dp))

                CustomTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = "Username",
                    placeholder = "Enter your username",
                    leadingIcon = {
                        Icon(
                            painterResource(id = R.drawable.baseline_alternate_email_24),
                            contentDescription = "Username Icon"
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                CustomTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Password",
                    placeholder = "Enter your password",
                    leadingIcon = {
                        Icon(Icons.Filled.Lock, contentDescription = "Password Icon")
                    },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                painter = painterResource(
                                    id = if (passwordVisible) R.drawable.visibilityon else R.drawable.visibilityoff
                                ),
                                contentDescription = "Toggle Password Visibility"
                            )
                        }
                    },
                    keyboardType = KeyboardType.Password,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = rememberMe, onCheckedChange = { rememberMe = it })
                        Text("Remember me", style = MaterialTheme.typography.bodyMedium)
                    }
                    CustomTextButton(
                        clickableText = "Forgot Password",
                        onClick = { /* Handle forgot password */ }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                CustomButton(
                    text = if (isLoading) "Logging in..." else "LOGIN",
                    enabled = !isLoading,
                    onClick = {
                        when {
                            username.isBlank() -> {
                                Toast.makeText(context, "Username cannot be empty", Toast.LENGTH_SHORT).show()
                            }

                            password.isBlank() -> {
                                Toast.makeText(context, "Password cannot be empty", Toast.LENGTH_SHORT).show()
                            }

                            else -> {
                                val user = LoginRequest(username.trim(), password.trim())
                                vm.login(user)
                            }
                        }
                    },
                    icon = {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = "Login Icon"
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))

                CustomTextButton(
                    normalText = "Not have an account?",
                    clickableText = " Register",
                    onClick = { navController.navigate(Routes.Register.routes) }
                )
            }
        }
    }
}
