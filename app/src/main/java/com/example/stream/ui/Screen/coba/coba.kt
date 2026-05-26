package com.example.stream.ui.Screen.coba

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import com.example.stream.ui.Screen.components.ButtonBar

@Composable
fun CobaScreen() {
    var selectedIndex by remember { mutableStateOf(3) }

    Scaffold(
        bottomBar = {
            ButtonBar(
                selectedIndex = selectedIndex,
                onItemSelected = { selectedIndex = it }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Ini Halaman Coba Screen", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Item Bottom Bar terpilih: $selectedIndex")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CobaScreenPreview() {
    CobaScreen()
}
