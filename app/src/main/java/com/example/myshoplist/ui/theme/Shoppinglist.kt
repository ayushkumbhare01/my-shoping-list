package com.example.myshoplist.ui.theme

 import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
 import androidx.compose.material3.Divider
 import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


data class items(val id: Int, var name:String, var quantity:Double, var price:String, var unit:String, val isedit:Boolean, var check:Boolean )

@Composable
fun Shoppinglistapp(){
    var sitem by remember{ mutableStateOf(listOf<items>()) }
    var showdialog by remember { mutableStateOf(false) }
    var itemname by remember{ mutableStateOf("") }
    var itemquan by remember{ mutableStateOf("") }
    var incom by remember{ mutableStateOf(false) }
    var itemprice by remember { mutableStateOf("") }
    var unitexpand by remember { mutableStateOf(false) }
    var unitinput by remember { mutableStateOf("Unit") }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        Button(onClick = {showdialog=true}, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(text = "Add item")
        }
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)){
            items(sitem){
                    item->
                if(item.isedit){
                    ShoppingItemEditer(item = item, onEditComplete ={editedunit,editedname,editedquan,editedprice->sitem=sitem.map{it.copy(isedit = false)}
                        val editedItem = sitem.find {it.id ==item.id}
                        editedItem?.let {
                            it.name = editedname
                            it.quantity=editedquan.toDouble()
                            it.price=editedprice
                            it.unit=editedunit
                        }})
                }
                else{
                    shoppinglistitem(item =item , onEditClick = {sitem=sitem.map {it.copy(isedit = it.id==item.id)}}, onDelete ={sitem=sitem.filter { it.id != item.id }} )
                }
            }
        }
    }
    val context= LocalContext.current
    if(showdialog){
        AlertDialog(onDismissRequest = {showdialog=false},
            confirmButton = {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp), horizontalArrangement = Arrangement.Center) {
                    Button(onClick = {if(itemname.isNotBlank() && itemquan.isNotBlank() && itemprice.isNotBlank()){
                        var newItem=items(sitem.size + 1, name = itemname, quantity = itemquan.toDouble(), price =itemprice,unit=unitinput ,isedit = false,check=false)
                        var updatedItemList = sitem.toMutableList()
                        var itemExists = false
                        for (j in updatedItemList.indices) {
                            if (updatedItemList[j].name == newItem.name &&
                                updatedItemList[j].unit == newItem.unit &&
                                updatedItemList[j].price == newItem.price
                            ) {
                                updatedItemList[j] = updatedItemList[j].copy(quantity = updatedItemList[j].quantity + newItem.quantity)
                                itemExists = true
                                break
                            }
                        }
                        if (itemExists){Toast.makeText(context,"Item already exists",Toast.LENGTH_LONG).show()}
                        if (!itemExists) {
                            updatedItemList.add(newItem.copy(id = updatedItemList.size + 1))
                        }
                        sitem = updatedItemList
                        showdialog=false
                        itemname=""
                        itemquan=""
                        itemprice=""
                        incom=false
                    }else{incom=true}
                    }) {
                        Text(text = "ADD")
                    }
                    Spacer(modifier = Modifier.width(35.dp))
                    Button(onClick = {showdialog=false
                        incom=false}) {
                        Text(text = "CANCEL")
                    }
                }
            },
            title = {Text(text = "Add Item")},
            text = {
                Column {
                    OutlinedTextField(value =itemname,
                        onValueChange ={itemname=it},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        label = {Text(text ="Item name")}
                    )
                    OutlinedTextField(value = itemquan,
                        onValueChange = {itemquan=it},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        label = {Text(text = "Quantity")}, keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number))
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
                        Button(onClick = {unitexpand=true}, modifier = Modifier.fillMaxWidth(0.9f)) {
                            Text(text = "$unitinput")
                            Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
                        }
                        Spacer(modifier = Modifier.width(20.dp))
                        DropdownMenu(expanded =unitexpand , onDismissRequest = {unitexpand=false},modifier = Modifier.fillMaxWidth(0.75f)) {
                            DropdownMenuItem(
                                text = { Row{
                                    Spacer(modifier = Modifier.width(70.dp))
                                    Text(text = "Kilograms(Kg)")
                                }
                                }, onClick = {unitinput="Kg"
                                    unitexpand=false})
                            DropdownMenuItem(
                                text = { Row{
                                    Spacer(modifier = Modifier.width(70.dp))
                                    Text(text = "Grams(g)")
                                }
                                }, onClick = {unitinput="g"
                                    unitexpand=false})
                            DropdownMenuItem(
                                text = { Row{
                                    Spacer(modifier = Modifier.width(70.dp))
                                    Text(text = "Litre(L)")
                                }
                                }, onClick = {unitinput="L"
                                    unitexpand=false})
                        }
                    }
                    OutlinedTextField(value = itemprice,
                        onValueChange = {itemprice=it},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        label = { Text(text = "Price/Unit")}, keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number))
                    if(incom){
                        Text(text = "Please enter the required fields", modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp), color = Color.Red)
                    }
                }
            })
    }
}

@Composable
fun ShoppingItemEditer(item: items, onEditComplete:(String,String,Double,String)->Unit){
    var editedname by remember { mutableStateOf(item.name) }
    var editedquan by remember { mutableStateOf(item.quantity.toString()) }
    var editedprice by remember { mutableStateOf(item.price) }
    var editedunit by remember { mutableStateOf(item.unit) }
    var unitdrop by remember { mutableStateOf(false) }
    var droplabel by remember { mutableStateOf("Unit") }
Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Divider(color = Color.Red, thickness = 3.dp)
    Row (modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        , horizontalArrangement = Arrangement.SpaceEvenly){
        Column {
            Row {
            TextField(
                value = editedname,
                label = { Text(text ="Item Name") }, onValueChange = { editedname = it },
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .width(130.dp)
            )
                Spacer(modifier = Modifier.width(5.dp))
                TextField(value =editedprice,label = {Text(text ="Price/Unit")}, onValueChange = {editedprice=it}, singleLine = true, modifier = Modifier
                    .wrapContentSize()
                    .width(130.dp))
            }
            Spacer(modifier = Modifier.height(5.dp))
            Row {
            TextField(value =editedquan, label = {Text(text ="Quantity")},onValueChange = {editedquan=it}, singleLine = true, modifier = Modifier
                .wrapContentSize()
                .width(130.dp))
                Spacer(modifier = Modifier.width(5.dp))
                Box(){
                    Button(onClick = {unitdrop=true}, modifier = Modifier.size(height = 50.dp, width = 130.dp)) {
                        Text(text = "${droplabel}")
                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    DropdownMenu(expanded =unitdrop, onDismissRequest = {unitdrop=false},modifier = Modifier.width(130.dp)) {
                        DropdownMenuItem(
                            text = { Row{
                                Text(text = "Kilograms(Kg)")
                            }
                            }, onClick = {editedunit="Kg"
                                droplabel="Kg"
                            unitdrop=false})
                        DropdownMenuItem(
                            text = { Row{
                                Text(text = "Grams(g)")
                            }
                            }, onClick = {editedunit="g"
                                droplabel="g"
                                unitdrop=false})
                        DropdownMenuItem(
                            text = { Row{
                                Text(text = "Litre(L)")
                            }
                            }, onClick = {editedunit="L"
                                droplabel="L"
                                unitdrop=false })
                    }
                }
            }
        }

    }

    Button(onClick = {onEditComplete(editedunit,editedname,editedquan.toDouble(),editedprice)}) {
        Text(text = "Save")
    }
Divider(color = Color.Red, thickness = 3.dp)}
}

@Composable
fun shoppinglistitem(
    item: items,
    onEditClick: () -> Unit,
    onDelete: () -> Unit
) {
    val checkedState = remember { mutableStateOf(item.check) }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checkedState.value,
            onCheckedChange = {
                checkedState.value = it
                item.check = it
            }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .border(
                    border = BorderStroke(2.dp, Color(0xFFFFFFFF)),
                    shape = RoundedCornerShape(5)
                )
                .height(110.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Name: ${item.name}",
                    maxLines = 1,
                    modifier = Modifier.padding(start = 10.dp, top = 10.dp)
                )
                Text(
                    text = "Qty: ${item.quantity}-${item.unit}",
                    maxLines = 1,
                    modifier = Modifier.padding(start = 10.dp)
                )
                Text(
                    text = "Price: ${item.price}",
                    maxLines = 1,
                    modifier = Modifier.padding(start = 10.dp)
                )
                Text(
                    text = "Cost: ${item.price.toInt() * item.quantity}",
                    maxLines = 1,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }
            Column {
                IconButton(
                    onClick = onEditClick,
                    modifier = Modifier.padding(start = 20.dp, end = 2.dp, top = 5.dp)
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.padding(start = 20.dp, bottom = 5.dp, end = 2.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
@Preview
@Composable
fun PreviewShoppingListApp() {
    Shoppinglistapp()
}


