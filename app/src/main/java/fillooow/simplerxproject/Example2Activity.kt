package fillooow.simplerxproject

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.function.Predicate

// filter and disposable

class Example2Activity : AppCompatActivity() {

    companion object {
        val TAG = "RxTAG"
    }

    lateinit var disposable: Disposable
    lateinit var animalsObservable: Observable<String>
    lateinit var animalsObserver: Observer<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example2)

        animalsObservable = getMyAnimalsObservable()
        animalsObserver = getMyAnimalsObserver()

        // Subscribing observer to observable
        animalsObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .filter{
                it.toLowerCase().startsWith("b")
            }
            .subscribeWith(animalsObserver)
    }

    private fun getMyAnimalsObserver(): Observer<String>{
        return object : Observer<String> {

            override fun onComplete() {
                Log.d(TAG, "All items are emitted")
            }

            override fun onSubscribe(d: Disposable) {
                Log.d(TAG, "onSubscribe")
                disposable = d
            }

            override fun onNext(t: String) {
                Log.d(TAG, "Name: $t")
            }

            override fun onError(e: Throwable) {
                Log.d(TAG, "onError ${e.message}")
            }
        }
    }

    private fun getMyAnimalsObservable(): Observable<String>{
        return Observable.fromArray(
            "Ant", "Ape",
            "Bat", "Bee", "Bear", "Butterfly",
            "Cat", "Crab", "Cod",
            "Dog", "Dove",
            "Fox", "Frog")
    }

    override fun onDestroy() {
        super.onDestroy()

        disposable.dispose()
    }
}
