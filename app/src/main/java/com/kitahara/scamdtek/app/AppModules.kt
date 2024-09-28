package com.kitahara.scamdtek.app

import com.kitahara.scamdtek.presentation.contact_detail.ContactDetailViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


object AppModules {

    var appModules: List<Module> = listOf()

    private val contactDetailModule = module {
        viewModel { ContactDetailViewModel(get(), get()) }
    }
    init {
        appModules = listOf(
            contactDetailModule,
        )
    }
}