package com.kitahara.scamdtek.app

import com.kitahara.scamdtek.data.contact_number.CallerInfoRepository
import com.kitahara.scamdtek.data.database.CallerInfoDatabase
import com.kitahara.scamdtek.domain.GetCallerInfoUseCase
import com.kitahara.scamdtek.presentation.contact_detail.ContactDetailViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


object AppModules {

    var appModules: List<Module> = listOf()
    private val contactDetailModule = module {
        single { CallerInfoRepository() }
        viewModel { ContactDetailViewModel(get(), GetCallerInfoUseCase(get())) }
    }

    private val databaseModule = module {
        single { CallerInfoDatabase.create(get()) }
        single { (get() as CallerInfoDatabase).riskWithCommentsDao() }
    }

    init {
        appModules = listOf(
            contactDetailModule,
            databaseModule,
        )
    }
}