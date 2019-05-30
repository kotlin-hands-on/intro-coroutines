package contributors

fun main() {
    setDefaultFontSize(18f)
    ContributorsUI().apply {
        pack()
        setLocationRelativeTo(null)
        isVisible = true
    }
}