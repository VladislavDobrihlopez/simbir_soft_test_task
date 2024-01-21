package com.dobrihlopez.simbir_soft_test_task.ioc

import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent
interface EditorScreenSubcomponent {
    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        @NamedId
        fun eventId(namedId: Long? = null): Builder

        @BindsInstance
        @NamedCardColor
        fun eventCardColor(value: String? = null): Builder

        fun create(): EditorScreenSubcomponent
    }
}