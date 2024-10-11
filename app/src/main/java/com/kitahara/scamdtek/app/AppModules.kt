package com.kitahara.scamdtek.app

import com.kitahara.scamdtek.data.contact_number.CallerInfoRepository
import com.kitahara.scamdtek.data.database.CallerInfoDatabase
import com.kitahara.scamdtek.domain.ResolveCallerInfo
import com.kitahara.scamdtek.domain.SyncCallerInfoUseCase
import com.kitahara.scamdtek.presentation.contact_detail.ContactDetailViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


object AppModules {

    private val contactDetailModule = module {
        single { CallerInfoRepository() }
        viewModel { ContactDetailViewModel(get(), get()) }
    }
    private val callerInfoModule = module {
        factory { SyncCallerInfoUseCase(get(), get()) }
        factory { ResolveCallerInfo(get()) }
    }

    private val databaseModule = module {
        single { CallerInfoDatabase.create(get()) }
        single { (get() as CallerInfoDatabase).riskWithCommentsDao() }
    }

    val appModules: List<Module> = listOf(
        contactDetailModule,
        databaseModule,
        callerInfoModule,
    )
}