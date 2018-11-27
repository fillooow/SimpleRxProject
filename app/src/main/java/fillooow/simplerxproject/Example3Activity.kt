package fillooow.simplerxproject

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

// CompositeDisposable

class Example3Activity : AppCompatActivity() {

    companion object {
        val TAG = "RxTAG"
        val compositeDisposable = CompositeDisposable()
    }

    lateinit var animalsObservable: Observable<String>
    lateinit var animalsObserver: DisposableObserver<String>
    lateinit var animalsObserverAllCaps: DisposableObserver<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example3)

        animalsObservable = getMyAnimalsObservable()
        animalsObserver = getMyAnimalsObserver()
        animalsObserverAllCaps = getMyAnimalsAllCapsObserver()

        compositeDisposable.add(
            animalsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter{
                    it.toLowerCase().startsWith("b")
                }
                .subscribeWith(animalsObserver)
        )

        compositeDisposable.add(
            animalsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter {
                    it.toLowerCase().startsWith("c")
                }.map {
                    it.toUpperCase()
                }.subscribeWith(animalsObserverAllCaps)
        )
    }

    fun getMyAnimalsObserver(): DisposableObserver<String> {
        return object : DisposableObserver<String>() {
            override fun onComplete() {
                Log.d(TAG, "All items emitted")
            }

            override fun onNext(t: String) {
                Log.d(TAG, "Name $t")
            }

            override fun onError(e: Throwable) {
                Log.e(TAG, "onError + ${e.message}")
            }
        }
    }

    fun getMyAnimalsAllCapsObserver(): DisposableObserver<String>{
        return object : DisposableObserver<String>() {
            override fun onComplete() {
                Log.d(TAG, "All items are emitted")
            }

            override fun onNext(t: String) {
                Log.d(TAG, "Name: $t")
            }

            override fun onError(e: Throwable) {
                Log.e(TAG, "onError ${e.message}")
            }
        }
    }

    fun getMyAnimalsObservable(): Observable<String> {
        return Observable.fromArray("Ant", "Ape",
            "Bat", "Bee", "Bear", "Butterfly",
            "Cat", "Crab", "Cod",
            "Dog", "Dove",
            "Fox", "Frog")
    }

    override fun onDestroy() {
        super.onDestroy()

        // don't send events once the activity destroyed
        compositeDisposable.clear()
    }
}
