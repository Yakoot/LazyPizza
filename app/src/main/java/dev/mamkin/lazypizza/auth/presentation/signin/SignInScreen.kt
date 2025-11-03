package dev.mamkin.lazypizza.auth.presentation.signin

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.mamkin.lazypizza.R
import dev.mamkin.lazypizza.auth.presentation.signin.components.SignInTextField
import dev.mamkin.lazypizza.core.presentation.designsystem.buttons.FilledButton
import dev.mamkin.lazypizza.core.presentation.designsystem.buttons.TextButton
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.AppTheme
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import dev.mamkin.lazypizza.core.presentation.util.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignInRoot(
    viewModel: SignInViewModel = koinViewModel(),
    backToHome: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.event) {
        when (it) {
            SignInEvent.BackToHome -> backToHome()
        }
    }

    SignInScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun SignInScreen(
    state: SignInState,
    onAction: (SignInAction) -> Unit,
) {
    val activity = LocalActivity.current

    Scaffold(
        contentWindowInsets = WindowInsets(0),
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(paddingValues = paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(184.dp))
            Text(
                text = stringResource(R.string.sign_in_page_title),
                color = AppTheme.colors.textPrimary,
                style = AppTheme.typography.title1Medium
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = if (state.isCodeSent) {
                    stringResource(R.string.sign_in_page_enter_code_subtitle)
                } else {
                    stringResource(R.string.sign_in_page_enter_phone_subtitle)
                },
                color = AppTheme.colors.textSecondary,
                style = AppTheme.typography.body3Regular
            )
            Spacer(modifier = Modifier.height(20.dp))
            SignInTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.phoneNumber,
                onValueChange = {
                    onAction(SignInAction.OnPhoneNumberChanged(it))
                },
                placeholder = "+1 000 000 0000"
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (state.isCodeSent) {
                FilledButton(
                    text = stringResource(R.string.sign_in_button_text_confirm),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = state.isCodeSubmitEnabled
                ) {
                    onAction(SignInAction.OnCodeSubmitted)
                }
            } else {
                FilledButton(
                    text = stringResource(R.string.sign_in_button_text_continue),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = state.isPhoneSubmitEnabled
                ) {
                    activity?.let {
                        onAction(SignInAction.OnPhoneNumberSubmitted(it))

                    }
                }
            }
            Spacer(modifier = Modifier.height(2.dp))
            TextButton(
                text = stringResource(R.string.sign_in_continue_without_sign_in),
                onClick = {
                    onAction(SignInAction.OnContinueWithoutSignInClicked)
                },
            )
            Spacer(modifier = Modifier.height(2.dp))
            if (state.isResendEnabled) {
                TextButton(
                    text = stringResource(R.string.sign_in_resend_button),
                    onClick = {
                        onAction(SignInAction.OnResendClicked)
                    },
                )
            } else {
                Text(
                    text = state.resendText,
                    color = AppTheme.colors.textSecondary,
                    style = AppTheme.typography.body3Regular
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    LazyPizzaTheme {
        SignInScreen(
            state = SignInState(),
            onAction = {}
        )
    }
}
