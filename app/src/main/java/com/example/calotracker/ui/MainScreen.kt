package com.example.calotracker.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.calotracker.R
import com.example.calotracker.ui.theme.CalotrackerTheme
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.statusBarsPadding

@Composable
fun MainScreen(navController: NavHostController) {
    Scaffold(
        containerColor = Color.White // Sfondo completamente bianco
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            WelcomeTextContent(onStartClick = {
                navController.navigate("home")
            })
        }
    }
}

@Composable
fun WelcomeTextContent(onStartClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically)
    ) {
        // Cambia qui il logo se hai un nome diverso (es: R.drawable.mio_logo)
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo CaloTracker",
            modifier = Modifier
                .height(150.dp)
                .padding(top = 16.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
            elevation = CardDefaults.cardElevation(6.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Benvenuto su CaloTracker",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Tieni traccia della tua alimentazione\nraggiungendo i tuoi obiettivi di benessere!",
                    style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 22.sp),
                    textAlign = TextAlign.Center
                )
            }
        }

        Button(
            onClick = onStartClick,
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            elevation = ButtonDefaults.buttonElevation(10.dp),
            modifier = Modifier
                .height(56.dp)
                .width(220.dp)
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Avvia",
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Vai al Diario",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    CalotrackerTheme {
        MainScreen(navController = rememberNavController())
    }
}
