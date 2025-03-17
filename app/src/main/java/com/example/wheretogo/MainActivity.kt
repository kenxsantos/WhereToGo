package com.example.wheretogo

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wheretogo.ui.theme.WhereToGoTheme
import kotlinx.coroutines.CoroutineScope

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val snackbarHostState = remember { SnackbarHostState() }
            val scope = rememberCoroutineScope()

            WhereToGoTheme {
                Scaffold(
                    snackbarHost = {
                    SnackbarHost(snackbarHostState) },
                    modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    Column {
                        val postViewModel: PostViewModel = viewModel()

                        Header(scope, snackbarHostState);

                        Spacer(modifier = Modifier.height(16.dp))
                        Row (
                           horizontalArrangement = Arrangement.End,
                           modifier = Modifier.fillMaxWidth().fillMaxHeight()
                           ){
                           PostList(postViewModel.posts, postViewModel)
                       }
                    }
                }
            }
        }
    }
}


@Composable
fun Header(scope: CoroutineScope, snackbarHostState: SnackbarHostState) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary),
        modifier = Modifier.fillMaxWidth().height(150.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxSize()) {
            Text(
                text = "WTG?",
                modifier = Modifier
                    .padding(16.dp),
                textAlign = TextAlign.Center,
            )
            AddButton(scope, snackbarHostState)
        }
    }
}

@Composable
fun AddButton(scope: CoroutineScope, snackbarHostState: SnackbarHostState) {
    val shouldShowDialog = rememberSaveable { mutableStateOf(false) } // 1

    if (shouldShowDialog.value) {
        AddPostDialog(shouldShowDialog = shouldShowDialog);
    }

    IconButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = { shouldShowDialog.value = true },
        ) {
            Icon(Icons.Filled.Add, "Add")
    }
}

@Composable
fun AddPostDialog(
    postViewModel: PostViewModel = viewModel(),
    shouldShowDialog: MutableState<Boolean>
) {
    Dialog(
        onDismissRequest = { // 4
        shouldShowDialog.value = false
    },) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            var postName by remember { mutableStateOf("") }
            var postOrigin by remember { mutableStateOf("") }
            var postDestination by remember { mutableStateOf("") }
            var postDescription by remember { mutableStateOf("") }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(16.dp)) {
                Text("ADD POST", fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = postName,
                    onValueChange = { postName = it },
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    label = {
                        Text("Name")
                    }
                )

                TextField(
                    value = postOrigin,
                    onValueChange = { postOrigin = it },
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    label = {
                        Text("Origin")
                    }
                )
                TextField(
                    value = postDestination,
                    onValueChange = { postDestination = it },
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    label = {
                        Text("Destination")
                    }
                )
                TextField(
                    value = postDescription,
                    onValueChange = { postDescription = it },
                    modifier = Modifier.padding(16.dp),
                )
                Button(onClick = {
                    if (postOrigin.isNotBlank() && postDestination.isNotBlank()) {
                        postViewModel.addPost(postName, postOrigin, postDestination, postDescription)
                        postName = ""
                        postOrigin = ""
                        postDestination = ""
                        postDescription = ""
                    }
                }) {
                    Text("Add")
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun PostList(posts: List<Post>, postViewModel: PostViewModel) {
    val state = rememberScrollState()
    LaunchedEffect(Unit) { state.animateScrollTo(100) }

    Column (modifier = Modifier.verticalScroll(state)){
        for (post in posts) {
            Column ( modifier = Modifier.fillMaxWidth().height(80.dp)) {
                Card (
                    modifier = Modifier.fillMaxSize(),
                    elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)) {
                    Column (modifier = Modifier.padding(16.dp)) {
                        Text(post.name, fontSize = 12.sp, color = Color.Gray)
                        Row (
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ){
                            Text(post.origin, modifier = Modifier.weight(1f), textAlign = TextAlign.Start)
                            Image(
                                painter = painterResource(id = R.drawable.reverse),
                                contentDescription = stringResource(id = R.string.reverse),
                                contentScale = ContentScale.Fit,
                            )
                            Text(post.destination, modifier = Modifier.weight(1f), textAlign = TextAlign.End)

                        }
                    }
                }

            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    WhereToGoTheme {
        Scaffold(
            snackbarHost = {
                SnackbarHost(snackbarHostState) },
            modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            Column (
                modifier = Modifier.fillMaxWidth().fillMaxHeight()
                    .border(2.dp, Color.Magenta, shape = RectangleShape)

            ) {
                val postViewModel: PostViewModel = viewModel()
                Header(scope, snackbarHostState);
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth().height(100.dp)
                ){
                    PostList(postViewModel.posts, postViewModel)
                }
            }
        }
    }
}