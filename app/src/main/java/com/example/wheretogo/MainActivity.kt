package com.example.wheretogo

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WhereToGoTheme {
                WhereToGoApp();
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WhereToGoApp() {
    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "Bottom app bar",
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
        ) {
            val postViewModel: PostViewModel = viewModel()

           Column (modifier = Modifier.padding(8.dp)) {
               Header()

               Spacer(modifier = Modifier.height(16.dp))
               Row(
                   horizontalArrangement = Arrangement.End,
                   modifier = Modifier.fillMaxWidth().fillMaxHeight()
               ) {
                   PostList(postViewModel.posts, postViewModel)
               }
           }
        }
    }
}


@Composable
fun Header() {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary),
        modifier = Modifier.fillMaxWidth().height(150.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround,
) {
            Text(
                text = "WTG?",
                modifier = Modifier
                    .padding(16.dp),
                textAlign = TextAlign.Center,
            )
            AddButton()
        }
    }
}

@Composable
fun AddButton() {
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
                    modifier = Modifier.padding(16.dp),label = {
                        Text("Description")
                    }

                )
                Button(onClick = {
                    if (postName.isNotBlank() && postOrigin.isNotBlank() && postDestination.isNotBlank() && postDescription.isNotBlank()) {
                        postViewModel.addPost(postName, postOrigin, postDestination, postDescription)
                        postName = ""
                        postOrigin = ""
                        postDestination = ""
                        postDescription = ""
                        shouldShowDialog.value = false
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
            PostListItem(post, postViewModel);
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


@Composable
fun PostListItem( post: Post, postViewModel: PostViewModel){
    Column {
        ElevatedCard (
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),  modifier = Modifier.fillMaxSize()){
            Column (
                verticalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.padding(16.dp)) {
                Row (
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(post.name, fontSize = 12.sp, color = Color.Gray)
                    IconButton(onClick = {
                        postViewModel.removePost(post)
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.baseline_close_24),
                            contentDescription = stringResource(id = R.string.delete),
                            modifier = Modifier.size(16.dp),
                        )
                    }
                }
                Row (
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ){
                    Text(post.origin, modifier = Modifier.weight(1f), textAlign = TextAlign.Start)
                    Image(
                        painter = painterResource(id = R.drawable.baseline_compare_arrows_24),
                        contentDescription = stringResource(id = R.string.reverse),
                        contentScale = ContentScale.Crop,
                    )
                    Text(post.destination, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
                }

                HorizontalDivider(thickness = 1.dp, color = Color.Gray)

                Box(){
                    Text(post.description, textAlign = TextAlign.Start)
                    Spacer(modifier = Modifier.height(32.dp))
                }
                ActionButtonContainer();
            }
        }

    }
}

@Composable
fun ActionButtonContainer() {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(onClick = {}) {
            Image(
                painter = painterResource(id = R.drawable.outline_thumb_up_24),
                contentDescription = stringResource(id = R.string.up_vote),
                modifier = Modifier.size(24.dp)
            )
        }

        IconButton(onClick = {}) {
            Image(
                painter = painterResource(id = R.drawable.outline_comment_24),
                contentDescription = stringResource(id = R.string.comment),
                modifier = Modifier.size(24.dp)
            )
        }
        IconButton(onClick = {}) {
            Image(
                painter = painterResource(id = R.drawable.outline_comment_24),
                contentDescription = stringResource(id = R.string.comment),
                modifier = Modifier.size(24.dp)
            )
        }
        DropdownMenuBtn()
    }
}

    @Composable
    fun DropdownMenuBtn() {
        var expanded by remember { mutableStateOf(false) }
        Box {
            IconButton(onClick = {
                expanded = !expanded
            }) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_more_horiz_24),
                    contentDescription = stringResource(id = R.string.comment),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Copy Link") },
                onClick = { /* Do something... */ }
            )
            DropdownMenuItem(
                text = { Text("Share") },
                onClick = { /* Do something... */ }
            )
        }
}
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WhereToGoTheme {
        WhereToGoApp();
    }
}