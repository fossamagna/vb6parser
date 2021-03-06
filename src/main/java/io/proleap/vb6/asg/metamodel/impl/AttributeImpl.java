/*
 * Copyright (C) 2016, Ulrich Wolffgang <u.wol@wwu.de>
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD 3-clause license. See the LICENSE file for details.
 */

package io.proleap.vb6.asg.metamodel.impl;

import io.proleap.vb6.VisualBasic6Parser.AttributeStmtContext;
import io.proleap.vb6.asg.metamodel.Attribute;
import io.proleap.vb6.asg.metamodel.Literal;
import io.proleap.vb6.asg.metamodel.Module;
import io.proleap.vb6.asg.metamodel.Scope;
import io.proleap.vb6.asg.metamodel.type.Type;

public class AttributeImpl extends ScopedElementImpl implements Attribute {

	protected final AttributeStmtContext ctx;

	protected final String name;

	protected final Type type;

	protected Literal value;

	public AttributeImpl(final String name, final Type type, final Module module, final Scope scope,
			final AttributeStmtContext ctx) {
		super(module.getProgram(), module, scope, ctx);

		this.ctx = ctx;
		this.name = name;
		this.type = type;
	}

	@Override
	public AttributeStmtContext getCtx() {
		return ctx;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Type getType() {
		return type;
	}

	@Override
	public Literal getValue() {
		return value;
	}

	@Override
	public void setValue(final Literal value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "name=[" + name + "]";
	}
}
