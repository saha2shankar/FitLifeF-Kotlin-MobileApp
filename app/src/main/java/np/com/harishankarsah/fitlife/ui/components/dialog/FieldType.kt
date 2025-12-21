package np.com.harishankarsah.fitlife.ui.components.dialog
sealed class FieldType {
    object Text : FieldType()
    object Number : FieldType()
    data class Dropdown(val options: List<String>) : FieldType()
    object Checkbox : FieldType()
}
