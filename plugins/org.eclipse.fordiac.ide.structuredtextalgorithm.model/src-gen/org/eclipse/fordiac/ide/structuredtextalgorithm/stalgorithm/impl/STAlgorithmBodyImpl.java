/**
 * *******************************************************************************
 * Copyright (c) 2022 Martin Erich Jobst
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *    Martin Jobst
 *      - initial API and implementation and/or initial documentation
 * *******************************************************************************
 */
package org.eclipse.fordiac.ide.structuredtextalgorithm.stalgorithm.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.fordiac.ide.structuredtextalgorithm.stalgorithm.STAlgorithmBody;
import org.eclipse.fordiac.ide.structuredtextalgorithm.stalgorithm.STAlgorithmPackage;

import org.eclipse.fordiac.ide.structuredtextcore.stcore.STStatement;
import org.eclipse.fordiac.ide.structuredtextcore.stcore.STVarDeclarationBlock;
import org.eclipse.fordiac.ide.structuredtextcore.stcore.STVarTempDeclarationBlock;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Body</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fordiac.ide.structuredtextalgorithm.stalgorithm.impl.STAlgorithmBodyImpl#getVarTempDeclarations <em>Var Temp Declarations</em>}</li>
 *   <li>{@link org.eclipse.fordiac.ide.structuredtextalgorithm.stalgorithm.impl.STAlgorithmBodyImpl#getStatements <em>Statements</em>}</li>
 * </ul>
 *
 * @generated
 */
public class STAlgorithmBodyImpl extends MinimalEObjectImpl.Container implements STAlgorithmBody {
	/**
	 * The cached value of the '{@link #getVarTempDeclarations() <em>Var Temp Declarations</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVarTempDeclarations()
	 * @generated
	 * @ordered
	 */
	protected EList<STVarTempDeclarationBlock> varTempDeclarations;

	/**
	 * The cached value of the '{@link #getStatements() <em>Statements</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStatements()
	 * @generated
	 * @ordered
	 */
	protected EList<STStatement> statements;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected STAlgorithmBodyImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return STAlgorithmPackage.Literals.ST_ALGORITHM_BODY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<STVarTempDeclarationBlock> getVarTempDeclarations() {
		if (varTempDeclarations == null) {
			varTempDeclarations = new EObjectContainmentEList<STVarTempDeclarationBlock>(STVarTempDeclarationBlock.class, this, STAlgorithmPackage.ST_ALGORITHM_BODY__VAR_TEMP_DECLARATIONS);
		}
		return varTempDeclarations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<STStatement> getStatements() {
		if (statements == null) {
			statements = new EObjectContainmentEList<STStatement>(STStatement.class, this, STAlgorithmPackage.ST_ALGORITHM_BODY__STATEMENTS);
		}
		return statements;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case STAlgorithmPackage.ST_ALGORITHM_BODY__VAR_TEMP_DECLARATIONS:
				return ((InternalEList<?>)getVarTempDeclarations()).basicRemove(otherEnd, msgs);
			case STAlgorithmPackage.ST_ALGORITHM_BODY__STATEMENTS:
				return ((InternalEList<?>)getStatements()).basicRemove(otherEnd, msgs);
			default:
				return super.eInverseRemove(otherEnd, featureID, msgs);
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case STAlgorithmPackage.ST_ALGORITHM_BODY__VAR_TEMP_DECLARATIONS:
				return getVarTempDeclarations();
			case STAlgorithmPackage.ST_ALGORITHM_BODY__STATEMENTS:
				return getStatements();
			default:
				return super.eGet(featureID, resolve, coreType);
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case STAlgorithmPackage.ST_ALGORITHM_BODY__VAR_TEMP_DECLARATIONS:
				getVarTempDeclarations().clear();
				getVarTempDeclarations().addAll((Collection<? extends STVarTempDeclarationBlock>)newValue);
				return;
			case STAlgorithmPackage.ST_ALGORITHM_BODY__STATEMENTS:
				getStatements().clear();
				getStatements().addAll((Collection<? extends STStatement>)newValue);
				return;
			default:
				super.eSet(featureID, newValue);
				return;
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case STAlgorithmPackage.ST_ALGORITHM_BODY__VAR_TEMP_DECLARATIONS:
				getVarTempDeclarations().clear();
				return;
			case STAlgorithmPackage.ST_ALGORITHM_BODY__STATEMENTS:
				getStatements().clear();
				return;
			default:
				super.eUnset(featureID);
				return;
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case STAlgorithmPackage.ST_ALGORITHM_BODY__VAR_TEMP_DECLARATIONS:
				return varTempDeclarations != null && !varTempDeclarations.isEmpty();
			case STAlgorithmPackage.ST_ALGORITHM_BODY__STATEMENTS:
				return statements != null && !statements.isEmpty();
			default:
				return super.eIsSet(featureID);
		}
	}

} //STAlgorithmBodyImpl
