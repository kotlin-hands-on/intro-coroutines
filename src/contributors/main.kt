package contributors

fun main() {
    setDefaultFontSize(14f)
    ContributorsUI().apply {
        pack()
        setLocationRelativeTo(null)
        isVisible = true
    }
}