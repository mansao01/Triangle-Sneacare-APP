package com.mansao.trianglesneacare.utils

sealed class ConnectionStatus{
    object  Available: ConnectionStatus()
    object  UnAvailable: ConnectionStatus()
}