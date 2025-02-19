package org.mifospay.core.data.domain.usecase.kyc

import org.mifospay.core.data.base.UseCase
import com.mifospay.core.model.entity.kyc.KYCLevel1Details
import org.mifospay.core.data.fineract.repository.FineractRepository
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by ankur on 24/May/2018
 */
class FetchKYCLevel1Details @Inject constructor(private val mFineractRepository: FineractRepository) :
    UseCase<FetchKYCLevel1Details.RequestValues, FetchKYCLevel1Details.ResponseValue>() {

    class RequestValues(val clientId: Int) : UseCase.RequestValues
    class ResponseValue(
        val kycLevel1DetailsList: List<KYCLevel1Details?>
    ) : UseCase.ResponseValue

    override fun executeUseCase(requestValues: RequestValues) {
        mFineractRepository.fetchKYCLevel1Details(requestValues.clientId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : Subscriber<List<KYCLevel1Details?>>() {
                override fun onCompleted() {}
                override fun onError(e: Throwable) {
                    useCaseCallback.onError(e.toString())
                }

                override fun onNext(kycLevel1Details: List<KYCLevel1Details?>) {
                    useCaseCallback.onSuccess(
                        ResponseValue(kycLevel1Details)
                    )
                }
            })

    }
}
