package eu.simonbinder.kart.kernel

import eu.simonbinder.kart.kernel.expressions.Arguments
import eu.simonbinder.kart.kernel.expressions.Expression
import eu.simonbinder.kart.kernel.expressions.ExpressionVisitor
import eu.simonbinder.kart.kernel.expressions.NamedExpression
import eu.simonbinder.kart.kernel.members.Component
import eu.simonbinder.kart.kernel.members.Library
import eu.simonbinder.kart.kernel.members.Member
import eu.simonbinder.kart.kernel.members.MemberVisitor
import eu.simonbinder.kart.kernel.statements.Statement
import eu.simonbinder.kart.kernel.statements.StatementVisitor
import eu.simonbinder.kart.kernel.types.TypeParameter

interface TreeVisitor<R> :
    ExpressionVisitor<R>,
    StatementVisitor<R>,
    MemberVisitor<R> {

    fun defaultTreeNode(node: TreeNode): R

    override fun defaultExpression(node: Expression): R = defaultTreeNode(node)
    override fun defaultStatement(node: Statement): R = defaultTreeNode(node)
    override fun defaultMember(node: Member): R = defaultTreeNode(node)

    fun visitArguments(node: Arguments): R = defaultTreeNode(node)
    fun visitComponent(node: Component): R = defaultTreeNode(node)
    fun visitFunctionNode(node: FunctionNode): R = defaultTreeNode(node)
    fun visitLibrary(node: Library): R = defaultTreeNode(node)
    fun visitNamedExpression(node: NamedExpression): R = defaultTreeNode(node)
    fun visitTypeParameter(node: TypeParameter): R = defaultTreeNode(node)
}