package fillooow.simplerxproject

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class Example1Activity : AppCompatActivity() {

    companion object {
        val TAG = "RxTag"
    }

    lateinit var animalsObservable: Observable<String>
    lateinit var animalsObserver: Observer<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example1)
        Log.d("test", "started")

        // Observable emits data
        animalsObservable = Observable.just("Ant", "Bee", "Cat", "Dog", "Fox")
        animalsObserver = getMyAnimalsObserver()

        animalsObservable
            .subscribeOn(Schedulers.io()) // run tusk on a background thread
            .observeOn(AndroidSchedulers.mainThread()) // observer receive the data on android UI thread and so can
            // take any UI related actions
            .subscribe(animalsObserver)
    }

    fun getMyAnimalsObserver(): Observer<String> {
        return object : Observer<String> {
            override fun onComplete() {
                Log.d(TAG, "All items are emitted!")
            }

            override fun onSubscribe(d: Disposable) {
                Log.d(TAG, "onSubscribe")
            }

            override fun onNext(t: String) {
                Log.d(TAG, "onNext, name: $t")
            }

            override fun onError(e: Throwable) {
                Log.d(TAG, "onError")
            }

        }
    }
}
