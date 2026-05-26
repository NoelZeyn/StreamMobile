package com.example.stream.ui.Screen.AnggotaKeluarga

//import androidx.compose.material3.*
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.stream.Data.Local.UserPreferences
import com.example.stream.Data.Model.Request.PosisiKeluarga
import com.example.stream.ui.Screen.components.HeaderBackground
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AnggotaKeluargaScreen(
    navController: NavController,
    viewModel: AnggotaKeluargaViewModel,
) {
    var posisiKeluarga by remember { mutableStateOf<PosisiKeluarga?>(null) }

    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    val token by UserPreferences.getToken(context).collectAsState(initial = "")

    val login = token.isNullOrBlank() || token == "null"

    HeaderBackground{}
    RegisterContent(
        posisiKeluarga = posisiKeluarga,
        onPosisiKeluagaChange = { posisiKeluarga = it },
        onNext = {
            coroutineScope.launch {
                println("PosisiKeluarga: $posisiKeluarga")
                if (posisiKeluarga != null) {
                    viewModel.savePosisiKeluarga(posisiKeluarga!!)
                    delay(100)
                    navController.navigate("Completed-Anggota")
                } else {
                    println("Posisi keluarga belum dipilih")
                }
            }
        },

        onNextMore = {
//            Log.d("TOKEN_CHECK", "Token saat ini: '$token'")

            if (token.isNullOrBlank() || token == "null") {
//                Log.d("TOKEN_CHECK", "Token kosong, arahkan ke Login")
                navController.navigate("Login")
            } else {
//                Log.d("TOKEN_CHECK", "Token ada, arahkan ke profil-anggota")
                navController.navigate("profil-anggota")
            }
        },
        login = login
    )
}

@Composable
fun RegisterContent(
    posisiKeluarga: PosisiKeluarga?,
    onPosisiKeluagaChange: (PosisiKeluarga) -> Unit,
    onNext: () -> Unit = {},
    onNextMore: () -> Unit = {},
    login: Boolean
) {
    AnggotaKeluargaHeader()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 170.dp)
    ) {
        // content
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                val options = listOf(PosisiKeluarga.ISTRI, PosisiKeluarga.ANAK)

                Column {
                    options.forEach { posisi ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable { onPosisiKeluagaChange(posisi) }
                        ) {
                            RadioButton(
                                selected = posisi == posisiKeluarga,
                                onClick = { onPosisiKeluagaChange(posisi) }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = posisi.value)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = onNextMore,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF005F6B)
                        ),
                        border = BorderStroke(1.dp, Color(0xFF005F6B))
                    ) {
                        if (login) {
                            androidx.compose.material3.Text("Lewati")
                        } else {
                            androidx.compose.material3.Text("Selesai")
                        }
                    }


                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = onNext,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF005F6B),
                            contentColor = Color.White
                        )
                    ) {
                        androidx.compose.material3.Text("Tambah")
                    }
                }
            }
        }
    }
}


@Composable
fun AnggotaKeluargaHeader() {
    HeaderBackground {
        Text(
            text = "Data Keluarga",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Silakan lengkapi data anggota keluarga yang akan terdaftar dalam layanan Posyandu. Penambahan data juga dapat dilakukan di lain waktu.",
            fontSize = 14.sp,
            color = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterPreview() {
    RegisterContent(
        posisiKeluarga = null,
        onPosisiKeluagaChange = {},
        onNext = {},
        onNextMore = {},
        login = false
    )
}
