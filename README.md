package com.openeyeshost.swenproject

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onLoginSuccess: (StaffRole) -> Unit) {
    var staffId by remember { mutableStateOf("") }
    var pin by remember { mutableStateOf("") }

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
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    if (staffId.isNotBlank() && pin.isNotBlank()) {
                        onLoginSuccess(StaffRole.ADMIN)
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
    role: StaffRole,
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${role.label} Dashboard") },
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
                    StatCard(title = "Today Orders", value = "128")
                    StatCard(title = "Revenue", value = "₹48.2K")
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
                                    Icon(Icons.Default.ReceiptLong, contentDescription = null)
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

            items(MockData.logs) { log ->
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
fun OrdersScreen() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Orders Board") })
        }
    ) { innerPadding ->
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
                items(MockData.orders) { order ->
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
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KitchenScreen() {
    Scaffold(topBar = { TopAppBar(title = { Text("Kitchen Orders") }) }) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(MockData.orders.filter { it.status != "Billed" }) { order ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("${order.type} ${order.number}", fontWeight = FontWeight.Bold)
                        Text("Status: ${order.status}")
                        order.items.forEach {
                            Text("- ${it.name} x${it.qty}")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = {}) {
                            Text("Generate KOT")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillingScreen() {
    Scaffold(topBar = { TopAppBar(title = { Text("Billing") }) }) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(MockData.orders.filter { it.status == "Ready" || it.status == "Billed" }) { order ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Invoice for ${order.number}", fontWeight = FontWeight.Bold)
                        Text("Table/Room: ${order.number}")
                        Text("Guest Total: ₹${order.items.sumOf { it.price * it.qty.toDouble() }}")
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(onClick = {}) { Text("Create PDF") }
                            OutlinedButton(onClick = {}) { Text("Share WhatsApp") }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomsScreen() {
    Scaffold(topBar = { TopAppBar(title = { Text("Room Management") }) }) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(MockData.rooms) { room ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Room ${room.number}", fontWeight = FontWeight.Bold)
                        Text("Type: ${room.type} | Rate: ₹${room.ratePerNight}/night")
                        Text("Status: ${room.status}")
                        Text("Guest: ${room.guestName}")
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(onClick = {}) { Text("Check-in") }
                            OutlinedButton(onClick = {}) { Text("Check-out") }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen() {
    Scaffold(topBar = { TopAppBar(title = { Text("Menu Management") }) }) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(MockData.menuItems) { item ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(item.name, fontWeight = FontWeight.Bold)
                        Text("Category: ${item.category}")
                        Text("Price: ₹${item.price}")
                        Text("Available: ${if (item.isAvailable) "Yes" else "No"}")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaffScreen() {
    Scaffold(topBar = { TopAppBar(title = { Text("Staff Management") }) }) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(MockData.staffList) { staff ->
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
fun AdminDashboardScreen() {
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
                    StatCard(title = "Total Orders", value = "128")
                    StatCard(title = "Revenue", value = "₹48.2K")
                }
            }
            item {
                Text("Top Selling Items", fontWeight = FontWeight.Bold)
                Text("Paneer Tikka • Veg Biryani • Cold Coffee")
            }
            items(MockData.logs) { log ->
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
fun SettingsScreen() {
    Scaffold(topBar = { TopAppBar(title = { Text("Hotel Settings") }) }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(value = MockData.hotelSettings.name, onValueChange = {}, modifier = Modifier.fillMaxWidth(), label = { Text("Hotel Name") })
            OutlinedTextField(value = MockData.hotelSettings.address, onValueChange = {}, modifier = Modifier.fillMaxWidth(), label = { Text("Address") })
            OutlinedTextField(value = MockData.hotelSettings.gstin, onValueChange = {}, modifier = Modifier.fillMaxWidth(), label = { Text("GSTIN") })
            OutlinedTextField(value = "5%", onValueChange = {}, modifier = Modifier.fillMaxWidth(), label = { Text("Tax Percent") })
            OutlinedTextField(value = "10%", onValueChange = {}, modifier = Modifier.fillMaxWidth(), label = { Text("Service Charge %") })
            Button(onClick = {}) { Text("Save Settings") }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
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
                    Text("Priya Sharma", fontWeight = FontWeight.Bold, fontSize = 22.sp)
                    Text("Admin")
                    Text("Phone: +91 98765 12345")
                    Text("Staff ID: ADM-001")
                }
            }
            Button(onClick = {}) { Text("Update Profile") }
            OutlinedButton(onClick = {}) { Text("Logout") }
        }
    }
}
