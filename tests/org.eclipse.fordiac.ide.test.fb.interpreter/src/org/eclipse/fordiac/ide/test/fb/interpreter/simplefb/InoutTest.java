/*******************************************************************************
 * Copyright (c) 2021 Johannes Kepler University Linz
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Antonio Garmend�a, Bianca Wiesmayr
 *       - initial implementation and/or documentation
 *******************************************************************************/
package org.eclipse.fordiac.ide.test.fb.interpreter.simplefb;

import static org.eclipse.fordiac.ide.fb.interpreter.api.TransactionFactory.addTransaction;
import static org.eclipse.fordiac.ide.fb.interpreter.mm.VariableUtils.setVariable;

import org.eclipse.fordiac.ide.fb.interpreter.api.FBTransactionBuilder;
import org.eclipse.fordiac.ide.model.libraryElement.ServiceSequence;
import org.eclipse.fordiac.ide.model.libraryElement.SimpleFBType;
import org.eclipse.fordiac.ide.test.fb.interpreter.infra.AbstractInterpreterTest;

public class InoutTest extends AbstractInterpreterTest {

	@Override
	public void test() {
		final SimpleFBType fb = (SimpleFBType) loadFBType("inout"); //$NON-NLS-1$
		final ServiceSequence seq = fb.getService().getServiceSequence().get(0);

		setVariable(fb, "IN", "1"); //$NON-NLS-1$ //$NON-NLS-2$
		addTransaction(seq, FBTransactionBuilder.getSimpleFBTransaction("OUT:=1")); //$NON-NLS-1$
		runFBTest(fb, seq);
	}

}
