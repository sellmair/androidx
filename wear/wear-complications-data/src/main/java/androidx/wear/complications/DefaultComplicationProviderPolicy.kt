/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.wear.complications

import android.content.ComponentName
import androidx.annotation.RestrictTo
import java.util.ArrayList

/**
 * A watch face may wish to try and set one or more non-system providers as the default provider
 * for a complication. If a provider can't be used for some reason (e.g. it isn't installed or it
 * doesn't support the requested type, or the watch face lacks the necessary permission) then the
 * next one will be tried. A system provider acts as a final fallback in case no non-system
 * providers can be used.
 *
 * If the DefaultComplicationProviderPolicy is empty then no default is set.
 */
class DefaultComplicationProviderPolicy {
    /** List of up to two non-system providers to be tried in turn. This may be empty. */
    val primaryProvider: ComponentName?

    val secondaryProvider: ComponentName?

    /** Fallback in case none of the non-system providers could be used. */
    @SystemProviders.ProviderId
    val systemProviderFallback: Int

    /** @hide */
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    constructor(
        providers: List<ComponentName>,
        systemProviderFallback: Int
    ) {
        this.primaryProvider = if (providers.isNotEmpty()) providers[0] else null
        this.secondaryProvider = if (providers.size >= 2) providers[1] else null
        this.systemProviderFallback = systemProviderFallback
    }

    /** No default complication provider. */
    constructor() {
        primaryProvider = null
        secondaryProvider = null
        systemProviderFallback = NO_DEFAULT_PROVIDER
    }

    /**
     * Uses systemProvider as the default complication provider.
     */
    constructor(systemProvider: Int) {
        primaryProvider = null
        secondaryProvider = null
        systemProviderFallback = systemProvider
    }

    /**
     * Attempts to use provider as the default complication provider, if not present then
     * systemProviderFallback will be used instead.
     */
    constructor(provider: ComponentName, systemProviderFallback: Int) {
        primaryProvider = provider
        secondaryProvider = null
        this.systemProviderFallback = systemProviderFallback
    }

    /**
     * Attempts to use primaryProvider as the default complication provider, if not present then
     * secondaryProvider will be tried and if that's not present then systemProviderFallback
     * will be used instead.
     */
    constructor(
        primaryProvider: ComponentName,
        secondaryProvider: ComponentName,
        systemProviderFallback: Int
    ) {
        this.primaryProvider = primaryProvider
        this.secondaryProvider = secondaryProvider
        this.systemProviderFallback = systemProviderFallback
    }

    /** Whether or not this DefaultComplicationProviderPolicy contains a default provider. */
    fun isEmpty() =
        primaryProvider == null && systemProviderFallback == NO_DEFAULT_PROVIDER

    /** @hide */
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    fun providersAsList() = ArrayList<ComponentName>().apply {
        primaryProvider?.let { add(it) }
        secondaryProvider?.let { add(it) }
    }

    companion object {
        internal const val NO_DEFAULT_PROVIDER = SystemProviders.NO_PROVIDER
    }
}