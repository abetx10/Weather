package com.example.weather.di

import android.content.Context
import androidx.room.Room
import com.example.weather.R
import com.example.weather.WeatherApplication
import com.example.weather.data.WeatherDataManager
import com.example.weather.data.room.AppDatabase
import com.example.weather.data.room.WeatherDao
import com.example.weather.repository.CityRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideCityRepository(): CityRepository {
        return CityRepository()
    }

    @Singleton
    @Provides
    fun provideWeatherDataManager(cityRepository: CityRepository): WeatherDataManager {
        return WeatherDataManager(cityRepository)
    }

    @Provides
    @Singleton
    fun provideApiKey(@ApplicationContext context: Context): String {
        return (context as WeatherApplication).openWeatherMapApiKey
    }

    @Provides
    @Singleton
    fun provideCities(@ApplicationContext context: Context): Array<String> {
        return context.resources.getStringArray(R.array.cities)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "weather_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideWeatherDao(database: AppDatabase): WeatherDao {
        return database.weatherDao()
    }
}