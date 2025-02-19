package org.mifospay.core.data.domain.usecase.account


import org.mifospay.core.data.base.UseCase
import org.mifospay.core.data.fineract.entity.mapper.AccountMapper
import org.mifospay.core.data.fineract.repository.FineractRepository
import com.mifospay.core.model.domain.Account
import com.mifospay.core.model.entity.client.ClientAccounts
import org.mifospay.core.data.util.Constants
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

import javax.inject.Inject

class FetchAccount @Inject constructor(private val fineractRepository: FineractRepository) :
    UseCase<FetchAccount.RequestValues, FetchAccount.ResponseValue>() {

    @Inject
    lateinit var accountMapper: AccountMapper

    override fun executeUseCase(requestValues: RequestValues) {
        fineractRepository.getSelfAccounts(requestValues.clientId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : Subscriber<ClientAccounts>() {
                override fun onCompleted() {}

                override fun onError(e: Throwable) {
                    useCaseCallback.onError(Constants.ERROR_FETCHING_ACCOUNTS)
                }

                override fun onNext(clientAccounts: ClientAccounts) {
                    val accounts = accountMapper.transform(clientAccounts)
                    if (!accounts.isNullOrEmpty()) {
                        var walletAccount: Account? = null
                        for (account in accounts) {
                            if (account.productId.toInt() == Constants.WALLET_ACCOUNT_SAVINGS_PRODUCT_ID) {
                                walletAccount = account
                                break
                            }
                        }
                        if (walletAccount != null) {
                            useCaseCallback.onSuccess(ResponseValue(walletAccount))
                        } else {
                            useCaseCallback.onError(Constants.NO_ACCOUNT_FOUND)
                        }
                    } else {
                        useCaseCallback.onError(Constants.NO_ACCOUNTS_FOUND)
                    }
                }
            })
    }

   data  class RequestValues(val clientId: Long) : UseCase.RequestValues

    data class ResponseValue(val account: Account) : UseCase.ResponseValue
}
