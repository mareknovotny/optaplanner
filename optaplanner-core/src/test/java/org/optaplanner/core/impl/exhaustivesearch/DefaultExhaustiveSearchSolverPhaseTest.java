/*
 * Copyright 2014 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.optaplanner.core.impl.exhaustivesearch;

import org.junit.Test;
import org.mockito.Matchers;
import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.api.score.buildin.simple.SimpleScore;
import org.optaplanner.core.impl.exhaustivesearch.decider.ExhaustiveSearchDecider;
import org.optaplanner.core.impl.exhaustivesearch.node.ExhaustiveSearchLayer;
import org.optaplanner.core.impl.exhaustivesearch.node.ExhaustiveSearchNode;
import org.optaplanner.core.impl.exhaustivesearch.scope.ExhaustiveSearchSolverPhaseScope;
import org.optaplanner.core.impl.exhaustivesearch.scope.ExhaustiveSearchStepScope;
import org.optaplanner.core.impl.heuristic.move.Move;
import org.optaplanner.core.impl.heuristic.selector.entity.EntitySelector;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import org.optaplanner.core.impl.solution.Solution;

import static org.mockito.Mockito.*;
import static org.optaplanner.core.impl.testdata.util.PlannerAssert.*;

public class DefaultExhaustiveSearchSolverPhaseTest {

    @Test
    public void restoreWorkingSolution() {
        ExhaustiveSearchSolverPhaseScope phaseScope = mock(ExhaustiveSearchSolverPhaseScope.class);
        ExhaustiveSearchStepScope lastCompletedStepScope = mock(ExhaustiveSearchStepScope.class);
        when(phaseScope.getLastCompletedStepScope()).thenReturn(lastCompletedStepScope);
        ExhaustiveSearchStepScope stepScope = mock(ExhaustiveSearchStepScope.class);
        when(stepScope.getPhaseScope()).thenReturn(phaseScope);
        Solution workingSolution = mock(Solution.class);
        when(phaseScope.getWorkingSolution()).thenReturn(workingSolution);

        ExhaustiveSearchLayer layer0 = new ExhaustiveSearchLayer(0, mock(Object.class));
        ExhaustiveSearchLayer layer1 = new ExhaustiveSearchLayer(1, mock(Object.class));
        ExhaustiveSearchLayer layer2 = new ExhaustiveSearchLayer(2, mock(Object.class));
        ExhaustiveSearchLayer layer3 = new ExhaustiveSearchLayer(3, mock(Object.class));
        ExhaustiveSearchLayer layer4 = new ExhaustiveSearchLayer(4, mock(Object.class));
        ExhaustiveSearchNode node0 = new ExhaustiveSearchNode(layer0, null, 0);
        node0.setMove(mock(Move.class));
        node0.setUndoMove(mock(Move.class));
        ExhaustiveSearchNode node1 = new ExhaustiveSearchNode(layer1, node0, 0);
        node1.setMove(mock(Move.class));
        node1.setUndoMove(mock(Move.class));
        ExhaustiveSearchNode node2A = new ExhaustiveSearchNode(layer2, node1, 0);
        node2A.setMove(mock(Move.class));
        node2A.setUndoMove(mock(Move.class));
        ExhaustiveSearchNode node3A = new ExhaustiveSearchNode(layer3, node2A, 0); // oldNode
        node3A.setMove(mock(Move.class));
        node3A.setUndoMove(mock(Move.class));
        ExhaustiveSearchNode node2B = new ExhaustiveSearchNode(layer2, node1, 1);
        node2B.setMove(mock(Move.class));
        node2B.setUndoMove(mock(Move.class));
        ExhaustiveSearchNode node3B = new ExhaustiveSearchNode(layer3, node2B, 1);
        node3B.setMove(mock(Move.class));
        node3B.setUndoMove(mock(Move.class));
        ExhaustiveSearchNode node4B = new ExhaustiveSearchNode(layer4, node3B, 0); // newNode
        node4B.setMove(mock(Move.class));
        node4B.setUndoMove(mock(Move.class));
        Score newScore = SimpleScore.valueOf(7);
        node4B.setScore(newScore);
        when(lastCompletedStepScope.getExpandingNode()).thenReturn(node3A);
        when(stepScope.getExpandingNode()).thenReturn(node4B);

        DefaultExhaustiveSearchSolverPhase solverPhase = new DefaultExhaustiveSearchSolverPhase();
        solverPhase.setEntitySelector(mock(EntitySelector.class));
        solverPhase.setDecider(mock(ExhaustiveSearchDecider.class));
        solverPhase.restoreWorkingSolution(stepScope);

        verify(node0.getMove(), times(0)).doMove(any(ScoreDirector.class));
        verify(node0.getUndoMove(), times(0)).doMove(any(ScoreDirector.class));
        verify(node1.getMove(), times(0)).doMove(any(ScoreDirector.class));
        verify(node1.getUndoMove(), times(0)).doMove(any(ScoreDirector.class));
        verify(node2A.getMove(), times(0)).doMove(any(ScoreDirector.class));
        verify(node2A.getUndoMove(), times(1)).doMove(any(ScoreDirector.class));
        verify(node3A.getMove(), times(0)).doMove(any(ScoreDirector.class));
        verify(node3A.getUndoMove(), times(1)).doMove(any(ScoreDirector.class));
        verify(node2B.getMove(), times(1)).doMove(any(ScoreDirector.class));
        verify(node2B.getUndoMove(), times(0)).doMove(any(ScoreDirector.class));
        verify(node3B.getMove(), times(1)).doMove(any(ScoreDirector.class));
        verify(node3B.getUndoMove(), times(0)).doMove(any(ScoreDirector.class));
        verify(node4B.getMove(), times(1)).doMove(any(ScoreDirector.class));
        verify(node4B.getUndoMove(), times(0)).doMove(any(ScoreDirector.class));
        // TODO FIXME
        // verify(workingSolution).setScore(newScore);
    }

}
