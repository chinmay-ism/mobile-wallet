package org.mifospay.core.data.repository.auth

import com.mifospay.core.model.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.mifospay.core.datastore.PreferencesHelper
import javax.inject.Inject


class AuthenticationUserRepository @Inject constructor(
    preferencesHelper: PreferencesHelper
) : UserDataRepository {

    override val userData: Flow<UserData> = flow {
        emit(
            UserData(
                isAuthenticated = !preferencesHelper.token.isNullOrBlank(),
                userName = preferencesHelper.username,
               // user = preferencesHelper.user,
                clientId = preferencesHelper.clientId
            )
        )
    }
}