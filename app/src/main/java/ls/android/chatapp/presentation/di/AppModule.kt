package ls.android.chatapp.presentation.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ls.android.chatapp.data.repository.mock.ConnectionsRepositoryMock
import ls.android.chatapp.data.repository.mock.MessagesRepositoryMock
import ls.android.chatapp.domain.repository.ConnectionRepository
import ls.android.chatapp.domain.repository.MessagesRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideConnectionRepository(): ConnectionRepository = ConnectionsRepositoryMock()

    @Provides
    @Singleton
    fun provideMessagesRepository(): MessagesRepository = MessagesRepositoryMock()
}