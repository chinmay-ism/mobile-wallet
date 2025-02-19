package org.mifospay.core.data.domain.usecase.user

import com.mifospay.core.model.entity.UserWithRole
import org.mifospay.core.data.base.UseCase
import org.mifospay.core.data.fineract.repository.FineractRepository
import org.mifospay.core.data.util.Constants
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class FetchUsers @Inject constructor(
    private val mFineractRepository: FineractRepository
) : UseCase<FetchUsers.RequestValues, FetchUsers.ResponseValue>() {

    override fun executeUseCase(requestValues: RequestValues) {
        mFineractRepository.users
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : Subscriber<List<UserWithRole>>() {
                override fun onCompleted() {}
                override fun onError(e: Throwable) {
                    useCaseCallback.onError(e.toString())
                }

                override fun onNext(userWithRoles: List<UserWithRole>) {
                    val tbp: MutableList<UserWithRole> = ArrayList()
                    for (userWithRole in userWithRoles) {
                        for ((_, name) in userWithRole.selectedRoles!!) {
                            if (name == Constants.MERCHANT) {
                                tbp.add(userWithRole)
                                break
                            }
                        }
                    }
                    useCaseCallback.onSuccess(ResponseValue(tbp))
                }
            })
    }

    class RequestValues : UseCase.RequestValues
    data class ResponseValue(val userWithRoleList: List<UserWithRole>) : UseCase.ResponseValue
}
