package fillooow.simplerxproject

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

// observer and observable

class Example1Activity : AppCompatActivity() {

    companion object {
        val TAG = "RxTag"
    }

    lateinit var animalsObservable: Observable<String>
    lateinit var animalsObserver: Observer<String>
    lateinit var disposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example1)

        // Observable emits data
        animalsObservable = Observable.just("Ant", "Bee", "Cat", "Dog", "Fox")

        // Observer
        animalsObserver = getMyAnimalsObserver()

        // Observer subscribing to observable
        animalsObservable
            .subscribeOn(Schedulers.io()) // run tusk on a background thread
            .observeOn(AndroidSchedulers.mainThread()) // observer receive the data on android UI thread and so can
            // take any UI related action
            .subscribeWith(animalsObserver) // returns Disposable, so we can unsubscribe at onDestroy, FE
    }

    fun getMyAnimalsObserver(): Observer<String> {
        return object : Observer<String> {
            override fun onComplete() {
                Log.d(TAG, "All items are emitted!")
            }

            override fun onSubscribe(d: Disposable) {
                Log.d(TAG, "onSubscribe")
                disposable = d
            }

            override fun onNext(t: String) {
                Log.d(TAG, "onNext, name: $t")
            }

            override fun onError(e: Throwable) {
                Log.d(TAG, "error: ${e.message}")
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()

        // Don't send events once the activity is destroyed
        disposable.dispose()
    }
}
