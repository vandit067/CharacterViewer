package com.sample.characterviewer.model

class CharactersDataApiResponseWrapper<T> {
    var throwable: Throwable? = null
    var response: T? = null

    constructor(throwable: Throwable) {
        this.throwable = throwable
    }

    constructor(response: T) {
        this.response = response
    }
}
