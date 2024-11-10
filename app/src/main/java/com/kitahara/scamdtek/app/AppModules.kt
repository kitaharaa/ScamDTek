package com.kitahara.scamdtek.app

import com.kitahara.scamdtek.data.caller_info.CallerInfoApi
import com.kitahara.scamdtek.data.database.CallerInfoDatabase
import com.kitahara.scamdtek.domain.ResolveCallerInfoUseCase
import com.kitahara.scamdtek.data.CallerInfoRepository
import com.kitahara.scamdtek.presentation.contact_detail.ContactDetailViewModel
import com.kitahara.scamdtek.presentation.contacts.ContactsViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


object AppModules {

    private val contactDetailModule = module {
        single { CallerInfoApi() }
        viewModel { ContactDetailViewModel(get(), get()) }
    }
    private val callerInfoModule = module {
        factory { CallerInfoRepository(get(), get()) }
        factory { ResolveCallerInfoUseCase(get()) }
    }
    private val contactsModule = module {
        viewModel { ContactsViewModel(get()) }
    }
    private val databaseModule = module {
        single { CallerInfoDatabase.create(get()) }
        single { (get() as CallerInfoDatabase).riskWithCommentsDao() }
    }

    val appModules: List<Module> = listOf(
        contactDetailModule,
        databaseModule,
        callerInfoModule,
        contactsModule,
    )
}