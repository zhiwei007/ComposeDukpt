package com.gordon
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dukpt.Dukpt
import com.dukpt.Util

@Composable
fun topBar(){
    Box (modifier = Modifier.height(150.dp).fillMaxWidth() ){
        Column(modifier = Modifier.fillMaxWidth(),horizontalAlignment = Alignment.CenterHorizontally ) {
            Divider(color = Color.LightGray)
            Text("Derive Keys", color = Color.Black, fontSize = 20.sp, textAlign = TextAlign.End,modifier = Modifier.padding(20.dp))
            Divider(  color = Color.LightGray)
        }
    }
}



@Composable
fun rowCell(label:String,  valueStr: MutableState<String>){
    Row (modifier = Modifier.padding(10.dp).fillMaxWidth(fraction = 0.5f), verticalAlignment = Alignment.CenterVertically) {
        Text("$label:", color = Color.Black,  textAlign = TextAlign.Center)
        TextField(value = valueStr.value, onValueChange = {
            valueStr.value = it
        },
            placeholder = { Text("$label ..." ,color = Color.Black,  textAlign = TextAlign.Center)  },
            trailingIcon = {
                IconButton(onClick = {
                    valueStr.value = ""
                }){
                    Icon(
                        Icons.Sharp.Delete, contentDescription = "Favorite",
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                }
            }

        )
    }
}

@Composable
fun genKeys(
            ipek: MutableState<String>
            ,ksn: MutableState<String>
            ,pek: MutableState<String>
            ,mek: MutableState<String>
){
    val ipek_hex = ipek.value
    val ksn_hex = ksn.value
    val ipekBytes =  Util.hex2byte(ipek_hex)
    val ksnBytes =  Util.hex2byte(ksn_hex)
//                    val ipek = Util.hex2byte("E69BA53691E7C240AD122D3D4582E8D9")//Fill the string into the TextField
//                    val ksn = Util.hex2byte("00000007172700000001")
    val keyRegister = Util.hex2byte("C0C0C0C000000000C0C0C0C000000000")
    val keyRegisterBitmask = Dukpt.toBitSet(keyRegister)

    val data = Util.hex2byte("00000000000000FF00000000000000FF")
    val dataVariantBitmask = Dukpt.toBitSet(data)

    val mac = Util.hex2byte("000000000000FF00000000000000FF00")
    val macVariantBitmask = Dukpt.toBitSet(mac)

    try {
        val PEK = Dukpt._getCurrentKey(
            Dukpt.toBitSet(ipekBytes),
            Dukpt.toBitSet(ksnBytes),
            keyRegisterBitmask,
            dataVariantBitmask
        )
        println("PEK:" + Util.hexString(Dukpt.toByteArray(PEK)))
        pek.value =  Util.hexString(Dukpt.toByteArray(PEK))


        val MEK = Dukpt._getCurrentKey(
            Dukpt.toBitSet(ipekBytes),
            Dukpt.toBitSet(ksnBytes),
            keyRegisterBitmask,
            macVariantBitmask
        )
        println("MEK:" + Util.hexString(Dukpt.toByteArray(MEK)))
        mek.value =  Util.hexString(Dukpt.toByteArray(MEK))
    } catch (e: Exception) {
        println(e)
        Snackbar {
              Text("Gen keys cause error: $e")
        }
    }
}

@Composable
fun content(){
    var ipek  =  remember { mutableStateOf("") }
    var ksn  =  remember { mutableStateOf("") }
    var pek  =  remember { mutableStateOf("") }
    var mek  =  remember { mutableStateOf("") }
    Box(modifier = Modifier.fillMaxSize().fillMaxHeight()){
        Column(modifier = Modifier.align(alignment = Alignment.Center)) {
            rowCell("IPEK",ipek)
            rowCell("KSN",ksn)
            rowCell("PEK",pek)
            rowCell("MEK",mek)
            Button(modifier = Modifier.width(330.dp).height(80.dp).padding(start = 50.dp, top = 20.dp),
                onClick = {
                    genKeys(ipek, ksn, pek, mek)
                }){
                Text("Derive Keys")
            }
        }
    }
}
