package com.example.expirycheck.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
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
import androidx.navigation.compose.rememberNavController
import com.example.expirycheck.R
import com.example.expirycheck.customs.CustomButton
import com.example.expirycheck.customs.CustomTextButton
import com.example.expirycheck.customs.CustomTextField
import com.example.expirycheck.models.RegisterRequest
import com.example.expirycheck.navigation.Routes
import com.example.expirycheck.repository.PreferencesRepository
import com.example.expirycheck.viewmodel.AuthViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    navController: NavController = rememberNavController(),
    vm: AuthViewModel = viewModel(),
    sharedPreferencesManager: PreferencesRepository,
) {
    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val isLoading = vm.isLoading
    val loginResponse = vm.loginResponse
    val authError = vm.authError

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

    LaunchedEffect(authError) {
        authError?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            vm.clearState()
        }
    }

    Scaffold(
        content = { paddingValues ->
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
                        text = "Register",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )


                    Spacer(modifier = Modifier.height(24.dp))

                    CustomTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = "Full Name",
                        placeholder = "Full Name",
                        leadingIcon = {
                            Icon(Icons.Filled.Person, contentDescription = "Full Name Icon")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

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

                    CustomButton(
                        text = if (isLoading) "Registering..." else "Register",
                        enabled = !isLoading,
                        onClick = {
                            when {
                                name.isBlank() -> {
                                    Toast.makeText(
                                        context,
                                        "Name cannot be empty",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                username.isBlank() -> {
                                    Toast.makeText(
                                        context,
                                        "Username cannot be empty",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                password.isBlank() -> {
                                    Toast.makeText(
                                        context,
                                        "Password cannot be empty",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                else -> {
                                    val registerRequest = RegisterRequest(
                                        fullName = name.trim(),
                                        username = username.trim(),
                                        password = password.trim()
                                    )
                                    vm.register(registerRequest)
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
                                    Icons.Filled.PlayArrow,
                                    contentDescription = "Register Icon"
                                )
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    CustomTextButton(
                        normalText = "Already have an account?",
                        clickableText = " Login",
                        onClick = { navController.navigate(Routes.Login.routes) }
                    )
                }
            }
        }
    )
}
