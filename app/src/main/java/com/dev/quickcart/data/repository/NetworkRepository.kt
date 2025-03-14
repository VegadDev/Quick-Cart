package com.dev.quickcart.data.repository

import com.dev.quickcart.data.DataDao
import javax.inject.Inject

interface NetworkRepository {


}


class NetworkRepositoryImpl
@Inject
constructor(
    private val dataDao: DataDao,
) : NetworkRepository {


}