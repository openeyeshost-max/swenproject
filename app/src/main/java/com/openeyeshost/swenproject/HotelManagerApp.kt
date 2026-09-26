package com.openeyeshost.swenproject

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun HotelManagerApp() {
    val navController = rememberNavController()
    val viewModel: HotelViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = { role ->
                    viewModel.setRole(role)
                    navController.navigate("dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("dashboard") {
            DashboardScreen(
                uiState = uiState,
                onNavigate = { destination -> navController.navigate(destination) },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("dashboard") { inclusive = true }
                    }
                }
            )
        }

        composable("orders") { OrdersScreen(uiState = uiState, viewModel = viewModel) }
        composable("kitchen") { KitchenScreen(uiState = uiState, viewModel = viewModel) }
        composable("billing") { BillingScreen(uiState = uiState, viewModel = viewModel) }
        composable("rooms") { RoomsScreen(uiState = uiState, viewModel = viewModel) }
        composable("menu") { MenuScreen(uiState = uiState, viewModel = viewModel) }
        composable("staff") { StaffScreen(uiState = uiState) }
        composable("admin") { AdminDashboardScreen(uiState = uiState) }
        composable("settings") { SettingsScreen(uiState = uiState, viewModel = viewModel) }
        composable("profile") { ProfileScreen(uiState = uiState) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: HotelViewModel,
    onLoginSuccess: (StaffRole) -> Unit
) {
    var staffId by remember { mutableStateOf("ADM-001") }
    var pin by remember { mutableStateOf("1234") }
    var error by remember { mutableStateOf("") }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "[Hotel Name] Manager",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Hotel + Restaurant Operations Center")
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = staffId,
                onValueChange = { staffId = it },
                label = { Text("Staff ID") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = pin,
                onValueChange = { pin = it },
                label = { Text("PIN") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (error.isNotEmpty()) {
                Text(error, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(8.dp))
            }
            Button(
                onClick = {
                    val valid = viewModel.login(staffId, pin)
                    if (valid) {
                        error = ""
                        onLoginSuccess(viewModel.uiState.value.currentRole)
                    } else {
                        error = "Invalid Staff ID or PIN"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
                onClick = { onLoginSuccess(StaffRole.WAITER) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Quick Login as Waiter")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    uiState: AppUiState,
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${uiState.currentRole.label} Dashboard") },
                actions = {
                    Button(onClick = onLogout) {
                        Icon(Icons.Default.ExitToApp, contentDescription = null)
                        Spacer(modifier = Modifier.padding(start = 4.dp))
                        Text("Logout")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard(title = "Today Orders", value = "${uiState.orders.size}")
                    StatCard(title = "Revenue", value = "₹${uiState.orders.sumOf { order -> order.items.sumOf { it.price * it.qty } }}/day")
                }
            }

            item {
                Text("Quick Actions", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }

            item {
                val actions = listOf(
                    "orders" to "New Order",
                    "kitchen" to "Kitchen Board",
                    "billing" to "Billing",
                    "rooms" to "Room Management",
                    "menu" to "Menu",
                    "staff" to "Staff",
                    "admin" to "Admin Dashboard",
                    "settings" to "Settings",
                    "profile" to "Profile"
                )

                actions.chunked(2).forEach { pair ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        pair.forEach { (route, label) ->
                            Card(
                                modifier = Modifier.weight(1f),
                                onClick = { onNavigate(route) },
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(Icons.Default.Search, contentDescription = null)
                                    Text(label)
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            item {
                Text("Live Activity Feed", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }

            items(uiState.logs.take(5)) { log ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(log.staffName, fontWeight = FontWeight.Bold)
                        Text(log.action)
                        Text(log.time, color = MaterialTheme.colorScheme.secondary)
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(title: String, value: String) {
    Card(
        modifier = Modifier.weight(1f),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(title, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, fontWeight = FontWeight.Bold, fontSize = 24.sp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(uiState: AppUiState, viewModel: HotelViewModel) {
    Scaffold(topBar = { TopAppBar(title = { Text("Orders Board") }) }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {}) { Text("New") }
                OutlinedButton(onClick = {}) { Text("Running") }
                OutlinedButton(onClick = {}) { Text("Billed") }
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(uiState.orders) { order ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("${order.type.uppercase()} ${order.number}", fontWeight = FontWeight.Bold)
                                Text(order.status)
                            }
                            Divider(modifier = Modifier.padding(vertical = 8.dp))
                            Text("Waiter: ${order.waiterName}")
                            order.items.forEach {
                                Text("- ${it.name} x${it.qty} @ ₹${it.price}")
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedButton(onClick = { viewModel.updateOrderStatus(order.id, "Preparing") }) { Text("Preparing") }
                                OutlinedButton(onClick = { viewModel.updateOrderStatus(order.id, "Ready") }) { Text("Ready") }
                                Button(onClick = { viewModel.updateOrderStatus(order.id, "Billed") }) { Text("Billed") }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KitchenScreen(uiState: AppUiState, viewModel: HotelViewModel) {
    Scaffold(topBar = { TopAppBar(title = { Text("Kitchen Orders") }) }) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(uiState.orders.filter { it.status != "Billed" }) { order ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("${order.type} ${order.number}", fontWeight = FontWeight.Bold)
                        Text("Status: ${order.status}")
                        order.items.forEach {
                            Text("- ${it.name} x${it.qty}")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.updateOrderStatus(order.id, "Ready") }) {
                            Text("Mark Ready")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillingScreen(uiState: AppUiState, viewModel: HotelViewModel) {
    Scaffold(topBar = { TopAppBar(title = { Text("Billing") }) }) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(uiState.orders.filter { it.status == "Ready" || it.status == "Billed" }) { order ->
                val subtotal = order.items.sumOf { it.price * it.qty.toDouble() }
                val tax = subtotal * (uiState.hotelSettings.taxPercent / 100.0)
                val service = subtotal * (uiState.hotelSettings.serviceChargePercent / 100.0)
                val total = subtotal + tax + service

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Invoice for ${order.number}", fontWeight = FontWeight.Bold)
                        Text("Status: ${order.status}")
                        Text("Gross: ₹${subtotal}")
                        Text("GST: ₹${tax}")
                        Text("Service: ₹${service}")
                        Text("Total: ₹${total}", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(onClick = { viewModel.updateOrderStatus(order.id, "Billed") }) { Text("Generate PDF") }
                            OutlinedButton(onClick = {}) { Text("WhatsApp") }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomsScreen(uiState: AppUiState, viewModel: HotelViewModel) {
    Scaffold(topBar = { TopAppBar(title = { Text("Room Management") }) }) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(uiState.rooms) { room ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Room ${room.number}", fontWeight = FontWeight.Bold)
                        Text("Type: ${room.type} | Rate: ₹${room.ratePerNight}/night")
                        Text("Status: ${room.status}")
                        Text("Guest: ${room.guestName}")
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(onClick = { viewModel.checkInRoom(room.number, "Guest ${room.number}") }) { Text("Check-in") }
                            OutlinedButton(onClick = { viewModel.checkOutRoom(room.number) }) { Text("Check-out") }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(uiState: AppUiState, viewModel: HotelViewModel) {
    Scaffold(topBar = { TopAppBar(title = { Text("Menu Management") }) }) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(uiState.menuItems) { item ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(item.name, fontWeight = FontWeight.Bold)
                        Text("Category: ${item.category}")
                        Text("Price: ₹${item.price}")
                        Text("Available: ${if (item.isAvailable) "Yes" else "No"}")
                    }
                }
            }

            item {
                Button(
                    onClick = {
                        viewModel.addMenuItem(MenuItem("m-new", "Chef Special", "Chef's Pick", 520.0, true))
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add Sample Item")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaffScreen(uiState: AppUiState) {
    Scaffold(topBar = { TopAppBar(title = { Text("Staff Management") }) }) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(uiState.staffList) { staff ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(staff.name, fontWeight = FontWeight.Bold)
                        Text("Role: ${staff.role.label}")
                        Text("Phone: ${staff.phone}")
                        Text("Status: ${if (staff.isActive) "Active" else "Inactive"}")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(uiState: AppUiState) {
    Scaffold(topBar = { TopAppBar(title = { Text("Admin Dashboard") }) }) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard(title = "Total Orders", value = "${uiState.orders.size}")
                    StatCard(title = "Today's Revenue", value = "₹${uiState.orders.sumOf { order -> order.items.sumOf { it.price * it.qty } }}")
                }
            }
            item {
                Text("Top Selling Items", fontWeight = FontWeight.Bold)
                Text("Paneer Tikka • Veg Biryani • Cold Coffee")
            }
            items(uiState.logs.take(8)) { log ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(log.staffName, fontWeight = FontWeight.Bold)
                        Text(log.action)
                        Text(log.time)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(uiState: AppUiState, viewModel: HotelViewModel) {
    var hotelName by remember { mutableStateOf(uiState.hotelSettings.name) }
    var address by remember { mutableStateOf(uiState.hotelSettings.address) }
    var gstin by remember { mutableStateOf(uiState.hotelSettings.gstin) }
    var tax by remember { mutableStateOf(uiState.hotelSettings.taxPercent.toString()) }
    var service by remember { mutableStateOf(uiState.hotelSettings.serviceChargePercent.toString()) }

    Scaffold(topBar = { TopAppBar(title = { Text("Hotel Settings") }) }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(value = hotelName, onValueChange = { hotelName = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Hotel Name") })
            OutlinedTextField(value = address, onValueChange = { address = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Address") })
            OutlinedTextField(value = gstin, onValueChange = { gstin = it }, modifier = Modifier.fillMaxWidth(), label = { Text("GSTIN") })
            OutlinedTextField(value = tax, onValueChange = { tax = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Tax Percent") })
            OutlinedTextField(value = service, onValueChange = { service = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Service Charge %") })
            Button(
                onClick = {
                    viewModel.saveHotelSettings(
                        HotelSettings(
                            name = hotelName,
                            address = address,
                            gstin = gstin,
                            taxPercent = tax.toDoubleOrNull() ?: 5.0,
                            serviceChargePercent = service.toDoubleOrNull() ?: 10.0
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Settings")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(uiState: AppUiState) {
    Scaffold(topBar = { TopAppBar(title = { Text("Profile") }) }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(uiState.selectedStaff.name, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                    Text(uiState.selectedStaff.role.label)
                    Text("Phone: ${uiState.selectedStaff.phone}")
                    Text("Staff ID: ${uiState.selectedStaff.id}")
                }
            }
            Button(onClick = {}) { Text("Update Profile") }
            OutlinedButton(onClick = {}) { Text("Logout") }
        }
    }
}

@Composable
fun StatusBadge(status: String) {
    Box(
        modifier = Modifier
            .background(
                when (status) {
                    "Ready" -> MaterialTheme.colorScheme.primaryContainer
                    "Preparing" -> MaterialTheme.colorScheme.tertiaryContainer
                    "Billed" -> MaterialTheme.colorScheme.secondaryContainer
                    else -> MaterialTheme.colorScheme.surfaceVariant
                },
                shape = RoundedCornerShape(999.dp)
            )
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(status, style = MaterialTheme.typography.labelMedium)
    }
}
