package np.com.harishankarsah.fitlife.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import np.com.harishankarsah.fitlife.ui.theme.*
import np.com.harishankarsah.fitlife.ui.utils.Size

@Composable
fun GlobalTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "",
    placeholder: String = "",
    modifier: Modifier = Modifier.fillMaxWidth(),
    isPassword: Boolean = false,
    error: String? = null,
    success: Boolean = false,
    leadingIcon: (@Composable (() -> Unit))? = null,
    trailingIcon: (@Composable (() -> Unit))? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    textStyle: TextStyle = LocalTextStyle.current,
    isRequired: Boolean = false
) {
    var isFocused by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    val borderColor = when {
        error != null -> Error
        success -> Success
        isFocused -> Primary
        else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
    }

    val backgroundColor = when {
        !enabled -> Surface.copy(alpha = 0.4f)
        error != null -> Error.copy(alpha = 0.08f)
        success -> Success.copy(alpha = 0.08f)
        else -> Surface
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Label with optional required indicator
        if (label.isNotEmpty()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 2.dp)
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                )
                if (isRequired) {
                    Spacer(modifier = Modifier.width(0.dp))
                    Text(
                        text = "*",
                        style = MaterialTheme.typography.labelMedium,
                        color = Secondary
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .clip(Shapes.small)
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = Shapes.small,
                )
                .background(backgroundColor)
        ) {
            TextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = {
                    if (placeholder.isNotEmpty()) {
                        Text(
                            text = placeholder,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(53.dp)
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    },
                singleLine = singleLine,
                enabled = enabled,
                readOnly = readOnly,
                maxLines = maxLines,
                minLines = minLines,
                textStyle = textStyle,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    cursorColor = Primary,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    errorLabelColor = Color.Transparent,
                    errorPlaceholderColor = Color.Transparent,
                    errorSupportingTextColor = Color.Transparent,
                    errorCursorColor = Error,
                ),
                shape = Shapes.small,
                leadingIcon = leadingIcon,
                trailingIcon = {
                    if (isPassword) {
                        val image = if (passwordVisible) Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff
                        val contentDescription = if (passwordVisible) "Hide password"
                        else "Show password"

                        IconButton(
                            onClick = { passwordVisible = !passwordVisible },
                            modifier = Modifier.size(Size.iconSm)
                        ) {
                            Icon(
                                imageVector = image,
                                contentDescription = contentDescription,
                                tint = if (error != null) Error
                                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    } else if (error != null) {
                        Icon(
                            imageVector = Icons.Filled.Error,
                            contentDescription = "Error",
                            tint = Error,
                            modifier = Modifier
                                .size(20.dp)
                                .padding(end = 12.dp)
                        )
                    } else if (success) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "Success",
                            tint = Success,
                            modifier = Modifier
                                .size(20.dp)
                                .padding(end = 12.dp)
                        )
                    } else if (trailingIcon != null) {
                        trailingIcon()
                    }
                },
                visualTransformation = if (isPassword && !passwordVisible)
                    PasswordVisualTransformation() else VisualTransformation.None,
                keyboardOptions = if (isPassword)
                    KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    )
                else keyboardOptions,
                keyboardActions = keyboardActions,
                isError = error != null,

            )
        }

        // Error message with improved styling
        if (error != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 4.dp, top = 2.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = "Error",
                    tint = Error,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = error,
                    color = Error,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }
    }
}


@Composable
@Preview(showBackground = true, backgroundColor = 0xFF141516)
fun GlobalTextFieldPreview() {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Primary,
            secondary = Secondary,
            surface = Surface,
            background = Background,
            onSurface = OnSurface,
            onBackground = OnBackground,
            error = Error
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Normal TextField
            GlobalTextField(
                value = "",
                onValueChange = {},
                label = "Username",
                placeholder = "Enter your username",
                leadingIcon = {
                    Icon(
                        Icons.Filled.Person,
                        contentDescription = "Username",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            )

            // Password Field
            GlobalTextField(
                value = "password123",
                onValueChange = {},
                label = "Password",
                placeholder = "Enter your password",
                isPassword = true,
                leadingIcon = {
                    Icon(
                        Icons.Filled.Lock,
                        contentDescription = "Password",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            )
            // Error State
            GlobalTextField(
                value = "invalid@email",
                onValueChange = {},
                label = "Email",
                placeholder = "Enter your email",
                error = "Please enter a valid email address",
                isRequired = true,
                leadingIcon = {
                    Icon(
                        Icons.Filled.Email,
                        contentDescription = "Email",
                        tint = Error
                    )
                }
            )

            // Success State
            GlobalTextField(
                value = "valid@email.com",
                onValueChange = {},
                label = "Email",
                placeholder = "Enter your email",
                success = true,
                leadingIcon = {
                    Icon(
                        Icons.Filled.Email,
                        contentDescription = "Email",
                        tint = Success
                    )
                }
            )

            // Disabled State
            GlobalTextField(
                value = "Read-only content",
                onValueChange = {},
                label = "Disabled Field",
                placeholder = "This field is disabled",
                enabled = false,
                leadingIcon = {
                    Icon(
                        Icons.Filled.Info,
                        contentDescription = "Disabled",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )
                }
            )
        }
    }
}