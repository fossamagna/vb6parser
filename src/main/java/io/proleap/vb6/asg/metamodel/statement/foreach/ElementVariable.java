/*
 * Copyright (C) 2018, Ulrich Wolffgang <u.wol@wwu.de>
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD 3-clause license. See the LICENSE file for details.
 */

package io.proleap.vb6.asg.metamodel.statement.foreach;

import java.util.List;

import io.proleap.vb6.VisualBasic6Parser.AmbiguousIdentifierContext;
import io.proleap.vb6.asg.metamodel.Declaration;
import io.proleap.vb6.asg.metamodel.ScopedElement;
import io.proleap.vb6.asg.metamodel.call.ElementVariableCall;
import io.proleap.vb6.asg.metamodel.type.AssignableTypedElement;

public interface ElementVariable extends AssignableTypedElement, Declaration, ScopedElement {

	void addElementVariableCall(ElementVariableCall elementVariableCall);

	@Override
	AmbiguousIdentifierContext getCtx();

	List<ElementVariableCall> getElementVariableCalls();
}
