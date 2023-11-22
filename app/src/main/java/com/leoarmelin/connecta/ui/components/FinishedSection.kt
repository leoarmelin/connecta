package com.leoarmelin.connecta.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.leoarmelin.connecta.models.Word
import com.leoarmelin.connecta.ui.theme.Typography

@OptIn(ExperimentalLayoutApi::class)
fun FinishedSection(
    finishedWords: List<Word>,
    lazyListScope: LazyListScope,
    sections: List<String>,
    wordsVisibilityAnimSpec: FiniteAnimationSpec<IntOffset>,
    hasPadding: Boolean = false
) {
    lazyListScope.items(sections, key = { it }) { section ->
        AnimatedVisibility(
            visible = finishedWords.any { it.category == section },
            enter = slideInVertically(animationSpec = wordsVisibilityAnimSpec) + fadeIn(),
            exit = slideOutVertically(animationSpec = wordsVisibilityAnimSpec) + fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = section,
                    style = Typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = if (hasPadding) Modifier.padding(horizontal = 8.dp) else Modifier
                )

                FlowRow(modifier = Modifier.padding(top = 16.dp)) {
                    finishedWords.filter { it.category == section }.forEach { word ->
                        WordPill(
                            wordValue = word.value,
                            state = WordPillState.FINISHED,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}