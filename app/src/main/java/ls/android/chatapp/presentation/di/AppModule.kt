package ls.android.chatapp.presentation.di

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ls.android.chatapp.data.repository.real.AuthenticationRepositoryImpl
import ls.android.chatapp.data.repository.real.ConnectionRepositoryImpl
import ls.android.chatapp.data.repository.real.MessageRepositoryImpl
import ls.android.chatapp.domain.repository.AuthenticationRepository
import ls.android.chatapp.domain.repository.ConnectionRepository
import ls.android.chatapp.domain.repository.MessagesRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebase(): FirebaseFirestore = Firebase.firestore

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideConnectionRepository(
        db: FirebaseFirestore,
        auth: FirebaseAuth,
        @ApplicationContext context: Context
    ): ConnectionRepository =
        ConnectionRepositoryImpl(db, auth, context)

    @Provides
    @Singleton
    fun provideMessagesRepository(
        db: FirebaseFirestore, auth: FirebaseAuth,
        @ApplicationContext context: Context
    ): MessagesRepository =
        MessageRepositoryImpl(db, auth, context)

    @Provides
    @Singleton
    fun provideAuthenticationRepository(
        auth: FirebaseAuth, @ApplicationContext context: Context
    ): AuthenticationRepository =
        AuthenticationRepositoryImpl(auth, context)
}