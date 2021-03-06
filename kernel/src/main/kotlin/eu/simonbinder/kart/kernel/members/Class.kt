package eu.simonbinder.kart.kernel.members

import eu.simonbinder.kart.kernel.NamedNode
import eu.simonbinder.kart.kernel.Reference
import eu.simonbinder.kart.kernel.TreeVisitor
import eu.simonbinder.kart.kernel.Uri
import eu.simonbinder.kart.kernel.types.DartType
import eu.simonbinder.kart.kernel.utils.HasFlags
import eu.simonbinder.kart.kernel.utils.children
import eu.simonbinder.kart.kernel.utils.flag

class Class(
    reference: Reference,
    var name: String = reference.canonicalName?.name ?: "",
    var fileUri: Uri? = null,
    var superClass: DartType? = null,
    var implementedClasses: MutableList<DartType> = mutableListOf()
) : NamedNode(reference), HasFlags, HasMembers {

    // Note: We set the lowest two bit so that levelBit0 and levelBit1 are set (corresponds to ClassLevel.Body), which
    // Is the only thing we generate
    override var flags: Int = 0b11
    override val members = children<Member>()

    var isAbstract by flag(2)
    var isEnum by flag(3)
    var hasConstConstructor by flag(7)

    var startFileOffset: Int = NO_OFFSET
    var fileEndOffset: Int = NO_OFFSET

    override fun <T> accept(visitor: TreeVisitor<T>): T {
        return visitor.visitClass(this)
    }

    override fun <T> visitChildren(visitor: TreeVisitor<T>) {
        members.forEach { it.accept(visitor) }
    }

}