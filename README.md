# Botterknife

A library inspired by [kotterknife](https://github.com/JakeWharton/kotterknife) and 
[Buffer Knife](http://jakewharton.github.io/butterknife/).

It is implemented completely with Kotlin's delegate properties (kotterknife) but retains the 
ability to reset any bound views (Butter Knife).

#### Usage: Android

For android there are a number of implementations, but the basic pattern is the following:
```kotlin
class SomeActivity : AppCompatActivity(), ActivityBinder.Delegate {
    override val binder = ActivityBinder()
    
    val title: TextView by bindView(R.id.title)
    
    val recycler: RecyclerView by bindView(R.id.recycler_view)
    
    val optionalToolbar: Toolbar? by bindOptionalView(R.id.toolbar)
    
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        title.text = "Hello botterknife"
    }
    
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        binder.unbind()
    }
}
```

Such a binder also exists for:
* [Fragments](botterknife-android/src/main/kotlin/net/serverpeon/botterknife/typed/FragmentBinder.kt)
* [Dialogs](botterknife-android/src/main/kotlin/net/serverpeon/botterknife/typed/DialogBinder.kt)
* [support.v4 Fragments](botterknife-android/src/main/kotlin/net/serverpeon/botterknife/typed/SupportFragmentBinder.kt)
* [Views](botterknife-android/src/main/kotlin/net/serverpeon/botterknife/typed/ViewBinder.kt)
* [recyclerview.v7 ViewHolder](botterknife-android/src/main/kotlin/net/serverpeon/botterknife/typed/ViewHolderBinder.kt)

If you have a view-holding type that is not in this list, creating a new binder is fairly trivial:
```kotlin
class <Type>Binder : AndroidBinder<<Type>>() {
    override fun findView(source: <Type>, id: Int): View? {
        return < way to get view from Type >
    }

    interface Delegate : AndroidBinder.Delegate<<Type>, <Type>Binder>
}
```

#### Other platforms

All binders have [LookupBinder](botterknife-core/src/main/kotlin/net/serverpeon/botterknife/LookupBinder.kt) as a root.
Look at the implementation of [AndroidBinder](botterknife-android/src/main/kotlin/net/serverpeon/botterknife/AndroidBinder.kt)
to see how it can be specialized for a given platform.