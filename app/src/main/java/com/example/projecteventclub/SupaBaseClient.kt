package com.example.projecteventclub

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.realtime.Realtime


object SupaBaseClient {
    val client = createSupabaseClient(
        supabaseUrl = "https://spipnkeymdonufwklovp.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InNwaXBua2V5bWRvbnVmd2tsb3ZwIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzgzNTY4MDEsImV4cCI6MjA5MzkzMjgwMX0.T6PxLXVwJVTW0Dl-BcjaqhmnACx0w46j5lTf5cy2_nA"
    ) {
        install(Postgrest)
        install(Auth)
        install(Storage)
        install(Realtime)
    }
}
