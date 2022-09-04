package ru.example.cryptocurrency.domain.use_case.get_coin

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import ru.example.cryptocurrency.R
import ru.example.cryptocurrency.common.Resource
import ru.example.cryptocurrency.common.UiText
import ru.example.cryptocurrency.data.remote.dto.toCoinDetail
import ru.example.cryptocurrency.domain.model.CoinDetail
import ru.example.cryptocurrency.domain.repository.CoinRepository
import java.io.IOException
import javax.inject.Inject

class GetCoinUseCase @Inject constructor(
    private val repository: CoinRepository
) {
    operator fun invoke(coinId: String): Flow<Resource<CoinDetail>> = flow {
        try {
            emit(Resource.Loading())
            val coin = repository.getCoinById(coinId).toCoinDetail()
            emit(Resource.Success(coin))
        } catch(e: HttpException) {
            if(e.localizedMessage.isNullOrEmpty()) {
                emit(Resource.Error(UiText.StringResource(R.string.unknown_exception)))
            }
            else {
                emit(Resource.Error(UiText.DynamicString(e.localizedMessage!!)))
            }
        } catch(e: IOException) {
            emit(Resource.Error(UiText.StringResource(R.string.io_exception)))
        }
    }
}