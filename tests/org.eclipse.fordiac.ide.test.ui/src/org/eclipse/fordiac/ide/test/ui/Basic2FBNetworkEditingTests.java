/*******************************************************************************
 * Copyright (c) 2023 Andrea Zoitl
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Andrea Zoitl - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.fordiac.ide.test.ui;

import static org.eclipse.swtbot.swt.finder.waits.Conditions.treeItemHasNode;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.fordiac.ide.test.ui.swtbot.SWTBot4diacGefEditor;
import org.eclipse.fordiac.ide.test.ui.swtbot.SWTBot4diacGefViewer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swtbot.eclipse.finder.matchers.WidgetMatcherFactory;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditPart;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditor;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefFigureCanvas;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.eclipse.swtbot.swt.finder.widgets.TimeoutException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class Basic2FBNetworkEditingTests extends Abstract4diacUITests {

	/**
	 * Drags and Drops two Function Blocks onto the canvas.
	 *
	 * The method selects the event FB E_CYCLE from the System Explorer hierarchy
	 * tree and then drags it onto the canvas of the editing area. Afterwards it is
	 * looked whether an EditPart is to be found and to conclude that FB is present.
	 * Then a second FB (E_SWITCH) is dragged onto the canvas and the check is the
	 * same as above.
	 */
	@SuppressWarnings("static-method")
	@Test
	public void dragAndDrop2FB() {
		// select FB E_CYCLE
		final SWTBotView systemExplorerView = bot.viewById(SYSTEM_EXPLORER_ID);
		systemExplorerView.show();
		final Composite systemExplorerComposite = (Composite) systemExplorerView.getWidget();
		final Tree swtTree = bot.widget(WidgetMatcherFactory.widgetOfType(Tree.class), systemExplorerComposite);
		final SWTBotTree tree = new SWTBotTree(swtTree);
		assertNotNull(tree);
		final SWTBotTreeItem treeProjectItem = tree.getTreeItem(PROJECT_NAME);
		assertNotNull(treeProjectItem);
		treeProjectItem.select();
		treeProjectItem.expand();
		final SWTBotTreeItem typeLibraryNode = treeProjectItem.getNode(TYPE_LIBRARY_NODE);
		assertNotNull(typeLibraryNode);
		typeLibraryNode.select();
		typeLibraryNode.expand();
		final SWTBotTreeItem eventsNode = typeLibraryNode.getNode(EVENTS_NODE);
		assertNotNull(eventsNode);
		eventsNode.select();
		eventsNode.expand();
		bot.waitUntil(treeItemHasNode(eventsNode, E_CYCLE_TREE_ITEM));
		final SWTBotTreeItem eCycleNode = eventsNode.getNode(E_CYCLE_TREE_ITEM);
		assertNotNull(eCycleNode);
		eCycleNode.select();
		eCycleNode.click();
		// select application editor
		final SWTBotGefEditor editor = bot.gefEditor(PROJECT_NAME);
		assertNotNull(editor);
		final SWTBot4diacGefViewer viewer = (SWTBot4diacGefViewer) editor.getSWTBotGefViewer();
		assertNotNull(viewer);
		final SWTBotGefFigureCanvas canvas = viewer.getCanvas();
		assertNotNull(canvas);
		final Point point1 = new Point(100, 100);
		eCycleNode.dragAndDrop(canvas, point1);
		assertNotNull(editor.getEditPart(E_CYCLE_FB));
		final Rectangle absPos1 = getAbsolutePosition(editor, E_CYCLE_FB);
		assertEquals(point1.x, absPos1.x);
		assertEquals(point1.y, absPos1.y);

		// select FB E_SWITCH
		final SWTBotTreeItem eSwitchNode = eventsNode.getNode(E_SWITCH_TREE_ITEM);
		assertNotNull(eSwitchNode);
		eSwitchNode.select();
		eSwitchNode.click();
		final Point point2 = new Point(300, 150);
		eSwitchNode.dragAndDrop(canvas, point2);
		assertNotNull(editor.getEditPart(E_SWITCH_FB));
		final Rectangle absPos2 = getAbsolutePosition(editor, E_SWITCH_FB);
		assertEquals(point2.x, absPos2.x);
		assertEquals(point2.y, absPos2.y);
	}

	/**
	 * The Method drag and drops 2 FBs onto the editing area. Afterwards one FB is
	 * deleted and it is checked if the FB is no longer on the canvas after
	 * deletion.
	 */
	@SuppressWarnings("static-method")
	@Test
	public void delete1FB() {
		dragAndDropEventsFB(E_CYCLE_TREE_ITEM, new Point(300, 100));
		dragAndDropEventsFB(E_SWITCH_TREE_ITEM, new Point(100, 100));
		final SWTBotGefEditor editor = bot.gefEditor(PROJECT_NAME);
		assertNotNull(editor);
		assertNotNull(editor.getEditPart(E_CYCLE_FB));
		assertNotNull(editor.getEditPart(E_SWITCH_FB));
		editor.click(E_CYCLE_FB);
		final SWTBotGefEditPart parent = editor.getEditPart(E_CYCLE_FB).parent();
		assertNotNull(parent);
		parent.click();
		bot.menu(EDIT).menu(DELETE).click();
		assertNull(editor.getEditPart(E_CYCLE_FB));
		assertNotNull(editor.getEditPart(E_SWITCH_FB));
		assertTrue(editor.selectedEditParts().isEmpty());
	}

	/**
	 *
	 * Checks if FBs can be selected together by drawing a rectangle by mouse left
	 * click over the FBs
	 *
	 * First, a rectangle is drawn next to the FBs to check whether they are not
	 * selected as expected. Then a rectangle is drawn over the FBs to check whether
	 * the FBs are selected.
	 */
	@SuppressWarnings("static-method")
	@Test
	public void select2FBsViaMouseLeftClickOnFB() {
		dragAndDropEventsFB(E_N_TABLE_TREE_ITEM, new Point(100, 100));
		dragAndDropEventsFB(E_CTUD_TREE_ITEM, new Point(300, 100));
		final SWTBot4diacGefEditor editor = (SWTBot4diacGefEditor) bot.gefEditor(PROJECT_NAME);

		// drag rectangle next to FBs, therefore FBs should not be selected
		editor.drag(40, 40, 200, 200);
		assertThrows(TimeoutException.class, () -> editor.waitForSelectedFBEditPart());
		List<SWTBotGefEditPart> selectedEditParts = editor.selectedEditParts();
		assertTrue(selectedEditParts.isEmpty());
		assertFalse(isFbSelected(selectedEditParts, E_N_TABLE_FB));
		assertFalse(isFbSelected(selectedEditParts, E_CTUD_FB));

		// drag rectangle over FBs, therefore FBs should be selected
		editor.drag(50, 50, 500, 300);
		assertDoesNotThrow(() -> editor.waitForSelectedFBEditPart());
		selectedEditParts = editor.selectedEditParts();
		assertFalse(selectedEditParts.isEmpty());
		assertTrue(isFbSelected(selectedEditParts, E_N_TABLE_FB));
		assertTrue(isFbSelected(selectedEditParts, E_CTUD_FB));
	}

	/**
	 * Checks if the FB can be moved onto the canvas.
	 *
	 * The method drag and drops 2 FBs E_CYCLE and E_SWITCH to a certain position
	 * onto the canvas and it is checked whether the FBs are in the correct
	 * position. Afterwards one FB is moved to a new point and this position is also
	 * checked. Than it is checks whether the position of E_SWITCH has changed,
	 * which is not expected. To achieve this it is necessary to create a
	 * draw2d.geometry Point with the same coordinates of the swt.graphics Point.
	 */
	@SuppressWarnings("static-method")
	@Test
	public void move1FB() {
		final Point pos1 = new Point(100, 100);
		dragAndDropEventsFB(E_CYCLE_TREE_ITEM, pos1);
		final Point pos2 = new Point(350, 100);
		dragAndDropEventsFB(E_N_TABLE_TREE_ITEM, pos2);

		// select E_CYCLE and check position of E_CYCLE
		final SWTBotGefEditor editor = bot.gefEditor(PROJECT_NAME);
		assertNotNull(editor);
		assertNotNull(editor.getEditPart(E_CYCLE_FB));
		editor.click(E_CYCLE_FB);
		final SWTBotGefEditPart fb1 = editor.getEditPart(E_CYCLE_FB).parent();
		assertNotNull(fb1);
		final Rectangle fb1Bounds1 = getAbsolutePosition(editor, E_CYCLE_FB);
		assertTrue(fb1Bounds1.contains(pos1.x, pos1.y));

		// check position of E_N_TABLE
		final Rectangle fb2Bounds1 = getAbsolutePosition(editor, E_N_TABLE_FB);
		assertTrue(fb2Bounds1.contains(pos2.x, pos2.y));

		// move E_CYCLE and check new position
		final Point pos3 = new Point(125, 185);
		editor.drag(fb1, pos3.x, pos3.y);
		final Rectangle fb1Bounds2 = getAbsolutePosition(editor, E_CYCLE_FB);
		assertTrue(fb1Bounds2.contains(pos3.x, pos3.y));

		// check if E_N_TABLE is still on same position
		assertTrue(fb2Bounds1.contains(pos2.x, pos2.y));
	}

	/**
	 * Checks if FBs can be moved on the canvas.
	 *
	 * The method drag and drops 2 FBs E_CYCLE and E_SWITCH to a certain position
	 * onto the canvas and it is checked whether the FBs are in the correct
	 * position. Afterwards the FBs are moved one after another to a new point and
	 * this position is also checked. To achieve this it is necessary to create a
	 * draw2d.geometry Point with the same coordinates of the swt.graphics Point.
	 */
	@SuppressWarnings("static-method")
	@Test
	public void moveBothFBOneAfterAnother() {
		final Point pos1 = new Point(200, 200);
		dragAndDropEventsFB(E_CYCLE_TREE_ITEM, pos1);
		final Point pos2 = new Point(400, 200);
		dragAndDropEventsFB(E_SWITCH_TREE_ITEM, pos2);

		// select and move E_CYCLE
		final SWTBotGefEditor editor = bot.gefEditor(PROJECT_NAME);
		assertNotNull(editor);
		assertNotNull(editor.getEditPart(E_CYCLE_FB));
		editor.click(E_CYCLE_FB);
		final SWTBotGefEditPart fb1 = editor.getEditPart(E_CYCLE_FB).parent();
		assertNotNull(fb1);
		final Rectangle fb1Bounds1 = getAbsolutePosition(editor, E_CYCLE_FB);
		assertTrue(fb1Bounds1.contains(pos1.x, pos1.y));
		final Point pos3 = new Point(85, 85);
		editor.drag(fb1, pos3.x, pos3.y);
		final Rectangle fb1Bounds2 = getAbsolutePosition(editor, E_CYCLE_FB);
		assertTrue(fb1Bounds2.contains(pos3.x, pos3.y));

		// select and move E_SWITCH
		editor.click(E_SWITCH_FB);
		final SWTBotGefEditPart fb2 = editor.getEditPart(E_SWITCH_FB).parent();
		assertNotNull(fb2);
		final Rectangle fb2Bounds1 = getAbsolutePosition(editor, E_SWITCH_FB);
		assertTrue(fb2Bounds1.contains(pos2.x, pos2.y));
		final Point pos4 = new Point(285, 85);
		editor.drag(fb2, pos4.x, pos4.y);
		final Rectangle fb2Bounds2 = getAbsolutePosition(editor, E_SWITCH_FB);
		assertTrue(fb2Bounds2.contains(pos4.x, pos4.y));
	}

	/**
	 * Checks if FBs can be selected and moved together by drawing a rectangle by
	 * mouse left click over the FBs
	 *
	 * First, a rectangle is drawn next to the FBs to check whether they are not
	 * selected as expected. Then a rectangle is drawn over the FBs to check whether
	 * the FBs are selected.
	 */
	@SuppressWarnings("static-method")
	@Test
	public void moveBothFBTogether() {
		final Point absPos1Fb1 = new Point(100, 100);
		dragAndDropEventsFB(E_CYCLE_TREE_ITEM, absPos1Fb1);
		final Point absPos1Fb2 = new Point(100, 220);
		dragAndDropEventsFB(E_SR_TREE_ITEM, absPos1Fb2);
		final SWTBot4diacGefEditor editor = (SWTBot4diacGefEditor) bot.gefEditor(PROJECT_NAME);

		final Rectangle fb1Bounds1 = getAbsolutePosition(editor, E_CYCLE_FB);
		assertTrue(fb1Bounds1.contains(absPos1Fb1.x, absPos1Fb1.y));
		final Rectangle fb2Bounds1 = getAbsolutePosition(editor, E_SR_FB);
		assertTrue(fb2Bounds1.contains(absPos1Fb2.x, absPos1Fb2.y));

		// drag rectangle over FBs, therefore FBs should be selected
		editor.drag(50, 50, 400, 400);
		assertDoesNotThrow(() -> editor.waitForSelectedFBEditPart());
		List<SWTBotGefEditPart> selectedEditParts = editor.selectedEditParts();
		assertFalse(selectedEditParts.isEmpty());
		assertTrue(isFbSelected(selectedEditParts, E_CYCLE_FB));
		assertTrue(isFbSelected(selectedEditParts, E_SR_FB));

		// move selection by clicking on point within selection (120, 120) and drag to
		// new Point (285, 85)
		final Point pointFrom = new Point(120, 120);
		final Point pointTo = new Point(285, 85);
		editor.drag(pointFrom.x, pointFrom.y, pointTo.x, pointTo.y);

		assertDoesNotThrow(() -> editor.waitForSelectedFBEditPart());
		selectedEditParts = editor.selectedEditParts();
		assertFalse(selectedEditParts.isEmpty());
		assertTrue(isFbSelected(selectedEditParts, E_CYCLE_FB));
		assertTrue(isFbSelected(selectedEditParts, E_SR_FB));

		// Calculation of translation
		final int translationX = pointTo.x - pointFrom.x;
		final int translationY = pointTo.y - pointFrom.y;

		// Calculation of new Position of E_CYCLEs
		final int absPos2Fb1X = absPos1Fb1.x + translationX;
		final int absPos2Fb1Y = absPos1Fb1.y + translationY;
		final Rectangle fb1Bounds2 = getAbsolutePosition(editor, E_CYCLE_FB);
		assertEquals(absPos2Fb1X, fb1Bounds2.x);
		assertEquals(absPos2Fb1Y, fb1Bounds2.y);
		assertTrue(fb1Bounds2.contains(absPos2Fb1X, absPos2Fb1Y));

		// Calculation of new Position of E_SR
		final int absPos2Fb2X = absPos1Fb2.x + translationX;
		final int absPos2Fb2Y = absPos1Fb2.y + translationY;
		final Rectangle fb2Bounds2 = getAbsolutePosition(editor, E_SR_FB);
		assertEquals(absPos2Fb2X, fb2Bounds2.x);
		assertEquals(absPos2Fb2Y, fb2Bounds2.y);
		assertTrue(fb2Bounds2.contains(absPos2Fb2X, absPos2Fb2Y));
	}

	/**
	 * Checks if connections stays in place after moving one FB
	 *
	 */
	@Disabled("until implementation")
	@Test
	public void checkIfConnectionRemainsAfterMoving1FB() {
		// in progress
	}

	/**
	 * Checks if connections stays in place after moving both FBs at the same time
	 *
	 */
	@Disabled("until implementation")
	@Test
	public void checkIfConnectionRemainsAfterMovingBothFBsTogether() {
		// in progress
	}

}