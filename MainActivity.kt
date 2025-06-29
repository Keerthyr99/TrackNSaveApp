package com.tracknsave.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.tracknsave.app.data.AppDatabase
import com.tracknsave.app.data.Transaction
import com.tracknsave.app.data.TransactionDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrackNSaveApp()
        }
    }
}

@Composable
fun TrackNSaveApp() {
    val context = androidx.compose.ui.platform.LocalContext.current
    val db = Room.databaseBuilder(context, AppDatabase::class.java, "tracknsave-db").build()
    val dao = db.transactionDao()
    val transactionsFlow: Flow<List<Transaction>> = dao.getAll()
    val transactions by transactionsFlow.collectAsState(initial = emptyList())

    MaterialTheme {
        Scaffold(topBar = { TopAppBar(title = { Text("TrackNSave") }) }) { padding ->
            Column(Modifier.padding(padding).padding(16.dp)) {
                Button(onClick = { /* TODO: Add transaction dialog */ }) {
                    Text("+ Add Transaction")
                }
                LazyColumn {
                    items(transactions) { txn ->
                        Text("\${txn.type} - ₹\${txn.amount} | \${txn.category} on \${txn.date}")
                    }
                }
            }
        }
    }
}