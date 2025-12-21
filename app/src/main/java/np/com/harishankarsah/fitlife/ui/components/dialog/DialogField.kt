package np.com.harishankarsah.fitlife.ui.components.dialog

data class DialogField(
    val label: String,
    val placeholder: String = "",
    val type: FieldType = FieldType.Text,
    val initialValue: Any? = null
)
