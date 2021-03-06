package eu.simonbinder.kart.transformer

import eu.simonbinder.kart.kernel.Source
import eu.simonbinder.kart.kernel.Uri
import eu.simonbinder.kart.kernel.members.Component
import eu.simonbinder.kart.kernel.types.DartType
import eu.simonbinder.kart.kernel.types.DynamicType
import eu.simonbinder.kart.kernel.types.InterfaceType
import eu.simonbinder.kart.kotlin.DartBackendContext
import eu.simonbinder.kart.transformer.invoke.DartIntrinsics
import eu.simonbinder.kart.transformer.metadata.MetadataRepository
import eu.simonbinder.kart.transformer.names.Names
import org.jetbrains.kotlin.backend.common.ir.ir2string
import org.jetbrains.kotlin.backend.jvm.ir.eraseTypeParameters
import org.jetbrains.kotlin.ir.declarations.IrFile
import org.jetbrains.kotlin.ir.declarations.path
import org.jetbrains.kotlin.ir.descriptors.IrBuiltIns
import org.jetbrains.kotlin.ir.symbols.IrFileSymbol
import org.jetbrains.kotlin.ir.types.IrSimpleType
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.classOrNull
import java.io.File

class CompilationInfo(
    val names: Names,
    val component: Component,
    dartBackendContext: DartBackendContext,
    val visitedFiles: MutableMap<IrFileSymbol, Source> = mutableMapOf()
) {

    val irBuiltIns = dartBackendContext.irBuiltIns
    val dartIntrinsics = DartIntrinsics(names.dartNames, irBuiltIns, dartBackendContext.symbolTable)

    val meta = MetadataRepository()

    fun loadFile(file: IrFile): Uri {
        val symbol = file.symbol
        if (visitedFiles.containsKey(symbol)) visitedFiles[symbol]!!

        val content = File(file.path).readText(Charsets.UTF_8)
        val uri = Uri.file(file.path)
        // Note: We set the importUri because it's the only one respected by the VM - it shows up in stack traces.
        visitedFiles[symbol] = Source(content, importUri = uri, fileUri = uri)
        return uri
    }

    fun dartTypeFor(irType: IrType): DartType {
        val intrinsic = dartIntrinsics.intrinsicType(irType)
        if (intrinsic != null) return intrinsic

        val withoutParams = irType.eraseTypeParameters() as IrSimpleType
        val inducingClass = withoutParams.classOrNull!!.owner

        val dartName = names.nameFor(inducingClass)
        return InterfaceType(classReference = dartName).withNullabilityOfIr(withoutParams)
    }
}