package com.leoarmelin.connecta.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leoarmelin.connecta.ui.theme.ConnectaTheme
import com.leoarmelin.connecta.ui.theme.Typography
import androidx.compose.material3.MaterialTheme.colorScheme as mtc

enum class WordPillState {
    NORMAL,
    SELECTED,
    RIGHT,
    WRONG,
    FINISHED
}

@Composable
fun WordPill(
    modifier: Modifier = Modifier,
    wordValue: String,
    state: WordPillState = WordPillState.NORMAL,
    onClick: () -> Unit = {}
) {
    val mainColor by animateColorAsState(
        targetValue = when (state) {
            WordPillState.SELECTED -> mtc.primary
            WordPillState.RIGHT -> mtc.secondary
            WordPillState.WRONG -> Color(0xFFFF3333)
            WordPillState.FINISHED -> mtc.onBackground.copy(alpha = 0.5f)
            else -> mtc.onBackground
        },
        label = "mainColor"
    )

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(32.dp))
            .clickable { onClick() }
            .border(3.dp, mainColor, RoundedCornerShape(32.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = wordValue,
            style = Typography.labelMedium,
            color = mainColor,
        )
    }
}

@Preview
@Composable
fun PreviewWordPill() {
    ConnectaTheme {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(mtc.background)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    WordPill(
                        wordValue = "AOBA",
                        state = WordPillState.NORMAL,
                    )
                    WordPill(
                        wordValue = "AOBA",
                        state = WordPillState.SELECTED,
                    )
                    WordPill(
                        wordValue = "AOBA",
                        state = WordPillState.RIGHT,
                    )
                    WordPill(
                        wordValue = "AOBA",
                        state = WordPillState.WRONG,
                    )
                    WordPill(
                        wordValue = "AOBA",
                        state = WordPillState.FINISHED,
                    )
                }
                ConnectaTheme(useDarkTheme = true) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.background(mtc.background)
                    ) {
                        WordPill(
                            wordValue = "AOBA",
                            state = WordPillState.NORMAL,
                        )
                        WordPill(
                            wordValue = "AOBA",
                            state = WordPillState.SELECTED,
                        )
                        WordPill(
                            wordValue = "AOBA",
                            state = WordPillState.RIGHT,
                        )
                        WordPill(
                            wordValue = "AOBA",
                            state = WordPillState.WRONG,
                        )
                        WordPill(
                            wordValue = "AOBA",
                            state = WordPillState.FINISHED,
                        )
                    }
                }
            }
        }

    }
}