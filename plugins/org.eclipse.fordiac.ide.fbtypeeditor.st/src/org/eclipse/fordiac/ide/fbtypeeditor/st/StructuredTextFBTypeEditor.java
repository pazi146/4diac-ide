/*******************************************************************************
 * Copyright (c) 2022 Martin Erich Jobst
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Martin Jobst - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.fordiac.ide.fbtypeeditor.st;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.fordiac.ide.fbtypeeditor.editors.FBTypeXtextEditor;
import org.eclipse.fordiac.ide.structuredtextalgorithm.ui.document.STAlgorithmDocument;
import org.eclipse.fordiac.ide.structuredtextalgorithm.ui.document.STAlgorithmDocumentFBTypeUpdater;
import org.eclipse.fordiac.ide.structuredtextalgorithm.util.STAlgorithmMapper;
import org.eclipse.fordiac.ide.ui.FordiacMessages;
import org.eclipse.fordiac.ide.ui.imageprovider.FordiacImage;
import org.eclipse.ui.IEditorInput;
import org.eclipse.xtext.resource.ILocationInFileProvider;
import org.eclipse.xtext.util.ITextRegion;

import com.google.inject.Inject;

public class StructuredTextFBTypeEditor extends FBTypeXtextEditor {

	@Inject
	private STAlgorithmMapper algorithmMapper;

	@Inject
	private ILocationInFileProvider locationProvider;

	@Inject
	private STAlgorithmDocumentFBTypeUpdater fbTypeUpdater;

	@Override
	protected void installFBTypeUpdater() {
		if (getDocument() instanceof STAlgorithmDocument) {
			fbTypeUpdater.install((STAlgorithmDocument) getDocument());
		}
	}
	
	@Override
	protected void removeFBTypeUpdater() {
		fbTypeUpdater.uninstall();
	}
	
	@Override
	protected void doSetInput(IEditorInput input) throws CoreException {
		super.doSetInput(input);
		setPartName(FordiacMessages.Algorithm);
		setTitleImage(FordiacImage.ICON_ALGORITHM.getImage());
	}

	@Override
	protected boolean selectAndReveal(final Object element, final boolean revealEditor) {
		final ITextRegion location = getDocument().priorityReadOnly(resource -> {
			if (resource != null) {
				final EObject object = algorithmMapper.fromModel(resource, element);
				if (object != null) {
					return locationProvider.getSignificantTextRegion(object);
				}
			}
			return null;
		});
		if (location != null) {
			selectAndReveal(location.getOffset(), location.getLength());
			if (revealEditor) {
				revealEditor();
			}
		}
		return location != null;
	}

	@Override
	public String getEditorId() {
		return "org.eclipse.fordiac.ide.structuredtextalgorithm.STAlgorithm"; //$NON-NLS-1$
	}
}
