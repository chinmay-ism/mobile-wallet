package org.mifospay.core.data.domain.usecase.twofactor

import org.mifospay.core.data.base.UseCase
import org.mifospay.core.data.fineract.repository.FineractRepository
import com.mifospay.core.model.domain.twofactor.AccessToken
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by ankur on 01/June/2018
 */
class ValidateOTP @Inject constructor(private val mFineractRepository: FineractRepository) :
    UseCase<ValidateOTP.RequestValues, ValidateOTP.ResponseValue>() {

    class RequestValues(val token: String) : UseCase.RequestValues
    class ResponseValue(val accessToken: AccessToken) : UseCase.ResponseValue

    override fun executeUseCase(requestValues: RequestValues) {
        mFineractRepository.validateToken(requestValues.token)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : Subscriber<AccessToken>() {
                override fun onCompleted() {}
                override fun onError(e: Throwable) {
                    useCaseCallback.onError(e.toString())
                }

                override fun onNext(accessToken: AccessToken) {
                    useCaseCallback.onSuccess(ResponseValue(accessToken))
                }
            })

    }
}
