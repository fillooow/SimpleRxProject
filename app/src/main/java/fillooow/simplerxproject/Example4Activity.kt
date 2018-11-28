package fillooow.simplerxproject

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers


// CompositeDisposable
class Example4Activity : AppCompatActivity() {

    companion object {
        val TAG = "RxTAG"
        val disposable: CompositeDisposable = CompositeDisposable()
    }

    lateinit var myNotesObserver: DisposableObserver<Note>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example4)

        myNotesObserver = getNotesObserver()

        disposable.add(
            getNotesObservable().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    it.note = it.note.toUpperCase()
                    it
                }
                .subscribeWith(myNotesObserver)
        )
    }

    fun prepareNotes(): List<Note>{
        var notes: MutableList<Note> = mutableListOf()
        notes.add(Note(1, "buy tooth paste"))
        notes.add(Note(2, "call brother"))
        notes.add(Note(3, "watch narcos tonight"))
        notes.add(Note(4, "pay power bill"))

        return notes
    }

    fun getNotesObservable(): Observable<Note>{
        val notes: List<Note> = prepareNotes()
        return Observable.create {
            for (note in notes) {
                if (!it.isDisposed){ // onNext only if observable is not disposed
                    it.onNext(note)
                }
            }

            if (!it.isDisposed) {
                it.onComplete()
            }
        }
    }

    fun getNotesObserver(): DisposableObserver<Note>{
        return object : DisposableObserver<Note>() {
            override fun onComplete() {
                Log.d(TAG, "All items are emitted")
            }

            override fun onNext(item: Note) {
                Log.d(TAG, "Note: ${item.note}")
            }

            override fun onError(e: Throwable) {
                Log.e(TAG, "error: ${e.message}")
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }
}
