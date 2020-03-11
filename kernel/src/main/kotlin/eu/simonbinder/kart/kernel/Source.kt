package eu.simonbinder.kart.kernel

class Source(
    val content: String,
    val importUri: Uri?,
    val fileUri: Uri?
) {

    val lineStarts: List<Int> by lazy {
        val chars = content.toCharArray()
        val lineBreakPositions = mutableListOf(0)

        chars.forEachIndexed { index, c ->
            if (c == '\n') {
                lineBreakPositions.add(index + 1)
            }
        }

        return@lazy lineBreakPositions
    }

}