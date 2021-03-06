/*
 * Copyright (C) 2017, Ulrich Wolffgang <u.wol@wwu.de>
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD 3-clause license. See the LICENSE file for details.
 */

package io.proleap.vb6.asg.metamodel.statement.callstmt.impl;

import io.proleap.vb6.VisualBasic6Parser.ImplicitCallStmt_InBlockContext;
import io.proleap.vb6.asg.metamodel.Module;
import io.proleap.vb6.asg.metamodel.Scope;
import io.proleap.vb6.asg.metamodel.call.Call;
import io.proleap.vb6.asg.metamodel.call.impl.CallDelegateImpl;
import io.proleap.vb6.asg.metamodel.statement.StatementType;
import io.proleap.vb6.asg.metamodel.statement.StatementTypeEnum;
import io.proleap.vb6.asg.metamodel.statement.callstmt.CallStmt;

public class CallStmtImpl extends CallDelegateImpl implements CallStmt {

	protected final ImplicitCallStmt_InBlockContext ctx;

	protected final StatementType statementType = StatementTypeEnum.CALL;

	public CallStmtImpl(final Call delegate, final Module module, final Scope scope,
			final ImplicitCallStmt_InBlockContext ctx) {
		super(delegate, module, scope, ctx);

		this.ctx = ctx;
	}

	@Override
	public ImplicitCallStmt_InBlockContext getCtx() {
		return ctx;
	}

	@Override
	public StatementType getStatementType() {
		return statementType;
	}

}
