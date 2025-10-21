package com.izzy2lost.weeu.common.collections


fun <T> Set<T>.toggleInSet(item: T): Set<T> {
    return if (item in this) this - item else this + item
}
