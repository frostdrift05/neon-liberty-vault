package com.Meztido0.Dewgov.presentation.game.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.Meztido0.Dewgov.core.config.GameConfig
import com.Meztido0.Dewgov.core.theme.BorderDim
import com.Meztido0.Dewgov.core.theme.ChromeCyan
import com.Meztido0.Dewgov.presentation.game.NodeState
import com.Meztido0.Dewgov.presentation.game.NodeUi

@Composable
fun TacticalMap(
    nodes: List<NodeUi>,
    currentIndex: Int,
    glow: Float,
    travel: Float,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val gap: Dp = 12.dp
    BoxWithConstraints(modifier = modifier) {
        val gridSize = GameConfig.GRID_SIZE
        val cell = min(
            (maxWidth - gap * (gridSize - 1)) / gridSize,
            (maxHeight - gap * (gridSize - 1)) / gridSize
        )
        val boardSide = cell * gridSize + gap * (gridSize - 1)
        Box(
            modifier = Modifier
                .size(boardSide, boardSide)
                .align(Alignment.Center)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val step = (cell + gap).toPx()
                val half = cell.toPx() / 2f
                for (node in nodes) {
                    for (neighbour in nodes) {
                        if (neighbour.index <= node.index) continue
                        val sameRow = node.row == neighbour.row && neighbour.col - node.col == 1
                        val sameCol = node.col == neighbour.col && neighbour.row - node.row == 1
                        if (!sameRow && !sameCol) continue
                        val from = Offset(node.col * step + half, node.row * step + half)
                        val to = Offset(neighbour.col * step + half, neighbour.row * step + half)
                        val live = (node.index == currentIndex && neighbour.state == NodeState.REACHABLE) ||
                            (neighbour.index == currentIndex && node.state == NodeState.REACHABLE) ||
                            (node.index == currentIndex && neighbour.state == NodeState.SELECTED) ||
                            (neighbour.index == currentIndex && node.state == NodeState.SELECTED)
                        drawLine(
                            color = if (live) ChromeCyan.copy(alpha = 0.6f) else BorderDim.copy(alpha = 0.6f),
                            start = from,
                            end = to,
                            strokeWidth = 2.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                        if (live) {
                            val anchor = if (node.index == currentIndex) from else to
                            val target = if (node.index == currentIndex) to else from
                            val dot = Offset(
                                anchor.x + (target.x - anchor.x) * travel,
                                anchor.y + (target.y - anchor.y) * travel
                            )
                            drawCircle(
                                color = ChromeCyan.copy(alpha = 0.85f),
                                radius = 3.dp.toPx(),
                                center = dot
                            )
                        }
                    }
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(gap)) {
                for (row in 0 until gridSize) {
                    Row(horizontalArrangement = Arrangement.spacedBy(gap)) {
                        for (col in 0 until gridSize) {
                            val node = nodes.firstOrNull { it.row == row && it.col == col }
                            if (node != null) {
                                DistrictNode(
                                    node = node,
                                    glow = glow,
                                    onSelect = onSelect,
                                    modifier = Modifier.size(cell, cell)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
