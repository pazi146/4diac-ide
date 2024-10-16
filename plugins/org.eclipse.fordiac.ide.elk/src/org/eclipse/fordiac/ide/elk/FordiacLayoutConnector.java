/*******************************************************************************
 * Copyright (c) 2020 Johannes Kepler University Linz
 * 				 2020 Primetals Technologies Germany GmbH
 * 				 2021, 2024 Primetals Technologies Austria GmbH
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Daniel Lindhuber, Bianca Wiesmayr, Alois Zoitl
 *     - initial API and implementation and/or initial documentation
 *******************************************************************************/

package org.eclipse.fordiac.ide.elk;

import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.elk.core.service.DiagramLayoutEngine;
import org.eclipse.elk.core.service.IDiagramLayoutConnector;
import org.eclipse.elk.core.service.LayoutConnectorsService;
import org.eclipse.elk.core.service.LayoutMapping;
import org.eclipse.elk.core.util.NullElkProgressMonitor;
import org.eclipse.elk.graph.properties.IPropertyHolder;
import org.eclipse.fordiac.ide.application.editparts.AbstractContainerContentEditPart;
import org.eclipse.fordiac.ide.application.editparts.SubAppForFBNetworkEditPart;
import org.eclipse.fordiac.ide.application.editparts.UnfoldedSubappContentEditPart;
import org.eclipse.fordiac.ide.elk.commands.LayoutCommand;
import org.eclipse.fordiac.ide.elk.helpers.FordiacGraphBuilder;
import org.eclipse.fordiac.ide.elk.helpers.FordiacGraphDataHelper;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;

import com.google.inject.Injector;

public class FordiacLayoutConnector implements IDiagramLayoutConnector {

	@Override
	public LayoutMapping buildLayoutGraph(final IWorkbenchPart workbenchPart, final Object diagramPart) {
		final FordiacLayoutMapping mapping = new FordiacLayoutMapping(workbenchPart, true);

		if (mapping.hasNetwork()) {
			FordiacGraphBuilder.build(mapping);
		}

		return mapping;
	}

	@Override
	public void applyLayout(final LayoutMapping mapping, final IPropertyHolder settings) {
		final var fordiacMapping = (FordiacLayoutMapping) mapping;
		FordiacGraphDataHelper.calculate(fordiacMapping);
		fordiacMapping.getCommandStack().execute(new LayoutCommand(fordiacMapping.getLayoutData()));

		// schedule as async to ensure the changes from the layout command have been
		// processed
		Display.getDefault().asyncExec(() -> {
			final var handlerService = PlatformUI.getWorkbench().getService(IHandlerService.class);
			final var networkEditPart = fordiacMapping.getNetworkEditPart();

			try {
				if (networkEditPart instanceof UnfoldedSubappContentEditPart) {
					final var event = new Event();
					event.data = networkEditPart.getParent(); // pass to the handler
					handlerService.executeCommand("org.eclipse.fordiac.ide.elk.expandedSubappConnectionLayout", event); //$NON-NLS-1$
				} else {
					handlerService.executeCommand("org.eclipse.fordiac.ide.elk.connectionLayout", null); //$NON-NLS-1$
				}
			} catch (ExecutionException | NotDefinedException | NotEnabledException | NotHandledException e) {
				e.printStackTrace();
			}
		});
	}

	public static void executeManually(final List<SubAppForFBNetworkEditPart> editParts) {
		final var workbenchPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
		final Injector injector = LayoutConnectorsService.getInstance().getInjector(workbenchPart, null);
		final DiagramLayoutEngine engine = injector.getInstance(DiagramLayoutEngine.class);
		final var handlerService = PlatformUI.getWorkbench().getService(IHandlerService.class);

		for (final var ep : editParts) {
			final FordiacLayoutMapping mapping = new FordiacLayoutMapping(workbenchPart, true,
					(AbstractContainerContentEditPart) ep.getContentEP());
			if (mapping.hasNetwork()) {
				FordiacGraphBuilder.build(mapping);
			}
			final IStatus status = engine.layout(mapping, new NullElkProgressMonitor(),
					new DiagramLayoutEngine.Parameters());
			if (status.isOK()) {
				FordiacGraphDataHelper.calculate(mapping);
				mapping.getCommandStack().execute(new LayoutCommand(mapping.getLayoutData()));
			}

			Display.getDefault().asyncExec(() -> {
				final var event = new Event();
				event.data = ep; // pass to the handler
				try {
					handlerService.executeCommand("org.eclipse.fordiac.ide.elk.expandedSubappConnectionLayout", event); //$NON-NLS-1$
				} catch (ExecutionException | NotDefinedException | NotEnabledException | NotHandledException e) {
					e.printStackTrace();
				}
			});

		}
	}

}