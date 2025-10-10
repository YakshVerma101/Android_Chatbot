package com.example.writter

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.writter.ui.theme.BackgroundColorMessage
import com.example.writter.ui.theme.ColorModelMessage
import com.example.writter.ui.theme.ColorUserMessage

@Composable
fun ChatPage(
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel,
    textToSpeech: TextToSpeech
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = BackgroundColorMessage)
    ) {
        AppHeader()
        MessageList(modifier = Modifier.weight(1f), messageList = viewModel.messageList, textToSpeech = textToSpeech)
        MessageInput(onMessageSend = {
            viewModel.sendMessage(it)
        })
    }
}

@Composable
fun MessageList(
    modifier: Modifier = Modifier,
    messageList: List<MessageModel>,
    textToSpeech: TextToSpeech
) {
    if (messageList.isEmpty()) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier.size(60.dp),
                painter = painterResource(id = R.drawable.baseline_question_answer_24),
                contentDescription = "Icon",
                tint = ColorModelMessage
            )
            Text(text = "Ask me anything", fontSize = 22.sp, color = Color.White)
        }
    }
    LazyColumn(
        modifier = modifier,
        reverseLayout = true
    ) {
        items(messageList.reversed()) {
            MessageRow(messageModel = it, textToSpeech = textToSpeech)
        }
    }
}

@Composable
fun MessageRow(
    messageModel: MessageModel,
    textToSpeech: TextToSpeech
) {
    val isModel = messageModel.role == "model"
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .align(if (isModel) Alignment.BottomStart else Alignment.BottomEnd)
                    .padding(
                        start = if (isModel) 8.dp else 70.dp,
                        end = if (isModel) 70.dp else 8.dp,
                        top = 8.dp,
                        bottom = 8.dp
                    )
                    .clip(RoundedCornerShape(48f))
                    .background(if (isModel) ColorModelMessage else ColorUserMessage)
                    .padding(16.dp)
            ) {
                SelectionContainer {
                    Text(
                        text = messageModel.message,
                        fontWeight = FontWeight.W500,
                        color = Color.White
                    )
                }
            }
            if (isModel) {
                IconButton(
                    onClick = {
                        textToSpeech.speak(messageModel.message, TextToSpeech.QUEUE_FLUSH, null, null)
                    },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_speaker),
                        contentDescription = "Speak",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun MessageInput(onMessageSend: (String) -> Unit) {
    var message by remember { mutableStateOf("") }
    val context = LocalContext.current
    val activity = context as ComponentActivity

    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier.weight(1f),
            value = message,
            onValueChange = { message = it },
            textStyle = androidx.compose.ui.text.TextStyle(color = Color.White),
            placeholder = { Text(text = "Type a message...", color = Color.Gray) }
        )
        IconButton(onClick = {
            if (message.isNotEmpty()) {
                onMessageSend(message)
                message = ""
            }
        }) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Send",
                tint = Color.White
            )
        }
        IconButton(onClick = {
            startVoiceRecognition(activity) { recognizedText ->
                message = recognizedText
            }
        }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_microphone),
                contentDescription = "Voice Input",
                tint = Color.White
            )
        }
    }
}

fun startVoiceRecognition(
    activity: ComponentActivity,
    onResult: (String) -> Unit
) {
    val speechRecognizer = android.speech.SpeechRecognizer.createSpeechRecognizer(activity)
    val intent = android.content.Intent(android.speech.RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(android.speech.RecognizerIntent.EXTRA_LANGUAGE_MODEL, android.speech.RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(android.speech.RecognizerIntent.EXTRA_LANGUAGE, java.util.Locale.getDefault())
    }

    speechRecognizer.setRecognitionListener(object : android.speech.RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {}
        override fun onBeginningOfSpeech() {}
        override fun onRmsChanged(rmsdB: Float) {}
        override fun onBufferReceived(buffer: ByteArray?) {}
        override fun onEndOfSpeech() {}
        override fun onError(error: Int) {
            onResult("")
        }

        override fun onResults(results: Bundle?) {
            val matches = results?.getStringArrayList(android.speech.SpeechRecognizer.RESULTS_RECOGNITION)
            if (!matches.isNullOrEmpty()) {
                onResult(matches[0])
            } else {
                onResult("")
            }
        }

        override fun onPartialResults(partialResults: Bundle?) {}
        override fun onEvent(eventType: Int, params: Bundle?) {}
    })

    speechRecognizer.startListening(intent)
}

@Composable
fun AppHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF767676))
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = "Writter",
            color = Color.White,
            fontSize = 22.sp
        )
    }
}