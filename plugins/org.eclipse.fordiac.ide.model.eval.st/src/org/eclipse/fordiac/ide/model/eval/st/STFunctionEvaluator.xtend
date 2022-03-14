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
package org.eclipse.fordiac.ide.model.eval.st

import org.eclipse.fordiac.ide.model.eval.Evaluator
import org.eclipse.fordiac.ide.model.eval.variable.Variable
import org.eclipse.fordiac.ide.structuredtextcore.stcore.STVarInputDeclarationBlock
import org.eclipse.fordiac.ide.structuredtextfunctioneditor.stfunction.STFunction
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor

import static org.eclipse.fordiac.ide.model.eval.variable.VariableOperations.*

@FinalFieldsConstructor
class STFunctionEvaluator extends StructuredTextEvaluator {
	final STFunction function

	final Variable returnVariable

	new(STFunction function, Iterable<Variable> variables, Evaluator parent) {
		super(function.name, variables, parent)
		this.function = function
		function.varDeclarations.filter(STVarInputDeclarationBlock).flatMap[varDeclarations].reject [
			this.variables.containsKey(name)
		].forEach [
			evaluateVariableInitialization
		]
		if (function.returnType !== null) {
			returnVariable = newVariable(function.name, function.returnType)
			this.variables.put(returnVariable.name, returnVariable)
		} else
			returnVariable = null
	}

	override prepare() {
	}

	override evaluate() {
		prepare();
		function.evaluateStructuredTextFunction
		returnVariable?.value
	}

	def protected void evaluateStructuredTextFunction(STFunction function) {
		function.varDeclarations.reject(STVarInputDeclarationBlock).flatMap[varDeclarations].forEach [
			evaluateVariableInitialization
		]
		try {
			function.code.evaluateStatementList
		} catch (StructuredTextException e) {
			// return
		}
	}

	override STFunction getSourceElement() {
		function
	}
}
