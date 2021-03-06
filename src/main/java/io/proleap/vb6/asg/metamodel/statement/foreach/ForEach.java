/*
 * Copyright (C) 2016, Ulrich Wolffgang <u.wol@wwu.de>
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD 3-clause license. See the LICENSE file for details.
 */

package io.proleap.vb6.asg.metamodel.statement.foreach;

import io.proleap.vb6.VisualBasic6Parser.ForEachStmtContext;
import io.proleap.vb6.asg.metamodel.Scope;
import io.proleap.vb6.asg.metamodel.statement.Statement;
import io.proleap.vb6.asg.metamodel.valuestmt.ValueStmt;

/**
 * Repeats a group of statements for each element in an array or collection.
 */
public interface ForEach extends Scope, Statement {

	@Override
	ForEachStmtContext getCtx();

	ElementVariable getElementVariable();

	ValueStmt getIn();

	void setElementVariable(ElementVariable elementVariable);

	void setIn(ValueStmt in);
}
