package network

import kotlinx.serialization.KSerializer
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import org.w3c.xhr.XMLHttpRequest
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.reflect.KClass

// adapted from https://medium.com/@animusdesign/kotlin-js-rest-interactions-bd117e49727b

/**
 * An enum representing supported HTTP verbs.
 */
enum class HTTPVerbs {
    POST,
    GET,
    PUT,
    UPDATE,
    DELETE
}

/**
 * Utilized by all HTTP responses. Gets the response as a string and amends to the coroutine
 */
fun statusHandler(xhr: XMLHttpRequest, coroutineContext: Continuation<String>) {
    if (xhr.readyState == XMLHttpRequest.DONE) {
        if (xhr.status / 100 == 2) {
            coroutineContext.resume(xhr.response as String)
        } else {
            coroutineContext.resumeWithException(RuntimeException("HTTP error: ${xhr.status}"))
        }
    }
}

/**
 * Base wrapper for the GET verb
 */
suspend fun httpGet(url: String): String = suspendCoroutine { c ->
    val xhr = XMLHttpRequest()
    xhr.onreadystatechange = { _ -> statusHandler(xhr, c) }
    xhr.open("GET", url)
    xhr.send()
}

/**
 * Base wrapper for PUT or POST verb.
 * *Need to add support outside of json uploads.*
 */
suspend fun httpPutOrPost(url: String, data: String, httpVerb: HTTPVerbs): String = suspendCoroutine { c ->
    val xhr = XMLHttpRequest()
    xhr.onreadystatechange = { _ -> statusHandler(xhr, c) }
    xhr.open(httpVerb.name, url, true)
    xhr.setRequestHeader("Content-type", "application/json; charset=utf-8")
    xhr.send(data)
}

/**
 * Wrapper for uploading items, and casting to native Kotlin classes.
 */
@UnstableDefault
suspend inline fun <reified S : Any, reified R : Any> uploadBase(
    url: String,
    httpVerb: HTTPVerbs,
    data: S,
    dataSerializer: KSerializer<S>,
    responseSerializer: KSerializer<R>,
    debug: Boolean = false
): R {
    if (debug) console.log("uploadBase for url: $url, verb: $httpVerb, dataSerializer.descriptor: ${dataSerializer.descriptor.name}, responseSerializer.descriptor: ${responseSerializer.descriptor.name}, data: $data")
    val jsonString = Json.nonstrict.stringify(dataSerializer, data)
    val response = httpPutOrPost(url, jsonString, httpVerb)
    return Json.nonstrict.parse(responseSerializer, response)
}

/**
 * Slim wrapper simply setting HTTPVerb to PUT
 */
@UnstableDefault
suspend inline fun <reified S : Any, reified R : Any> putBase(
    url: String,
    data: S,
    dataSerializer: KSerializer<S>,
    responseSerializer: KSerializer<R>,
    debug: Boolean = false
): R {
    return uploadBase(
        url,
        HTTPVerbs.PUT,
        data,
        dataSerializer,
        responseSerializer,
        debug
    )
}

/**
 * Slim wrapper simply setting HTTPVerb to POST
 */
@UnstableDefault
suspend inline fun <reified S : Any, reified R : Any> postBase(
    url: String,
    data: S,
    dataSerializer: KSerializer<S>,
    responseSerializer: KSerializer<R>,
    debug: Boolean = false
): R {
    return uploadBase(
        url,
        HTTPVerbs.POST,
        data,
        dataSerializer,
        responseSerializer,
        debug
    )
}

@UnstableDefault
suspend inline fun <reified R : Any> getBase(
    url: String,
    clazz: KClass<R>,
    serializer: KSerializer<R>,
    debug: Boolean = false
): R {
    if (debug) console.log("getBase for url: $url, class: ${clazz.simpleName}, descriptor: ${serializer.descriptor.name}")
    val rawData = httpGet(url)
    if (debug) console.log(rawData)
    val parsed = Json.nonstrict.parse(serializer, rawData)
    if (debug) console.log("Received a response of:\n $parsed")

    return parsed
}