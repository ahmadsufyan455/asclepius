package com.dicoding.asclepius.di

import androidx.room.Room
import com.dicoding.asclepius.data.api.ApiClient
import com.dicoding.asclepius.data.database.HistoryDatabase
import com.dicoding.asclepius.data.repository.ArticleRepository
import com.dicoding.asclepius.data.repository.HistoryRepository
import com.dicoding.asclepius.viewmodel.ArticleViewModel
import com.dicoding.asclepius.viewmodel.HistoryViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val databaseModule = module {
    factory { get<HistoryDatabase>().historyDao() }
    single {
        Room.databaseBuilder(androidContext(), HistoryDatabase::class.java, "history.db")
            .fallbackToDestructiveMigration().build()
    }
}

val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiClient::class.java)
    }
}

val repositoryModule = module {
    single { ArticleRepository(get()) }
    single { HistoryRepository(get()) }
}

val viewModelModule = module {
    viewModel { ArticleViewModel(get()) }
    viewModel { HistoryViewModel(get()) }
}