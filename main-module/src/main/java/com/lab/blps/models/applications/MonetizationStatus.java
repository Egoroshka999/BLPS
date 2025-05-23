package com.lab.blps.models.applications;

public enum MonetizationStatus {
    NONE,               // монетизация отсутствует
    REQUESTED,          // отправлена заявка на монетизацию
    PENDING_CONTRACT,   // ожидается согласование договора
    ACTIVE,             // монетизация активна
    STOPPED             // монетизация остановлена
}
