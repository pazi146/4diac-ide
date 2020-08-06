/*******************************************************************************
 * Copyright (c) 2017 fortiss GmbH
 *               2019, 2020 Johannes Kepler Univeristy Linz
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Monika Wenger
 *     - initial API and implementation and/or initial documentation
 *   Bianca Wiesmayr - create command now has enhanced guess
 *   Daniel Lindhuber - added addEntry method
 *******************************************************************************/
package org.eclipse.fordiac.ide.gef.properties;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.fordiac.ide.model.data.DataType;
import org.eclipse.fordiac.ide.model.libraryElement.Event;
import org.eclipse.fordiac.ide.model.libraryElement.IInterfaceElement;
import org.eclipse.fordiac.ide.model.libraryElement.InterfaceList;
import org.eclipse.fordiac.ide.model.typelibrary.EventTypeLibrary;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.viewers.IContentProvider;

public abstract class AbstractEditInterfaceEventSection extends AbstractEditInterfaceSection {

	@Override
	protected IContentProvider getOutputsContentProvider() {
		return new EventInterfaceContentProvider(false) {

		};
	}

	@Override
	protected IContentProvider getInputsContentProvider() {
		return new EventInterfaceContentProvider(true);
	}

	@Override
	protected String[] fillTypeCombo() {
		List<String> list = new ArrayList<>();
		for (DataType dataType : EventTypeLibrary.getInstance().getEventTypes()) {
			list.add(dataType.getName());
		}
		return list.toArray(new String[0]);
	}

	protected DataType getLastUsedEventType(InterfaceList interfaceList, boolean isInput,
			IInterfaceElement interfaceElement) {
		if (null != interfaceElement) {
			return interfaceElement.getType();
		}
		EList<Event> eventList = getEventList(interfaceList, isInput);
		if (!eventList.isEmpty()) {
			return eventList.get(eventList.size() - 1).getType();
		}
		return EventTypeLibrary.getInstance().getType(fillTypeCombo()[0]);
	}

	@Override
	protected int getInsertingIndex(IInterfaceElement interfaceElement, boolean isInput) {
		if (null != interfaceElement) {
			InterfaceList interfaceList = (InterfaceList) interfaceElement.eContainer();
			return getInsertingIndex(interfaceElement, getEventList(interfaceList, isInput));
		}
		return -1;
	}

	private static EList<Event> getEventList(InterfaceList interfaceList, boolean isInput) {
		return isInput ? interfaceList.getEventInputs() : interfaceList.getEventOutputs();
	}

	@Override
	public void addEntry(Object entry, int index, CompoundCommand cmd) {
		if (entry instanceof Event) {
			cmd.add(newInsertCommand((Event) entry, getIsInputsViewer(), index));
		}
	}

	protected static class EventInterfaceContentProvider extends InterfaceContentProvider {
		public EventInterfaceContentProvider(boolean inputs) {
			super(inputs);
		}

		@Override
		protected Object[] getInputs(Object inputElement) {
			InterfaceList interfaceList = getInterfaceListFromInput(inputElement);
			if (null != interfaceList) {
				return interfaceList.getEventInputs().toArray();
			}
			return new Object[0];
		}

		@Override
		protected Object[] getOutputs(Object inputElement) {
			InterfaceList interfaceList = getInterfaceListFromInput(inputElement);
			if (null != interfaceList) {
				return interfaceList.getEventOutputs().toArray();
			}
			return new Object[0];
		}
	}
}
