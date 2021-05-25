package br.com.zup.edu.testesrreativo

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

fun main() {
subject()
}

private fun testesReativo() {
    var obs: Observable<String> = Observable.just("1", "2")
    obs.subscribe { println("$it") }

    var completable = Completable.complete()
    completable.subscribe { println("completado") }

    //  var single = Single.just("um")
    //  single.subscribe { println("$it") }

    var talvez = Maybe.just("qualquer valor")
    talvez.subscribe { println("$it") }

    var obsComIntervalo = Observable.interval(1000, TimeUnit.MILLISECONDS)
    var sub = obsComIntervalo.subscribe { println("$it") }

    //obsComIntervalo.subscribe { println("$it") }

    Thread.sleep(2000)

    sub.dispose()

    Thread.sleep(200)
}

fun mainDois(){

    var obsDois = Observable.just("1","2")
    obsDois.subscribe { println("$it sub um") }
    obsDois.subscribe { println("$it sub dois") }


}

fun frio(){

    //Frio
    var obs = Observable.interval(1000, TimeUnit.MILLISECONDS)
    obs.subscribe { println("$it primeiro sub") }

    Thread.sleep(2000)

    obs.subscribe { println("$it segundo sub") }

    Thread.sleep(2000)
    //Frio







}

fun quente(){
    var obs = Observable.interval(1000, TimeUnit.MILLISECONDS)

    var quente = obs.publish()
    quente.connect()

    Thread.sleep(2000)
    quente.subscribe{ println("$it quente")}
    Thread.sleep(200)
}

fun subject(){

    var obs = Observable.just("um", "dois", "tres")
    var subject = PublishSubject.create<String>()
    subject.subscribe{ println("$it")}

    obs.subscribe(subject)

}
